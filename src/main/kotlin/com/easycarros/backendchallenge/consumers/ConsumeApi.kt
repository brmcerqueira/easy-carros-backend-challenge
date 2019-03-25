package com.easycarros.backendchallenge.consumers

import com.fasterxml.jackson.databind.ObjectMapper
import com.easycarros.backendchallenge.common.Auditor
import com.easycarros.backendchallenge.serializers.mapFrom
import com.easycarros.backendchallenge.serializers.mapTo
import io.reactivex.Completable
import io.reactivex.Single
import io.vertx.core.json.DecodeException
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.MultiMap
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.client.HttpRequest
import io.vertx.reactivex.ext.web.client.HttpResponse
import io.vertx.reactivex.ext.web.client.WebClient
import kotlin.reflect.KClass

class ConsumeApi(private val webClient: WebClient,
                 private val objectMapper: ObjectMapper,
                 private val auditor: Auditor,
                 private val port: Int?,
                 private val host: String,
                 private val ssl: Boolean,
                 private val abs: Boolean,
                 private val authorization: String? = null,
                 val options: JsonObject? = null) {

    private fun createRequest(uri: String, request: (uri: String) -> HttpRequest<Buffer>, authorization: String?): HttpRequest<Buffer> {
        val rawUri = if (abs) if (ssl) "https://$host$uri" else "http://$host$uri" else uri
        auditor.info("path -> {0}", rawUri)
        val httpRequest = request(rawUri)

        if (authorization != null) {
            httpRequest.putHeader("Authorization", authorization)
            auditor.info("authorization -> {0}", authorization)
        } else if (this.authorization != null) {
            httpRequest.putHeader("Authorization", this.authorization)
            auditor.info("authorization -> {0}", this.authorization)
        }

        if (port != null) {
            httpRequest.port(port)
        }

        if (!abs) {
            httpRequest.host(host)
        }

        return httpRequest.ssl(ssl)
    }

    private fun <T: Any> Single<HttpResponse<Buffer>>.mapResponse(kclass: KClass<T>): Single<T> {
        return this.map {
            auditor.info("result-status-message -> {0}\nresult-body -> {1}", it.statusMessage(), it.bodyAsString())

            if (it.statusCode() in 200..299) {
                val result = try {
                    objectMapper.mapTo(it.bodyAsJsonObject(), kclass)
                }
                catch (ex: DecodeException) {
                    objectMapper.mapTo(it.bodyAsJsonArray(), kclass)
                }

                auditor.info("result-body-after-map -> {0}", result)
                return@map result
            }

            throw Exception(it.statusMessage())
        }
    }

    private fun Single<HttpResponse<Buffer>>.mapResponse(): Completable {
        return this.doOnSuccess {
            auditor.info("result-status-message -> {0}", it.statusMessage())

            if (it.statusCode() !in 200..299) {
                throw Exception(it.statusMessage())
            }
        }.toCompletable()
    }

    private fun <T : Any> form(uri: String, request: (uri: String) -> HttpRequest<Buffer>, kclass: KClass<T>, formData: Map<String, String>, authorization: String?): Single<T> {
        val httpRequest = createRequest(uri, request, authorization)
        val form = MultiMap.caseInsensitiveMultiMap()
        formData.forEach { k, v -> form.add(k, v) }
        auditor.info("form-data -> {0}", formData)
        return httpRequest.rxSendForm(form).mapResponse(kclass)
    }

    private fun <T : Any> body(uri: String, request: (uri: String) -> HttpRequest<Buffer>, kclass: KClass<T>, body: Any?, authorization: String?): Single<T> {
        return body(uri, request, body, authorization).mapResponse(kclass)
    }

    private fun body(uri: String, request: (uri: String) -> HttpRequest<Buffer>, body: Any?, authorization: String?): Single<HttpResponse<Buffer>> {
        val httpRequest = createRequest(uri, request, authorization)

        return (if (body != null) {
            val bodyJsonObject = objectMapper.mapFrom(body)
            auditor.info("body -> {0}", body)
            httpRequest.rxSendJsonObject(bodyJsonObject)
        }
        else httpRequest.sendWithNone())
    }

    private fun HttpRequest<Buffer>.sendWithNone() = this.putHeader("Content-Length", "0").rxSend()

    fun <T : Any> post(uri: String, kclass: KClass<T>, body: Any?, authorization: String?) = body(uri, if (abs) webClient::postAbs else webClient::post, kclass, body, authorization)

    inline fun <reified T : Any> post(uri: String, body: Any? = null, authorization: String? = null) = post(uri, T::class, body, authorization)

    fun <T : Any> post(uri: String, kclass: KClass<T>, formData: Map<String, String>, authorization: String?) = form(uri, if (abs) webClient::postAbs else webClient::post, kclass, formData, authorization)

    fun post(uri: String, body: Any?) = body(uri, if (abs) webClient::postAbs else webClient::post, body, authorization).mapResponse()

    inline fun <reified T : Any> post(uri: String, formData: Map<String, String>, authorization: String? = null) = post(uri, T::class, formData, authorization)

    fun <T: Any> get(uri: String, kclass: KClass<T>, authorization: String? = null) = createRequest(uri, if (abs) webClient::getAbs else webClient::get, authorization).rxSend().mapResponse(kclass)

    inline fun <reified T : Any> get(uri: String, authorization: String? = null) = get(uri, T::class, authorization)
}
