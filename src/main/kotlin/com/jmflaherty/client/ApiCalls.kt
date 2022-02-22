package com.jmflaherty.client

import com.jmflaherty.client.ApplicationApi.client
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlin.js.ExperimentalJsExport
import kotlinx.coroutines.async

object ApiCalls {
    private val address = Url("https://cors-test.appspot.com/test")

    suspend fun about(): HttpResponse {
        return client.get<HttpResponse>(url = address)
    }

    suspend fun aboutFlow(amount: Int): Flow<HttpResponse> = channelFlow<HttpResponse> {
        repeat(amount) {
            println("REQUEST NUMBER $it")
            trySend(about())
        }
    }.buffer()

    suspend fun aboutAsyncFlow(amount: Int): Flow<HttpResponse> = channelFlow<HttpResponse> {
        repeat(amount) {
            async {
                trySend(about())
            }.start()
        }
    }.buffer()

}


