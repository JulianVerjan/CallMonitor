package com.facetracking.data

import com.facetracking.data.datasource.FaceTrackingRemoteDataSource
import com.facetracking.data.repository.FaceTrackingRepository
import com.facetracking.data.usecase.SaveVideoUseCase
import com.facetracking.network.service.FaceTrackingService
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
    fun provideRemoteDataSource(faceTrackingService: FaceTrackingService): FaceTrackingRemoteDataSource {
        return FaceTrackingRemoteDataSource(faceTrackingService)
    }

    // / Provide repositories ///

    @Singleton
    @Provides
    fun provideFaceTrackingRepository(
        faceTrackingRemoteDataSource: FaceTrackingRemoteDataSource
    ): FaceTrackingRepository {
        return FaceTrackingRepository(faceTrackingRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideFaceTrackingUseCase(
        faceTrackingRepository: FaceTrackingRepository
    ): SaveVideoUseCase {
        return SaveVideoUseCase(faceTrackingRepository)
    }
}
