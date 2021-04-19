package com.jmflaherty.client

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlin.js.ExperimentalJsExport
import kotlin.native.concurrent.SharedImmutable
import kotlinx.coroutines.CoroutineDispatcher

@SharedImmutable
internal expect val ApplicationDispatcher: CoroutineDispatcher

@ExperimentalJsExport
object ApplicationApi {
    val client = HttpClient() {
        install(HttpTimeout) {
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
    val service = ApiCalls
}
