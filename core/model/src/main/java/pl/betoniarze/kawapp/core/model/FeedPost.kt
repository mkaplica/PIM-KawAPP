package pl.betoniarze.kawapp.core.model

data class FeedPost(
    val id: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val content: String,
    val imageUrls: List<String>
)
