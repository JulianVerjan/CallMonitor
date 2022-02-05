package com.test.server

import android.content.Context
import androidx.room.Room
import com.test.server.localdatabase.CallMonitorDb
import com.test.server.localdatabase.dao.CallRecordDao
import com.test.server.localdatabase.dao.CallStatusDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ServerModule {
    @Provides
    fun provideCallRecordDao(appDatabase: CallMonitorDb): CallRecordDao {
        return appDatabase.callRecordDao()
    }

    @Provides
    fun provideCallStatusDao(appDatabase: CallMonitorDb): CallStatusDao {
        return appDatabase.callStatusDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): CallMonitorDb {
        return Room.databaseBuilder(
            appContext,
            CallMonitorDb::class.java,
            "database-call-monitor"
        ).build()
    }
}
