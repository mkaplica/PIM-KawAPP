package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.database.model.FeedPostEntity

/** Access to the feed_posts table. */
@Dao
interface FeedPostDao {
    /** All feed posts stored on the device. Emits again whenever the table changes. */
    @Query("SELECT * FROM feed_posts")
    fun getAllFeedPosts(): Flow<List<FeedPostEntity>>

    /** The post with [id], or null if it does not exist. */
    @Query("SELECT * FROM feed_posts WHERE id = :id")
    fun getFeedPostById(id: String): Flow<FeedPostEntity?>

    /** Inserts [feedPost], replacing a post with the same id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedPost(feedPost: FeedPostEntity)

    /** Deletes the post with [id]. Does nothing if it does not exist. */
    @Query("DELETE FROM feed_posts WHERE id = :id")
    suspend fun deleteFeedPost(id: String)
}