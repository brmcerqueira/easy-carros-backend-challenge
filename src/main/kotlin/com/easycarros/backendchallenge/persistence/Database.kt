package com.easycarros.backendchallenge.persistence

import com.brmcerqueira.kuerongo.Json
import com.brmcerqueira.kuerongo.Pipeline
import com.easycarros.backendchallenge.common.Auditor
import com.easycarros.backendchallenge.serializers.mapFrom
import com.easycarros.backendchallenge.serializers.mapTo
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.mongo.MongoClient
import io.vertx.ext.mongo.WriteOption
import io.vertx.kotlin.ext.mongo.UpdateOptions
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

class Database @Inject constructor(private val mongoClient: MongoClient,
                                   @Named("persistence") private val objectMapper: ObjectMapper,
                                   @Named("persistence") private val auditor: Auditor) {


    fun findOne(collection: String, find: Find): Single<Optional<JsonObject>> {
        val fields = find.options.fields ?: JsonObject()
        val findJsonObject = find.raw<JsonObject>()
        auditor.info("collection -> {0}\nfind -> {1}\nfields -> {2}", collection, findJsonObject, fields)
        return Single.create { emitter ->
            mongoClient.findOne(collection, findJsonObject, fields) {
                if (!emitter.isDisposed) {
                    if (it.succeeded()) {
                        val jsonObject = it.result()
                        if (jsonObject == null) {
                            auditor.info("find-one-result -> empty")
                            emitter.onSuccess(Optional.empty())
                        }
                        else {
                            auditor.info("find-one-result -> {0}", jsonObject)
                            emitter.onSuccess(Optional.of(jsonObject))
                        }
                    } else if (it.failed()) {
                        emitter.onError(it.cause())
                    }
                }
            }
        }
    }

    fun <T : Any> genericFindOne(collection: String, find: Find, kclass: KClass<T>): Single<Optional<T>> = findOne(collection, find).map {
        if (it.isPresent) Optional.of(objectMapper.mapTo(it.get(), kclass)) else Optional.empty()
    }

    inline fun <reified T : Any> genericFindOne(collection: String, find: Find): Single<Optional<T>> =
        genericFindOne(collection, find, T::class)

    fun find(collection: String, find: Find): Single<List<JsonObject>> {
        val findJsonObject = find.raw<JsonObject>()
        auditor.info("collection -> {0}\nfind -> {1}\noptions -> {2}", collection, findJsonObject, find.options.toJson())
        return mongoClient.rxFindWithOptions(collection, findJsonObject, find.options).map {
            auditor.info("find-size -> {0}\nfind-result -> {1}", it.size, it)
            it
        }
    }

    fun <T : Any> genericFind(collection: String, find: Find, kclass: KClass<T>): Single<List<T>> = find(collection, find).map {
        it.map { objectMapper.mapTo(it, kclass) }
    }

    inline fun <reified T : Any> genericFind(collection: String, find: Find): Single<List<T>> =
        genericFind(collection, find, T::class)


    fun count(collection: String, find: Find): Single<Long> {
        val findJsonObject = find.raw<JsonObject>()
        auditor.info("collection -> {0}\nfind -> {1}", collection, findJsonObject)
        return mongoClient.rxCount(collection, findJsonObject).map {
            auditor.info("count -> {0}", it)
            it
        }
    }

    fun create(collection: String, entity: Any): Single<String> {
        val jsonObject = objectMapper.mapFrom(entity)
        jsonObject.remove("_id")
        return privateCreate(collection, jsonObject)
    }

    fun create(collection: String, json: Json): Single<String> = privateCreate(collection, json.raw())

    private fun privateCreate(collection: String, jsonObject: JsonObject): Single<String> {
        return mongoClient.rxSave(collection, jsonObject).map {
            auditor.info("collection -> {0}\ncreate -> {1}", collection, it)
            it
        }
    }

    fun update(collection: String, find: Find, entity: Any): Completable {
        auditor.info("collection -> {0}\nfind -> {1}\nentity -> {2}", collection, find.raw<JsonObject>(), entity)
        val jsonObject = objectMapper.mapFrom(entity)
        jsonObject.remove("_id")
        return privateUpdate(collection, find, jsonObject)
    }

    fun update(collection: String, find: Find, json: Json): Completable {
        val raw = json.raw<JsonObject>()
        auditor.info("collection -> {0}\nfind -> {1}\nentity -> {2}", collection, find.raw<JsonObject>(), raw)
        return privateUpdate(collection, find, raw)
    }

    private fun privateUpdate(collection: String, find: Find, jsonObject: JsonObject): Completable {
        return mongoClient.rxUpdate(collection, find.raw(), Json { "\$set" *= jsonObject }.raw()).doOnComplete {
            auditor.info("collection -> {0}\nupdate -> ok", collection)
        }
    }

    fun updateMany(collection: String, find: Find, entity: Any): Completable {
        auditor.info("collection -> {0}\nfind -> {1}\nentity -> {2}", collection, find.raw<JsonObject>(), entity)
        val jsonObject = objectMapper.mapFrom(entity)
        jsonObject.remove("_id")
        return privateUpdateMany(collection, find, jsonObject)
    }

    fun updateMany(collection: String, find: Find, json: Json): Completable {
        val raw = json.raw<JsonObject>()
        auditor.info("collection -> {0}\nfind -> {1}\nentity -> {2}", collection, find.raw<JsonObject>(), raw)
        return privateUpdateMany(collection, find, raw)
    }

    private fun privateUpdateMany(collection: String, find: Find, jsonObject: JsonObject): Completable {
        return mongoClient.rxUpdateCollectionWithOptions(collection, find.raw<JsonObject>(), Json { "\$set" *= jsonObject }.raw<JsonObject>(), UpdateOptions(multi = true))
            .toCompletable().doOnComplete {
                auditor.info("updateMany -> ok")
            }
    }

    fun aggregate(collection: String, pipeline: Pipeline): Single<List<JsonObject>> {
        val json = Json {
            "aggregate" *= collection
            "pipeline" *= pipeline.raw<JsonObject>()
            "cursor" *= JsonObject()
        }.raw<JsonObject>()
        auditor.info("aggregate -> {0}", json)
        return mongoClient.rxRunCommand("aggregate", json).map {
            auditor.info("aggregate-result -> {0}", it)
            it.getJsonObject("cursor").getJsonArray("firstBatch").map { it as JsonObject }.toList()
        }
    }

    fun <T : Any> genericAggregate(collection: String, pipeline: Pipeline, kclass: KClass<T>): Single<List<T>> =
        aggregate(collection, pipeline).map {
            it.map { objectMapper.mapTo(it, kclass) }
        }

    inline fun <reified T : Any> genericAggregate(collection: String, pipeline: Pipeline): Single<List<T>> =
        genericAggregate(collection, pipeline, T::class)

    fun aggregateOne(collection: String, pipeline: Pipeline): Single<Optional<JsonObject>> = aggregate(collection, pipeline).map {
        if (it.isNotEmpty()) Optional.of(it.first()) else Optional.empty()
    }

    fun <T : Any> genericAggregateOne(collection: String, pipeline: Pipeline, kclass: KClass<T>): Single<Optional<T>> = aggregate(collection, pipeline).map {
        if (it.isNotEmpty()) Optional.of(objectMapper.mapTo(it.first(), kclass)) else Optional.empty()
    }

    inline fun <reified T : Any> genericAggregateOne(collection: String, pipeline: Pipeline): Single<Optional<T>> =
        genericAggregateOne(collection, pipeline, T::class)

    fun remove(collection: String, find: Find, writeOption: WriteOption?): Completable =
        mongoClient.rxRemoveWithOptions(collection, find.raw<JsonObject>(), writeOption)
}
