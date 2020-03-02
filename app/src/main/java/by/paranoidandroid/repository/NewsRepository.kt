package by.paranoidandroid.repository

import androidx.lifecycle.LiveData
import by.paranoidandroid.db.ArticleDao
import by.paranoidandroid.db.SourceDao
import by.paranoidandroid.model.Article
import by.paranoidandroid.model.Source

/**
 * Implements the logic for deciding whether to fetch data from a network or use results cached in a local database.
 */
class NewsRepository(
    private val sourceDao: SourceDao,
    private val articleDao: ArticleDao
) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allSources: LiveData<List<Source>> = sourceDao.getSources()

    suspend fun getAllArticles(): LiveData<List<Article>> {
        return articleDao.getArticles()
    }

    suspend fun updateSource(source: Source) {
        sourceDao.update(source)
    }

    suspend fun insertAllArticles(articles: List<Article>) {
        articleDao.insertAll(articles)
    }

    suspend fun deleteAllArticles() {
        articleDao.deleteAll()
    }
}