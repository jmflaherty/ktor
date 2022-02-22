package com.jmflaherty

import com.epam.reportportal.junit5.ReportPortalExtension
import com.jmflaherty.client.ApiCalls
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExperimentalCoroutinesApi
@OptIn(DelicateCoroutinesApi::class)
@ExtendWith(ReportPortalExtension::class)
class ApplicationTestJUnit() {
    private val amount = 100

    @Test
    fun testSync(): Unit = runBlocking {
        repeat(amount) {
            ApiCalls.about().shouldHaveStatus(HttpStatusCode.OK)
        }
    }

    @Test
    fun testAsync(): Unit = runBlocking {
        repeat(amount) {
            async {
                ApiCalls.about().shouldHaveStatus(HttpStatusCode.OK)
            }.start()
        }
    }

    @Test
    fun testSyncFlow(): Unit = runBlocking {
        ApiCalls.aboutFlow(amount).collect {
            it.shouldHaveStatus(HttpStatusCode.OK)
        }
    }

    @Test
    fun testAsyncFlow(): Unit = runBlocking {
        ApiCalls.aboutAsyncFlow(amount).collect {
            it.shouldHaveStatus(HttpStatusCode.OK)
        }
    }
}
