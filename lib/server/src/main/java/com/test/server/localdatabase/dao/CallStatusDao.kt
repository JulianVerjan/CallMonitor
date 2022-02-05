package com.test.server.localdatabase.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete

import com.test.server.localdatabase.entities.CallStatusEntity

@Dao
interface CallStatusDao {

    @Query("SELECT * FROM CallStatusEntity  WHERE uid=:uid")
    fun getCallStatus(uid: Int): CallStatusEntity

    @Insert
    fun insertCallStatus(callStatus: CallStatusEntity): Long

    @Delete
    fun delete(callStatus: CallStatusEntity)
}