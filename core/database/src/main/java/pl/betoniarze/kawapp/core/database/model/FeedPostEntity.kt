package pl.betoniarze.kawapp.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.betoniarze.kawapp.core.model.FeedPost

/** Room row for a [FeedPost]. Lists are stored as JSON (see [Converters]). */
@Entity(tableName = "feed_posts")
data class FeedPostEntity(
    @PrimaryKey val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val content: String,
    val imageUrls: List<String>,
    var kavosCount: Int,
    var comments: List<String>
)

/** Maps the row to the domain [FeedPost]. */
fun FeedPostEntity.asExternalModel() = FeedPost(
    id = id,
    authorName = authorName,
    authorAvatarUrl = authorAvatarUrl,
    content = content,
    imageUrls = imageUrls,
    kavosCount = kavosCount,
    comments = comments
)