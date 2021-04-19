package com.jmflaherty

import com.jmflaherty.client.ApiCalls
import kotlin.js.ExperimentalJsExport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalJsExport
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class ApplicationTest {
    private val mainThreadSurrogate = newSingleThreadContext("Test Thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        run {
            Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
            mainThreadSurrogate.close()
        }
    }

    @Test
    fun testAsync(): Unit = runBlocking {
        ApiCalls.asyncAbout(100)
    }

    @Test
    fun testSync(): Unit = runBlocking {
        ApiCalls.syncAbout(100)
    }
}
