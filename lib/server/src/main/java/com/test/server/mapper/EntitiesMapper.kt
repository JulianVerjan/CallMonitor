package com.test.server.mapper

import com.callmonitor.model.CallRecord
import com.callmonitor.model.CallStatus
import com.test.server.localdatabase.entities.CallRecordEntity
import com.test.server.localdatabase.entities.CallStatusEntity

fun CallStatus.toCallStatusEntity() = CallStatusEntity(
    name = this.name,
    number = this.number,
    ongoing = this.ongoing
)

fun CallRecord.toCallRecordEntity() = CallRecordEntity(
    name = this.name,
    number = this.number,
    beginning = this.beginning,
    timeQueried = this.timeQueried,
    duration = this.duration
)
