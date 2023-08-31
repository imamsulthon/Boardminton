package com.imams.boardminton.ui.utils

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.ContactsContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver

@Composable
fun <LO : LifecycleObserver> LO.ObserveLifecycle(lifecycle: Lifecycle) {
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(this@ObserveLifecycle)
        onDispose {
            lifecycle.removeObserver(this@ObserveLifecycle)
        }
    }
}

fun vibrateClick(context: Context?, milliseconds: Long = 100) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val vibrationEffect1: VibrationEffect = VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.cancel()
    vibrator.vibrate(vibrationEffect1)
}

fun Uri.pickContact(context: Context, callback: (String, String) -> Unit) {
    val contentResolver: ContentResolver = context.contentResolver
    var name = ""
    var number = ""
    val cursor: Cursor = contentResolver.query(this, null, null, null, null)
        ?: return callback.invoke(name, number)
    if (cursor.count >= 0) {
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            val phones: Cursor? = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null
            )
            if (phones != null) {
                while (phones.moveToNext()) {
                    number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                }
                phones.close()
            }
        }
    }
    callback.invoke(name, number)
}

fun Context.sendWhatsappMessage(number: String, message: String = "From BoardMinton") {
    this.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(
                java.lang.String.format(
                    "https://api.whatsapp.com/send?phone=%s&text=%s",
                    number,
                    message,
                )
            )
        )
    )
}