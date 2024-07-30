package com.myapps.wtsacademy2.Utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URLConnection

object FileUploadHelper {
    fun copyUriToInternalPath(context: Context, uri: Uri): File? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null) ?: return null

        returnCursor.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            val displayName = cursor.getString(nameIndex)
            val outputFile = createStorageFile(context, displayName)

            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return null
                inputStream.use { input ->
                    val outputStream: OutputStream = FileOutputStream(outputFile)
                    val buf = ByteArray(512)
                    var len: Int
                    while (input.read(buf).also { len = it } > 0) {
                        outputStream.write(buf, 0, len)
                    }
                    outputStream.close()
                    return outputFile
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun createStorageFile(context: Context, fileName: String): File {
        val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES) ?: return File("")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }
        return File(storageDir, getUniqueFileName(storageDir, fileName))
    }

    private fun getUniqueFileName(directory: File, fileName: String): String {
        var baseName = fileName
        var counter = 1
        while (File(directory, baseName).exists()) {
            val extension = getFileExtension(fileName)
            baseName = if (extension.isNullOrEmpty()) {
                "$fileName-$counter"
            } else {
                val nameWithoutExtension = fileName.removeSuffix(".$extension")
                "$nameWithoutExtension-$counter.$extension"
            }
            counter++
        }
        return baseName
    }

    private fun getFileExtension(fileName: String): String? {
        val lastDotIndex = fileName.lastIndexOf('.')
        return if (lastDotIndex != -1) fileName.substring(lastDotIndex + 1) else null
    }

    fun File.createMultipartFile(partName: String): MultipartBody.Part {
        val fileBody = asRequestBody(getMimeType(this)?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, name, fileBody)
    }

    private fun getMimeType(file: File): String? {
        val fileNameMap = URLConnection.getFileNameMap()
        return fileNameMap.getContentTypeFor(file.name) ?: "image/*"
    }

    fun createPartFromString(data: String): RequestBody {
        return data.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}
