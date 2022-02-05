package com.callmonitor.data

import com.callmonitor.data.datasource.CallMonitorRemoteDataSource
import com.callmonitor.data.repository.CallMonitorRepository
import com.callmonitor.data.usecase.GetCallRecordListUseCase
import com.callmonitor.data.usecase.GetServicesInfoUseCase
import com.callmonitor.data.usecase.SaveCallRecordUseCase
import com.callmonitor.data.usecase.SaveCallStatusUseCase
import com.callmonitor.network.service.CallMonitorApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // / Provide Remote Data Sources ///

    @Provides
    fun provideRemoteDataSource(
        callMonitorApi: CallMonitorApi
    ): CallMonitorRemoteDataSource {
        return CallMonitorRemoteDataSource(callMonitorApi)
    }

    // / Provide repositories ///

    @Singleton
    @Provides
    fun provideCallMonitorRepository(
        callMonitorRemoteDataSource: CallMonitorRemoteDataSource
    ): CallMonitorRepository {
        return CallMonitorRepository(callMonitorRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideSavedCallRecordUseCase(
        callMonitorRepository: CallMonitorRepository
    ): SaveCallStatusUseCase {
        return SaveCallStatusUseCase(callMonitorRepository)
    }

    @Singleton
    @Provides
    fun provideGetServicesInfoUseCase(
        callMonitorRepository: CallMonitorRepository
    ): GetServicesInfoUseCase {
        return GetServicesInfoUseCase(callMonitorRepository)
    }

    @Singleton
    @Provides
    fun provideGetCallRecordListUseCase(
        callMonitorRepository: CallMonitorRepository
    ): GetCallRecordListUseCase {
        return GetCallRecordListUseCase(callMonitorRepository)
    }

    @Singleton
    @Provides
    fun provideStatusInfoUseCase(
        callMonitorRepository: CallMonitorRepository
    ): SaveCallRecordUseCase {
        return SaveCallRecordUseCase(callMonitorRepository)
    }
}
