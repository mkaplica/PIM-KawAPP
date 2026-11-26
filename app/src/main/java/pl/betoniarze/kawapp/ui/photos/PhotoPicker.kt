package pl.betoniarze.kawapp.ui.photos

import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import pl.betoniarze.kawapp.core.ui.R

/**
 * What a screen's ViewModel does with new photos. Built once per screen and handed to
 * [rememberPhotoPicker].
 *
 * @property newCameraUri Creates an empty file for the camera (see [PhotoStorage.newCameraUri]).
 * @property onPhotoTaken The camera saved a photo to the URI from [newCameraUri].
 * @property onCameraCancelled The camera was closed without a photo; the empty file can go.
 * @property onPhotosPicked Images chosen in the Photo Picker (temporary URIs, import them).
 */
class PhotoCallbacks(
    val newCameraUri: () -> Uri,
    val onPhotoTaken: (Uri) -> Unit,
    val onCameraCancelled: (Uri) -> Unit,
    val onPhotosPicked: (List<Uri>) -> Unit,
) {
    companion object {
        /** No-op callbacks for previews. */
        val None = PhotoCallbacks({ Uri.EMPTY }, {}, {}, {})
    }
}

/** Opens the system camera or the Photo Picker. Get one from [rememberPhotoPicker]. */
@Stable
class PhotoPicker internal constructor(
    private val camera: () -> Unit,
    private val gallery: () -> Unit,
) {
    /** System camera app; the photo goes straight into app storage. */
    fun openCamera() = camera()

    /** System Photo Picker, images only. */
    fun openGallery() = gallery()
}

/**
 * Camera and gallery without any runtime permission:
 * - the gallery is the system Photo Picker (no READ_MEDIA_IMAGES needed),
 * - the camera is the system camera app via TakePicture (works because the app does NOT declare
 *   the CAMERA permission; declaring it would make this intent require it).
 *
 * The pending camera URI is kept with rememberSaveable, so a photo taken after the system killed
 * the app in the background still arrives.
 *
 * @param maxItems How many more photos may be added; 1 uses the single-image picker.
 * @param onNoCameraApp Called on devices without a camera app.
 */
@Composable
fun rememberPhotoPicker(
    maxItems: Int,
    callbacks: PhotoCallbacks,
    onNoCameraApp: () -> Unit,
): PhotoPicker {
    val currentCallbacks by rememberUpdatedState(callbacks)
    val currentMaxItems by rememberUpdatedState(maxItems)
    val currentOnNoCameraApp by rememberUpdatedState(onNoCameraApp)
    var pendingCameraUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        val uri = pendingCameraUri ?: return@rememberLauncherForActivityResult
        pendingCameraUri = null
        if (saved) currentCallbacks.onPhotoTaken(uri) else currentCallbacks.onCameraCancelled(uri)
    }

    val pickOne = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) currentCallbacks.onPhotosPicked(listOf(uri))
    }

    // PickMultipleVisualMedia requires at least 2; a single slot uses pickOne instead.
    val multipleContract = remember(maxItems) {
        ActivityResultContracts.PickMultipleVisualMedia(maxItems.coerceAtLeast(2))
    }
    val pickMany = rememberLauncherForActivityResult(multipleContract) { uris ->
        if (uris.isNotEmpty()) currentCallbacks.onPhotosPicked(uris.take(currentMaxItems))
    }

    return remember(takePicture, pickOne, pickMany) {
        PhotoPicker(
            camera = {
                val uri = currentCallbacks.newCameraUri()
                pendingCameraUri = uri
                try {
                    takePicture.launch(uri)
                } catch (e: ActivityNotFoundException) {
                    pendingCameraUri = null
                    currentCallbacks.onCameraCancelled(uri)
                    currentOnNoCameraApp()
                }
            },
            gallery = {
                val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                when {
                    currentMaxItems <= 0 -> Unit
                    currentMaxItems == 1 -> pickOne.launch(request)
                    else -> pickMany.launch(request)
                }
            },
        )
    }
}

/**
 * Bottom sheet asking where the photo comes from. Picking an option dismisses the sheet first.
 *
 * @param onRemove Shown as a third option when set (e.g. removing the profile photo).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoSourceSheet(
    title: String,
    onCamera: () -> Unit,
    onGallery: () -> Unit,
    onDismiss: () -> Unit,
    onRemove: (() -> Unit)? = null,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 8.dp),
            )
            SheetOption(
                icon = Icons.Outlined.PhotoCamera,
                text = stringResource(R.string.photo_source_camera),
                onClick = {
                    onDismiss()
                    onCamera()
                },
            )
            SheetOption(
                icon = Icons.Outlined.PhotoLibrary,
                text = stringResource(R.string.photo_source_gallery),
                onClick = {
                    onDismiss()
                    onGallery()
                },
            )
            if (onRemove != null) {
                SheetOption(
                    icon = Icons.Outlined.Delete,
                    text = stringResource(R.string.remove_photo),
                    color = MaterialTheme.colorScheme.error,
                    onClick = {
                        onDismiss()
                        onRemove()
                    },
                )
            }
        }
    }
}

@Composable
private fun SheetOption(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface,
) {
    ListItem(
        headlineContent = { Text(text = text, color = color) },
        leadingContent = { Icon(icon, contentDescription = null, tint = color) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
    )
}
