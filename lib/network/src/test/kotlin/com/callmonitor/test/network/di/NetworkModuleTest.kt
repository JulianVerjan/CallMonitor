package com.callmonitor.test.network.di

import com.callmonitor.network.NetworkModule
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class NetworkModuleTest {

    private lateinit var networkModule: NetworkModule

    @Before
    fun setUp() {
        networkModule = NetworkModule
    }

    @Test
    fun verifyProvidedHttpClient() {
        val httpClient = networkModule.provideHttpClient()
        assertEquals(20000, httpClient.connectTimeoutMillis)
    }

    @Test
    fun verifyProvidedRetrofitBuilder() {
        val retrofit = networkModule.provideRetrofitBuilder(mockk())
        assertEquals(
            "http://192.168.0.115:5001",
            retrofit.baseUrl().toString()
        )
    }
}
