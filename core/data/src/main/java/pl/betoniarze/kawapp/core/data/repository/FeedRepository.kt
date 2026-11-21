package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.model.FeedPost

/** Source of community feed posts. */
interface FeedRepository {
    /** All feed posts. Emits again when the posts change. */
    fun getFeedPosts(): Flow<List<FeedPost>>
}