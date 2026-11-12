package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.database.model.FeedPostEntity

@Dao
interface FeedPostDao {
    @Query("SELECT * FROM feed_posts")
    fun getAllFeedPosts(): Flow<List<FeedPostEntity>>

    @Query("SELECT * FROM feed_posts WHERE id = :id")
    fun getFeedPostById(id: String): Flow<FeedPostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedPost(feedPost: FeedPostEntity)

    @Delete
    suspend fun deleteFeedPost(id: String)
}