package by.paranoidandroid.repository

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

    fun getAllSources(): List<Source> {
        return sourceDao.getSources()
    }

    fun getAllArticles(): List<Article> {
        return articleDao.getArticles()
    }

    suspend fun insertAllSources(sources: List<Source>) {
        sourceDao.insertAll(sources)
    }

    suspend fun insertAllArticles(articles: List<Article>) {
        articleDao.insertAll(articles)
    }

    suspend fun deleteAllArticles() {
        articleDao.deleteAll()
    }

    suspend fun deleteAllSources() {
        sourceDao.deleteAll()
    }

    suspend fun updateSource(source: Source) {
        sourceDao.update(source)
    }
}