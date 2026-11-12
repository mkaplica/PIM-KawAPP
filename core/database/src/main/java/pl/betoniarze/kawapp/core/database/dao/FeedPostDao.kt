package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.betoniarze.kawapp.core.database.model.FeedPostEntity

@Dao
interface FeedPostDao {
    @Query("SELECT * FROM feed_posts")
    suspend fun getAllFeedPosts(): List<FeedPostEntity>

    @Query("SELECT * FROM feed_posts WHERE id = :id")
    suspend fun getFeedPostById(id: String): FeedPostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedPost(feedPost: FeedPostEntity)

    @Delete
    suspend fun deleteFeedPost(id: String)
}