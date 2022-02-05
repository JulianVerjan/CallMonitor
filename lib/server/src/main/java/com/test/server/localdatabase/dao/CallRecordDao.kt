package com.test.server.localdatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.test.server.localdatabase.entities.CallRecordEntity

@Dao
interface CallRecordDao {

    @Query("SELECT * FROM CallRecordEntity")
    fun getAll(): List<CallRecordEntity>

    @Insert
    fun insertCallRecord(callRecord: CallRecordEntity): Long
}