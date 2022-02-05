package com.callmonitor.network

import com.callmonitor.network.adapter.NetworkResponseAdapterFactory
import com.callmonitor.network.service.CallMonitorApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TIME_OUT_DURATION = 20L

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT_DURATION, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT_DURATION, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT_DURATION, TimeUnit.SECONDS)
        return clientBuilder.build()
    }

    @Provides
    fun provideRetrofitBuilder(httpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.API_BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    fun provideCallMonitorApi(retrofit: Retrofit): CallMonitorApi =
        retrofit.create(CallMonitorApi::class.java)
}
