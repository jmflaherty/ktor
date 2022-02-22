package com.jmflaherty

import com.epam.reportportal.junit5.ReportPortalExtension
import com.jmflaherty.client.ApiCalls
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.extension.ExtendWith


@ExperimentalCoroutinesApi
@OptIn(DelicateCoroutinesApi::class)
@ExtendWith(ReportPortalExtension::class)
class Kotest : AnnotationSpec() {
    private val amount = 10

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
