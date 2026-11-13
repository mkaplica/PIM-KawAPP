package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.database.dao.FeedPostDao
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.FeedPost

class LocalFeedRepository @Inject constructor(
    private val feedPostDao: FeedPostDao
) : FeedRepository {
    override fun getFeedPosts(): Flow<List<FeedPost>> =
        feedPostDao.getAllFeedPosts()
            .map { list -> list.map { it.asExternalModel() } }
}