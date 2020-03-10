package by.paranoidandroid.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import by.paranoidandroid.BuildConfig
import by.paranoidandroid.db.NewsRoomDatabase
import by.paranoidandroid.model.Article
import by.paranoidandroid.network.ApiClient
import by.paranoidandroid.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableLiveData<List<Article>>()
    private val _error = MutableLiveData<String>()
    private val _isDataOutdated = MutableLiveData<Boolean>()
    private val _sources = MutableLiveData<String>()
    // The ViewModel maintains a reference to the repository to get data
    private val repository: NewsRepository
    private val sourcesObserver = Observer<String> {
        getArticlesFromNetwork(it)
    }

    private var getTopHeadlinesJobFromDB: Job = Job()

    val response: LiveData<List<Article>> get() = _response
    val error: LiveData<String> get() = _error
    val isDataOutdated: LiveData<Boolean> get() = _isDataOutdated
    val sources: LiveData<String> get() = _sources

    init {
        Log.d(TAG, "init")
        val sourceDao = NewsRoomDatabase.getDatabase(application).sourceDao()
        val articleDao = NewsRoomDatabase.getDatabase(application).articleDao()
        repository = NewsRepository(sourceDao, articleDao)
        _sources.observeForever(sourcesObserver)
    }

    override fun onCleared() {
        _sources.removeObserver(sourcesObserver)
        super.onCleared()
    }

    fun getNews(lastEntryTime: Long) {
        Log.d(TAG, "getNews")
        // Gets old news from database cache (in another process).
        getArticlesFromDatabase()
        val shouldBeDataRefreshed = isDataOutdated(lastEntryTime)
        _isDataOutdated.value = shouldBeDataRefreshed
        if (shouldBeDataRefreshed) {
            refresh()
        }
    }

    fun refresh() {
        // Stops getting news from database.
        Log.d(TAG,"Cancelling loading data from database and delete old data.")
        getTopHeadlinesJobFromDB.cancel()
        // remove all news from database
        deleteAllArticlesFromDatabase()
        getSources()
    }

    private fun getSources() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "getSourcesFromDatabase")
                val sources = repository.getAllSources().filter { it.enabled }
                val result = if (sources.isEmpty()) {
                    NONE_SOURCE
                } else {
                    sources.joinToString(separator = ",") { it.id }
                }
                Log.d(TAG,"Sources: $result")
                _sources.postValue(result)
            }
        }
    }

    private fun getArticlesFromNetwork(sources: String) {
        Log.d(TAG, "getArticlesFromNetwork")
        // We're still executing code on the main thread (because Retrofit does all its work on a background thread),
        // but now we're letting coroutines manage concurrency.
        viewModelScope.launch {
            val getNewsDeferred = ApiClient.retrofitService.getTopHeadlinesAsync(
                BuildConfig.API_KEY,
                sources
            )
            try {
                val response = getNewsDeferred.await()
                Log.d(TAG, "Total results: ${response.totalResults}")
                val newArticles = response.articles
                _response.value = newArticles
                if (!newArticles.isNullOrEmpty()) {
                    saveIntoDatabase(newArticles)
                }
            } catch (e: Exception) {
                _error.value = "Failure: something went wrong"
                Log.e(TAG, "Failure: ${e.message}")
            }
        }
    }

    /**
     * Checks whether last entry occurred more than 2 minutes ago (if uncomment - two days ago).
     * In other words, checks if the user wasn't in the app at least for 2 minutes (or 2 days).
     * Note: if the app is launched the first time, result is true.
     */
    private fun isDataOutdated(lastEntryTime: Long): Boolean {
        try {
            if (lastEntryTime == 0L) return true
            val nowTime = Calendar.getInstance().timeInMillis
            val diffInMillis = nowTime - lastEntryTime
            // val daysSinceLastEntry = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
            // return daysSinceLastEntry > 2
            val minutesSinceLastEntry = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS)
            return minutesSinceLastEntry > 2
        } catch (e: Exception) {
            Log.e(TAG, "Failed to check when last entry occurred: $e")
            return true
        }
    }

    private fun getArticlesFromDatabase() {
        getTopHeadlinesJobFromDB = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "getArticlesFromDatabase")
                val articles = repository.getAllArticles()
                Log.d(TAG,"Data from database are received. Size is ${articles.size}")
                _response.postValue(articles)
            }
        }
    }

    private fun saveIntoDatabase(articles: List<Article>) {
        getTopHeadlinesJobFromDB = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "saveIntoDatabase")
                repository.insertAllArticles(articles)
            }
        }
    }

    private fun deleteAllArticlesFromDatabase() {
        getTopHeadlinesJobFromDB = viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "deleteAllArticlesFromDatabase")
                repository.deleteAllArticles()
            }
        }
    }

    private companion object {
        private const val TAG = "NewsViewModel"
        private const val NONE_SOURCE = "none"
    }
}
