package com.test.server.server

import com.callmonitor.model.SavedLogRecordStatus
import com.callmonitor.model.CallStatus
import com.callmonitor.model.Root
import com.callmonitor.model.ServiceStatus
import com.callmonitor.model.SavedRecordStatus
import com.google.gson.Gson
import com.test.server.localdatabase.CallMonitorDb
import com.test.server.localdatabase.entities.CallRecordEntity
import com.test.server.localdatabase.entities.CallStatusEntity
import com.test.server.mapper.toCallRecordEntity
import com.test.server.mapper.toCallStatusEntity
import io.ktor.application.install
import io.ktor.application.call
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar
import javax.inject.Inject

class CallMonitorServer @Inject constructor(private val database: CallMonitorDb) {

    companion object {
        private const val PORT = 5001
        private const val STATUS = "/status"
        private const val STATUS_NAME = "status"
        private const val LOG = "/log"
        private const val LOG_NAME = "log"
        private const val ROOT_NAME = "/"
        private const val HTTP_TAG = "http://"
        private const val SERVICE_SAVED_RECORD = "/save_log_record"
        private const val SERVICE_SAVE_STATUS = "/save_status"
        private const val SAVED_STATUS_MESSAGE = "Status saved successfully :)"
        private const val SAVED_RECORD_MESSAGE = "Record saved successfully :)"
        private const val SAVED_ERROR_MESSAGE = "Something went wrong!!"
        private const val SAVED_SUCCESSFUL_CODE = 200
        private const val SAVED_ERROR_CODE = 405
        private const val DATE_FORMAT_YYYY_MM_DD_WITH_DASH = "yyyy-MM-dd'T'HH:mm:ssZ"
    }

    private lateinit var dateServerStart: String
    private lateinit var urlServer: String
    private val formatter = SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_WITH_DASH, Locale.US)

    private val server by lazy {
        embeddedServer(Netty, PORT, watchPaths = emptyList()) {
            install(WebSockets)
            install(CallLogging)
            install(ContentNegotiation) {
                gson()
            }
            routing {
                get(ROOT_NAME) {
                    val gson = Gson()
                    val data: String = gson.toJson(Root(dateServerStart,
                        listOf(ServiceStatus(STATUS_NAME, "$HTTP_TAG${getIpServer()}$STATUS:$PORT"),
                            ServiceStatus(LOG_NAME, "$HTTP_TAG${getIpServer()}$LOG:$PORT"))))
                    call.respond(data)
                }
                get(STATUS) {
                    val gson = Gson()
                    val data: String = gson.toJson(getCallStatus())
                    call.respond(data)
                }
                get(LOG) {
                    val gson = Gson()
                    val data: String = gson.toJson(getCallRecordList())
                    call.respond(data)
                }
                post(SERVICE_SAVE_STATUS) {
                    val data = call.receive<CallStatus>()
                    val idResult = insertCallStatus(data.toCallStatusEntity())
                    if (idResult > 0) call.respond(SavedRecordStatus(SAVED_SUCCESSFUL_CODE,
                        SAVED_STATUS_MESSAGE,
                        true))
                    else call.respond(SavedRecordStatus(SAVED_ERROR_CODE,
                        SAVED_ERROR_MESSAGE,
                        false))
                }
                post(SERVICE_SAVED_RECORD) {
                    val data = call.receive<SavedLogRecordStatus>()
                    val idResult =
                        insertCallRecord(data.callRecord.toCallRecordEntity())
                    if (idResult > 0) {
                        deleteCallStatus(data.callStatusToDelete.toCallStatusEntity())
                        call.respond(SavedRecordStatus(SAVED_SUCCESSFUL_CODE,
                            SAVED_RECORD_MESSAGE,
                            true))
                    } else call.respond(SavedRecordStatus(SAVED_ERROR_CODE,
                        SAVED_ERROR_MESSAGE,
                        false))
                }

            }
        }
    }

    private fun insertCallStatus(call: CallStatusEntity) =
        database.callStatusDao().insertCallStatus(call)

    private fun insertCallRecord(call: CallRecordEntity) =
        database.callRecordDao().insertCallRecord(call)

    private fun deleteCallStatus(call: CallStatusEntity) {
        database.callStatusDao().delete(call)
    }

    private fun getCallRecordList(): List<CallRecordEntity> {
        return database.callRecordDao().getAll()
    }

    private fun getCallStatus(uid: Int = 0): CallStatusEntity {
        return database.callStatusDao().getCallStatus(uid)
    }

    fun getIpServer(): String {
        return urlServer
    }

    fun startServer() {
        dateServerStart = formatter.format(Calendar.getInstance().time)
        urlServer = "${NetworkUtils.getLocalIpAddress()}:$PORT"
        CoroutineScope(Dispatchers.IO).launch {
            server.start(wait = true)
        }
    }

    fun stopServer() {
        dateServerStart = ""
        urlServer = ""
        server.stop(1_000, 2_000)
    }

}