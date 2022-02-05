package com.test.server.localdatabase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CallRecordEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "number") val number: String,
    @ColumnInfo(name = "timeQueried") val timeQueried: Int,
    @ColumnInfo(name = "beginning") val beginning: String,
    @ColumnInfo(name = "duration") val duration: String,
)