package com.jmflaherty

import com.jmflaherty.client.ApiCalls
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.js.ExperimentalJsExport

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalJsExport
@ObsoleteCoroutinesApi
class ApplicationTest : AnnotationSpec() {
    private val mainThreadSurrogate = newSingleThreadContext("Test Thread")
    private val amount = 100

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        run {
            Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
            mainThreadSurrogate.close()
        }
    }

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
    fun testFlow(): Unit = runBlocking {
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
