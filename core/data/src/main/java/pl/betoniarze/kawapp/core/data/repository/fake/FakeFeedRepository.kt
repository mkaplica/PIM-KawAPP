package pl.betoniarze.kawapp.core.data.repository.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.betoniarze.kawapp.core.data.fake.FakeCoffeeData
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.model.FeedPost

class FakeFeedRepository : FeedRepository {
    private val postsFlow = MutableStateFlow(FakeCoffeeData.feedPosts)

    override fun getFeedPosts(): Flow<List<FeedPost>> = postsFlow.asStateFlow()
}
