package by.paranoidandroid.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import by.paranoidandroid.BuildConfig
import by.paranoidandroid.db.NewsRoomDatabase
import by.paranoidandroid.model.Article
import by.paranoidandroid.network.ApiClient
import by.paranoidandroid.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableLiveData<List<Article>>()
    private val _error = MutableLiveData<String>()
    private val isDataOutdated = true // TODO: check if user wasn't in the app at least for 2 days

    private var dbArticles: LiveData<List<Article>>? = null

    // The ViewModel maintains a reference to the repository to get data
    private val repository: NewsRepository

    private var getTopHeadlinesJobFromNetwork = Job()
    private var getTopHeadlinesJobFromDB = Job()
    // We need the second job for database because after cancelling the job can't be reused
    private var getTopHeadlinesJobFromDB2 = Job()

    private val networkCoroutineScope = CoroutineScope(getTopHeadlinesJobFromNetwork + Dispatchers.Main)
    private val databaseCoroutineScope = CoroutineScope(getTopHeadlinesJobFromDB + Dispatchers.IO)
    private val databaseCoroutineScope2 = CoroutineScope(getTopHeadlinesJobFromDB2 + Dispatchers.IO)

    private val dbArticlesObserver = Observer<List<Article>?> { articles -> _response.postValue(articles) }

    val response: LiveData<List<Article>> get() = _response
    val error: LiveData<String> get() = _error

    init {
        Log.d(TAG, "init")
        val sourceDao = NewsRoomDatabase.getDatabase(application).sourceDao()
        val articleDao = NewsRoomDatabase.getDatabase(application).articleDao()
        repository = NewsRepository(sourceDao, articleDao)
        dbArticles?.observeForever(dbArticlesObserver)
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        getTopHeadlinesJobFromNetwork.cancel()
        getTopHeadlinesJobFromDB.cancel()
        super.onCleared()
    }

    fun getNews() {
        Log.d(TAG, "getNews")
        getArticlesFromDatabase()
        if (isDataOutdated) {
            // stop getting news from database
            getTopHeadlinesJobFromDB.cancel()
            // remove all news from database
            deleteAllArticlesFromDatabase()
            getArticlesFromNetwork()
        }
    }

    private fun getArticlesFromNetwork() {
        Log.d(TAG, "getArticlesFromNetwork")
        // We're still executing code on the main thread (because Retrofit does all its work on a background thread),
        // but now we're letting coroutines manage concurrency.
        networkCoroutineScope.launch {
            val getNewsDeferred = ApiClient.retrofitService.getTopHeadlines(BuildConfig.API_KEY, "google-news")
            try {
                val response = getNewsDeferred.await()
                Log.d(TAG, "Total results: ${response.totalResults}")
                val newArticles = response.articles
                if (!newArticles.isNullOrEmpty()) {
                    _response.value = newArticles
                    saveIntoDatabase(newArticles)
                }
            } catch (e: Exception) {
                _error.value = "Failure: something went wrong"
                Log.e(TAG, "Failure: ${e.message}")
            }
        }
    }

    private fun getArticlesFromDatabase() = databaseCoroutineScope.launch {
        Log.d(TAG, "getArticlesFromDatabase")
        dbArticles = repository.getAllArticles()
    }

    private fun saveIntoDatabase(articles: List<Article>) = databaseCoroutineScope2.launch {
        Log.d(TAG, "saveIntoDatabase")
        repository.insertAllArticles(articles)
    }

    private fun deleteAllArticlesFromDatabase() = databaseCoroutineScope2.launch {
        Log.d(TAG, "deleteAllArticlesFromDatabase")
        repository.deleteAllArticles()
    }

    private companion object {
        private const val TAG = "NewsViewModel"
    }
}
