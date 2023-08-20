package com.imams.boardminton.ui.screen.create

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileDescriptor
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.reflect.InvocationTargetException


class PhotoUriManager(private val appContext: Context) {

    fun buildNewUri(): Uri {
        val photosDir = File(appContext.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename())
        val authority = "${appContext.packageName}.$FILE_PROVIDER"
        return FileProvider.getUriForFile(appContext, authority, photoFile)
    }

    fun buildNewUri(unique: Int): Uri {
        val photosDir = File(appContext.cacheDir, PHOTOS_DIR)
        photosDir.mkdirs()
        val photoFile = File(photosDir, generateFilename(unique))
        val authority = "${appContext.packageName}.$FILE_PROVIDER"
        return FileProvider.getUriForFile(appContext, authority, photoFile)
    }

    /**
     * Create a unique file name based on the time the photo is taken
     */
    private fun generateFilename() = "boardminton_player-${System.currentTimeMillis()}.jpg"
    private fun generateFilename(unique: Int) = "boardminton_player_$unique-${System.currentTimeMillis()}.jpg"

    companion object {
        private const val PHOTOS_DIR = "photos"
        private const val FILE_PROVIDER = "fileprovider"
    }

    suspend fun savePlayerImage(uri: Uri, name: String, callback: ((String) -> Unit)? = null) {
        saveMediaToStorage(getBitmapFromUri(uri), name, callback)
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val contentResolver = appContext.contentResolver
        val parcelFileDescriptor: ParcelFileDescriptor? = contentResolver!!.openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        var image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        image = ExifUtils.rotateBitmap(fileDescriptor, image)?: image
        parcelFileDescriptor.close()
        return image
    }

    private suspend fun saveMediaToStorage(bitmap: Bitmap, name: String, callback: ((String) -> Unit)? = null) {
        withContext(Dispatchers.IO) {
            val filename = "$name.jpg"
            var fos: OutputStream? = null
            var imageUri: Uri? = null
            val imgDirectory: String? = Environment.DIRECTORY_DCIM
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appContext.contentResolver?.also { resolver ->

                    val contentValues = ContentValues().apply {

                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            Environment.DIRECTORY_DCIM
                        )

                    }
                    val medUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    imageUri = resolver.insert(medUri, contentValues)

                    log("if imageUri $imageUri medUri $medUri")

                    fos = imageUri?.let { with(resolver) {
                        openOutputStream(it) }
                    }
                }
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(imgDirectory)
                val image = File(imagesDir, filename).also { fos = FileOutputStream(it) }
                imageUri = Uri.fromFile(image)
                log("else imageUri $imageUri")
                Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                    mediaScanIntent.data = imageUri
                    appContext.sendBroadcast(mediaScanIntent)
                }
            }

            fos?.use {
                val success = async(Dispatchers.IO) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it)
                }
                withContext(Dispatchers.Main) {
                    if (success.await()) {
                        appContext.showToast("Saved Successfully")
                        callback?.invoke(imageUri.toString())
                    } else {
                        appContext.showToast("Saved Failed")
                        callback?.invoke("")
                    }
                }

            }
        }
    }
    private fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun log(m: String) = println("PhotoUriManager $m")

}

object ExifUtils {

    fun rotateBitmap(fileDescriptor: FileDescriptor, bitmap: Bitmap): Bitmap? {
        try {
            val orientation = getExifOrientation(fileDescriptor)
            if (orientation == 1) {
                return bitmap
            }
            val matrix = Matrix()
            when (orientation) {
                2 -> matrix.setScale(-1f, 1f)
                3 -> matrix.setRotate(180f)
                4 -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }

                5 -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }

                6 -> matrix.setRotate(90f)
                7 -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }

                8 -> matrix.setRotate(-90f)
                else -> return bitmap
            }
            return try {
                val oriented =
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                oriented
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                bitmap
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    @Throws(IOException::class)
    private fun getExifOrientation(fileDescriptor: FileDescriptor): Int {
        var orientation: Int = ExifInterface.ORIENTATION_NORMAL
        try {
            val exifInterface = ExifInterface(fileDescriptor)
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, orientation)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return orientation
    }
}
