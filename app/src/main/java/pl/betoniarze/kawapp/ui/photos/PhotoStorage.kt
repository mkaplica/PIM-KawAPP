package pl.betoniarze.kawapp.ui.photos

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Photos kept in the app's private storage (filesDir/photos). Needs no permission.
 *
 * Photos are stored in Room / preferences as `file://` URI strings, which Coil loads directly.
 * The camera app gets a `content://` URI from [FileProvider] instead, because it runs in another
 * process; [storedUri] turns it back into the `file://` form.
 */
@Singleton
class PhotoStorage @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dir: File
        get() = File(context.filesDir, DIR_NAME).apply { mkdirs() }

    private val authority: String
        get() = "${context.packageName}.fileprovider"

    /** Creates an empty file for the camera app to write into and returns its content:// URI. */
    fun newCameraUri(): Uri = FileProvider.getUriForFile(context, authority, File(dir, newFileName()))

    /** file:// form of a photo in app storage, e.g. after the camera wrote to [newCameraUri]. */
    fun storedUri(uri: Uri): String =
        File(dir, requireNotNull(uri.lastPathSegment)).toUri().toString()

    /**
     * Copies an image chosen in the Photo Picker into app storage. The picker only grants
     * temporary access to its URI, so the copy is what gets saved. Returns null if reading fails.
     */
    suspend fun importFromPicker(source: Uri): String? = withContext(Dispatchers.IO) {
        runCatching {
            val target = File(dir, newFileName())
            val input = context.contentResolver.openInputStream(source) ?: return@runCatching null
            input.use { stream -> target.outputStream().use { stream.copyTo(it) } }
            target.toUri().toString()
        }.getOrNull()
    }

    /**
     * Deletes a photo from app storage. Accepts both the file:// and content:// form; only the
     * file name is used, so nothing outside the photos folder can be touched.
     */
    fun delete(uri: String) {
        val name = Uri.parse(uri).lastPathSegment ?: return
        File(dir, name).delete()
    }

    private fun newFileName() = "${UUID.randomUUID()}.jpg"

    private companion object {
        /** Must match the path in res/xml/file_paths.xml. */
        const val DIR_NAME = "photos"
    }
}
