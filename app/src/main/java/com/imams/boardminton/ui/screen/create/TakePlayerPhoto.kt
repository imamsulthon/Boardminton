
package com.imams.boardminton.ui.screen.create

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.imams.boardminton.R
import com.imams.boardminton.ui.theme.BoardMintonTheme
import com.imams.boardminton.ui.utils.allPermissionsGranted
import com.imams.boardminton.ui.utils.cameraPermissionLauncher
import com.imams.boardminton.ui.utils.dashedBorder
import com.imams.boardminton.ui.utils.launchPermissionCamera

@Composable
fun TakePhoto(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    fileName: String? = null,
    getNewImageUri: () -> Uri,
    onPhotoTaken: (Uri) -> Unit,
) {
    val context = LocalContext.current
    val hasPhoto = imageUri != null
    var newImageUri: Uri? = null

    var hasCamPermission by remember { mutableStateOf(allPermissionsGranted(context)) }
    val cameraPermissionLauncher = cameraPermissionLauncher(isPermitted = {hasCamPermission = it})
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            newImageUri?.let(onPhotoTaken)
        }
    }

    val visualMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { success ->
            success?.let(onPhotoTaken)
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { success ->
            success?.let(onPhotoTaken)
        }
    )

    Column(modifier) {
        Text(
            text = stringResource(R.string.label_take_selfie),
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .dashedBorder(1.dp, MaterialTheme.colorScheme.onBackground, 10.dp)
                .heightIn(min = 100.dp)
        ) {
            if (hasPhoto) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(96.dp)
                        .padding(5.dp)
                        .aspectRatio(4 / 3f)
                )
            } else {
                PhotoDefaultImage(modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth())
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ){
                val padding = 4.dp
                OutlinedButton(
                    modifier = Modifier.padding(end = padding),
                    onClick = {
                        if (hasCamPermission) {
                            newImageUri = getNewImageUri()
                            cameraLauncher.launch(newImageUri)
                        } else {
                            cameraPermissionLauncher.launchPermissionCamera()
                        }
                    }) { Text(text = stringResource(R.string.label_camera), style = MaterialTheme.typography.labelSmall) }
                OutlinedButton(
                    modifier = Modifier.padding(end = padding),
                    onClick = {
                        newImageUri = getNewImageUri()
                        galleryLauncher.launch("image/*")
                    }) { Text(text = stringResource(R.string.label_file), style = MaterialTheme.typography.labelSmall) }
                OutlinedButton(
                    modifier = Modifier.padding(end = padding),
                    onClick = {
                        newImageUri = getNewImageUri()
                        visualMediaLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }) { Text(text = stringResource(R.string.label_gallery), style = MaterialTheme.typography.labelSmall) }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 2.dp))
        AnimatedVisibility(visible = imageUri != null && !fileName.isNullOrEmpty()) {
            Text(text = "Uri: ${imageUri?.path}\nFileName: $fileName", style = MaterialTheme.typography.bodySmall)
        }
        Spacer(modifier = Modifier.padding(vertical = 3.dp))

    }
}

@Composable
private fun PhotoDefaultImage(
    modifier: Modifier = Modifier,
    lightTheme: Boolean = LocalContentColor.current.luminance() < 0.5f,
) {
    val assetId = if (lightTheme) R.drawable.ic_player_man_color else R.drawable.ic_player_woman_color
    Image(
        painter = painterResource(id = assetId),
        modifier = modifier,
        contentDescription = null,
        alignment = Alignment.Center
    )
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PhotoQuestionPreview() {
    BoardMintonTheme {
        Surface(modifier = Modifier.padding(6.dp)) {
            TakePhoto(
                imageUri = Uri.parse("https://example.bogus/wow"),
                getNewImageUri = { Uri.EMPTY },
                onPhotoTaken = { },
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

