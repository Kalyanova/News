package by.paranoidandroid.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.paranoidandroid.model.Article

@Dao
interface ArticleDao {

    @Query("SELECT * from article ORDER BY time DESC")
    fun getArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(article: Article)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(articles: List<Article>)

    @Query("DELETE FROM article")
    suspend fun deleteAll()
}