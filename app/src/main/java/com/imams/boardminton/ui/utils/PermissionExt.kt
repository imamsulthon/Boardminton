package com.imams.boardminton.ui.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat

val REQUIRED_PERMISSIONS_Camera = mutableListOf(android.Manifest.permission.CAMERA).apply {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}.toTypedArray()

val REQUIRED_PERMISSIONS_Contact = mutableListOf(android.Manifest.permission.READ_CONTACTS).apply {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
        add(android.Manifest.permission.READ_CONTACTS)
    }
}.toTypedArray()

fun Context.hasPermissionContacts() = REQUIRED_PERMISSIONS_Contact.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

fun allPermissionsGranted(ctx: Context) = REQUIRED_PERMISSIONS_Camera.all {
    ContextCompat.checkSelfPermission(ctx, it) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun contactPermissionLauncher(isPermitted: (Boolean) -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions(),
    onResult = { isPermitted.invoke(it.size == 1) }
)

@Composable
fun cameraPermissionLauncher(isPermitted: (Boolean) -> Unit) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions(),
    onResult = { granted -> isPermitted.invoke(granted.size == 2) }
)

val PERMISSION_CONTACT = arrayOf(android.Manifest.permission.READ_CONTACTS)
val PERMISSION_CAMERA = arrayOf(
    android.Manifest.permission.CAMERA,
    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
)

fun ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
        .launchContactPermission() = this.launch(PERMISSION_CONTACT)

fun ManagedActivityResultLauncher<Array<String>, Map<String, @JvmSuppressWildcards Boolean>>
        .launchPermissionCamera() = this.launch(PERMISSION_CAMERA)