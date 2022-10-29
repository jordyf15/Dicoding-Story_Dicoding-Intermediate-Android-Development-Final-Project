package com.jordyf15.storyapp.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.jordyf15.storyapp.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

object Utils {
    private const val yearTime = 29030400000.0
    private const val monthTime = 2419200000.0
    private const val weekTime = 86400000 * 7.0
    private const val dayTime = 86400000.0
    private const val hourTime = 3600000.0
    private const val minuteTime = 60000.0
    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    private val timestamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createdAtToNow(createdAtStr: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'", Locale.US)
        var date: String
        try {
            val createdAtTime = format.parse(createdAtStr)?.time
            val nowTime = Date().time
            val result = nowTime - createdAtTime!!
            date = if (result > yearTime) {
                "${floor(result / monthTime).toInt()} Years ago"
            } else if (result > monthTime) {
                "${floor(result / monthTime).toInt()} Months ago"
            } else if (result > weekTime) {
                "${floor(result / weekTime).toInt()} Weeks ago"
            } else if (result > dayTime) {
                "${floor(result / dayTime).toInt()} Days ago"
            } else if (result > hourTime) {
                "${floor(result / hourTime).toInt()} Hours ago"
            } else if (result > minuteTime) {
                "${floor(result / minuteTime).toInt()} Minutes ago"
            } else {
                "${floor(result / 1000.0).toInt()} Seconds ago"
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            date = ""
        }

        return date
    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timestamp, ".jpg", storageDir)
    }

    fun uriToFile(selectedImage: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImage) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun createdFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply {
                mkdirs()
            }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timestamp.jpg")
    }
}