package com.jmflaherty.client

import com.jmflaherty.client.ApplicationApi.client
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.Url
import kotlin.js.ExperimentalJsExport
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@ExperimentalJsExport
object ApiCalls {
    private val address = Url("https://cors-test.appspot.com/test")

    suspend fun asyncAbout(retries: Int = 5) {
        coroutineScope {
            repeat(retries) {
                async {
                    val result: String = client.get { url(address.toString()) }
                }
            }
        }
    }

    suspend fun syncAbout(retries: Int = 5) {
        coroutineScope {
            repeat(retries) {
                val result: String = client.get { url(address.toString()) }
            }
        }
    }
}
