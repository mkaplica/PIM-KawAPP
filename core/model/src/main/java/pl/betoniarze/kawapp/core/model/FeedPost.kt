package pl.betoniarze.kawapp.core.model

/**
 * A post in the community feed.
 *
 * @property kavosCount Number of "Kawos" (likes) the post has received.
 * @property comments Comment texts, oldest first.
 */
data class FeedPost(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val content: String,
    val imageUrls: List<String>,
    var kavosCount: Int,
    var comments: List<String>,
)
