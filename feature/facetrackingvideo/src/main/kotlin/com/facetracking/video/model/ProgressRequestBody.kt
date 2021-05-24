package com.facetracking.video.model

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProgressRequestBody(val file: File, val listener: UploadCallbacks) : RequestBody() {

    private val path: String? = null
    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
        fun onError()
        fun onFinish()
    }

    override fun contentType(): MediaType? {
        return "video/mp4".toMediaTypeOrNull()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return file.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength: Long = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(file)
        var uploaded: Long = 0
        `in`.use { _ ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (`in`.read(buffer).also { read = it } != -1) {

                // update progress on UI thread
                handler.post(ProgressUpdater(uploaded, fileLength))
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) : Runnable {
        override fun run() {
            listener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }

}