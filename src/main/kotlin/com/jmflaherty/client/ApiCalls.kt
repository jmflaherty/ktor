package com.jmflaherty.client

import com.jmflaherty.client.ApplicationApi.client
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Url
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow

object ApiCalls {
    private val address = Url("https://cors-test.appspot.com/test")

    suspend fun about(): HttpResponse {
        return client.get<HttpResponse>(url = address)
    }

    suspend fun aboutFlow(amount: Int): Flow<HttpResponse> = channelFlow<HttpResponse> {
        repeat(amount) {
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


