package com.callmonitor.network.model.mapper

import com.callmonitor.model.Root
import com.callmonitor.model.CallRecord
import com.callmonitor.model.SavedRecordStatus
import com.callmonitor.model.ServiceStatus
import com.callmonitor.network.model.reponse.GetServicesInfoNetworkResponse
import com.callmonitor.network.model.reponse.SaveCallStatusNetworkResponse
import com.callmonitor.network.model.reponse.ServiceStatusResponse
import com.callmonitor.network.model.reponse.CallRecordResponse

fun SaveCallStatusNetworkResponse.toSavedRecordModel() = SavedRecordStatus(
    status = this.status,
    message = this.message,
    isRecordSaved = this.isRecordSaved
)

fun GetServicesInfoNetworkResponse.toRootModel() = Root(
    start = this.start,
    services = this.services.toListServices()
)

private fun List<ServiceStatusResponse>.toListServices(): List<ServiceStatus> {
    val listResult = arrayListOf<ServiceStatus>()
    this.forEach {
        listResult.add(ServiceStatus(
            name = it.name,
            url = it.url
        ))
    }

    return listResult.toList()
}

fun List<CallRecordResponse>.toCallRecordList(): List<CallRecord> {
    val listResult = arrayListOf<CallRecord>()
    this.forEach {
        listResult.add(CallRecord(
            name = it.name,
            number = it.number,
            timeQueried = it.timeQueried,
            beginning = it.beginning,
            duration = it.duration
        ))
    }

    return listResult.toList()
}
