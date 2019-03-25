import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "easy-carros"
version  = "1.0-SNAPSHOT"
val kotlinVersion = "1.3.10"
val vertxVersion = "3.5.3"

plugins {
    java
    application
    kotlin("jvm") version "1.3.10"
    kotlin("kapt") version "1.3.10"
    id("com.github.johnrengelman.shadow") version "4.0.3"
}

repositories {
    mavenLocal()
    jcenter()
    maven("https://plugins.gradle.org/m2/")
}

sourceSets {
    getByName("main").java.srcDirs("$buildDir/generated/source/kapt/")
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("main").java.srcDirs("src/main/java")
    getByName("main").resources.srcDirs("src/main/conf")
}

application {
    mainClassName = "io.vertx.core.Launcher"
}

dependencies {
    compile("io.vertx:vertx-unit:$vertxVersion")
    compile("io.vertx:vertx-core:$vertxVersion")
    compile("io.vertx:vertx-web:$vertxVersion")
    compile("io.vertx:vertx-mongo-client:$vertxVersion")
    compile("io.vertx:vertx-rx-java2:$vertxVersion")
    compile("io.vertx:vertx-auth-jwt:$vertxVersion")
    compile("io.vertx:vertx-web-client:$vertxVersion")
    compile("io.vertx:vertx-mail-client:$vertxVersion")
    compile("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    compile("com.brmcerqueira:kuerongo:1.0.1")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.0")
    compile("com.google.dagger:dagger:2.13")
    compile("org.mindrot:jbcrypt:0.3m")
    kapt("com.google.dagger:dagger-compiler:2.13")
}

tasks.withType<ShadowJar> {
    classifier = "fat"
    manifest {
        attributes(mapOf("Main-Verticle" to "com.easycarros.backendchallenge.presentation.MainVerticle"))
    }
    mergeServiceFiles {
        include("META-INF/services/io.vertx.core.spi.VerticleFactory")
    }
}

tasks.withType<JavaExec> {
    args = listOf(
        "run",
        "com.easycarros.backendchallenge.presentation.MainVerticle",
        "--conf=environments/${findProperty("env") ?: "dev"}-conf.json"
    )
}