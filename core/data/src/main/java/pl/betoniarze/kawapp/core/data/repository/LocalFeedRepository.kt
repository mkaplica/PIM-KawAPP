package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.data.BuildConfig
import pl.betoniarze.kawapp.core.data.fake.FakeCoffeeData
import pl.betoniarze.kawapp.core.database.dao.FeedPostDao
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.FeedPost

class LocalFeedRepository @Inject constructor(
    private val feedPostDao: FeedPostDao
) : FeedRepository {

    // Debug builds show the sample posts after the posts from the database.
    // Release builds never include them.
    private val samplePosts: List<FeedPost> =
        if (BuildConfig.DEBUG) FakeCoffeeData.feedPosts else emptyList()

    override fun getFeedPosts(): Flow<List<FeedPost>> =
        feedPostDao.getAllFeedPosts()
            .map { list -> list.map { it.asExternalModel() } + samplePosts }
}
