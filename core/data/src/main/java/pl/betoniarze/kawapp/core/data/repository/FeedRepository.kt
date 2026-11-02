package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.model.FeedPost

interface FeedRepository {
    fun getFeedPosts(): Flow<List<FeedPost>>
}