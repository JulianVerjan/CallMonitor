package com.test.server.localdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.server.localdatabase.dao.CallRecordDao
import com.test.server.localdatabase.dao.CallStatusDao
import com.test.server.localdatabase.entities.CallRecordEntity
import com.test.server.localdatabase.entities.CallStatusEntity

@Database(entities = [CallRecordEntity::class, CallStatusEntity::class], version = 1)
abstract class CallMonitorDb : RoomDatabase() {
    abstract fun callRecordDao(): CallRecordDao
    abstract fun callStatusDao(): CallStatusDao
}