package com.callmonitor.task.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.telephony.TelephonyManager
import android.provider.ContactsContract
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.CallSuper
import com.callmonitor.data.usecase.SaveCallRecordUseCase
import com.callmonitor.data.usecase.SaveCallStatusUseCase
import com.callmonitor.model.CallRecord
import com.callmonitor.model.CallStatus
import com.callmonitor.network.model.mapper.RepositoryResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.provider.CallLog
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

// https://stackoverflow.com/questions/62335727/hilt-injection-not-working-with-broadcastreceiver
abstract class HiltBroadcastReceiver : BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {
        // Hilt does not work with Broadcast receiver in case parent class's onReceive is called
    }
}

@AndroidEntryPoint
open class CallReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var saveCallStatusUseCase: SaveCallStatusUseCase

    @Inject
    lateinit var saveCallRecordUseCase: SaveCallRecordUseCase

    private val formatter = SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_WITH_DASH, Locale.US)

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        val number = intent.extras?.getString(TelephonyManager.EXTRA_INCOMING_NUMBER) ?: ""
        val name = getContactName(number, context)
        val ongoing: Boolean

        when {
            intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                ongoing = true
                if (number != "") {
                    val callStatus = CallStatus(name = name, number = number, ongoing = ongoing)
                    saveCallStatus(context, callStatus)
                }
            }
            intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE -> {
                ongoing = false
                if (number != "") {
                    val callInfo = getCallDuration(context)
                    val duration = callInfo.split(" ").first()
                    val dateLong = callInfo.split(" ").last()
                    val millisecond: Long = dateLong.toLong()
                    val dateString: String = formatter.format(Date(millisecond))
                    val callStatus = CallStatus(name = name, number = number, ongoing = ongoing)
                    val callRecord = CallRecord(name = name,
                        number = number,
                        duration = duration, beginning = dateString)
                    saveCallRecord(context, callStatus, callRecord)
                }
            }
        }
    }

    private fun getCallDuration(context: Context): String {
        var callDuration = ""
        var callDate = ""
        val contactCursor: Cursor? = context.contentResolver.query(CallLog.Calls.CONTENT_URI,
            null,
            null,
            null,
            "${CallLog.Calls.DATE} DESC limit 1;")
        contactCursor?.let {
            val duration = contactCursor.getColumnIndex(CallLog.Calls.DURATION)
            val date: Int = contactCursor.getColumnIndex(CallLog.Calls.DATE)
            while (contactCursor.moveToNext()) {
                callDuration = contactCursor.getString(duration)
                callDate = contactCursor.getString(date)
            }
            contactCursor.close()
        }
        return "$callDuration $callDate"
    }

    @SuppressLint("Range")
    private fun getContactName(number: String, context: Context): String {
        if (number != "") {
            var contactName = "Unknown user"
            // // define the columns I want the query to return
            val projection = arrayOf(
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER,
                ContactsContract.PhoneLookup.HAS_PHONE_NUMBER)

            // encode the phone number and build the filter URI
            val contactUri: Uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number))

            // query time
            val cursor: Cursor? = context.contentResolver.query(contactUri,
                projection, null, null, null)
            // querying all contacts = Cursor cursor =
            // context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
            // projection, null, null, null);
            cursor?.let {
                if (it.moveToFirst()) {
                    contactName =
                        cursor.getString(it.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME))
                }
                it.close()
            }
            return if (contactName == "") number else contactName
        } else return ""
    }

    private fun saveCallRecord(
        context: Context,
        callStatusToDelete: CallStatus,
        callRecord: CallRecord,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val savedCallRecordResult =
                saveCallRecordUseCase.saveCallRecord(callStatusToDelete, callRecord)) {
                is RepositoryResult.Success -> {
                    val isCallSaved = savedCallRecordResult.result?.isRecordSaved ?: false
                    //if (isCallSaved)
                    //    showToast(context = context, savedCallRecordResult.result?.message)
                }
                is RepositoryResult.Fail<*> ->{} //showToast(context = context,
                    //savedCallRecordResult.errorResult.toString())
                else -> {}
            }
        }
    }

    private fun saveCallStatus(context: Context, callStatus: CallStatus) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val savedCallStatusResult = saveCallStatusUseCase.saveCallStatus(callStatus)) {
                is RepositoryResult.Success -> {
                    val isCallSaved = savedCallStatusResult.result?.isRecordSaved ?: false
                    //if (isCallSaved)
                    //    showToast(context = context, savedCallStatusResult.result?.message)
                }
                is RepositoryResult.Fail<*> -> {}//showToast(context = context,
                //savedCallStatusResult.errorResult.toString())
                else -> {}
            }
        }
    }

    private fun showToast(context: Context?, message: String?) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    companion object {
        private const val DATE_FORMAT_YYYY_MM_DD_WITH_DASH = "yyyy-MM-dd'T'HH:mm:ssZ"
    }

}