package by.paranoidandroid.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.paranoidandroid.BuildConfig
import by.paranoidandroid.model.Article
import by.paranoidandroid.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val _response = MutableLiveData<List<Article>>()
    private val _error = MutableLiveData<String>()
    private var getTopHeadlinesJob = Job()

    private val coroutineScope = CoroutineScope(getTopHeadlinesJob + Dispatchers.Main)
    val response: LiveData<List<Article>> get() = _response
    val error: LiveData<String> get() = _error

    init {
        Log.d(TAG, "init")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        getTopHeadlinesJob.cancel()
        super.onCleared()
    }

    fun getNews() {
        Log.d(TAG, "getNews")
        // We're still executing code on the main thread (because Retrofit does all its work on a background thread),
        // but now we're letting coroutines manage concurrency.
        coroutineScope.launch {
            val getNewsDeferred = ApiClient.retrofitService.getTopHeadlines(BuildConfig.API_KEY, "google-news")
            try {
                val response = getNewsDeferred.await()
                Log.d(TAG, "Total results: ${response.totalResults}")
                _response.value = response.articles
            } catch (e: Exception) {
                _error.value = "Failure: something went wrong"
                Log.e(TAG, "Failure: ${e.message}")
            }
        }
    }

    private companion object {
        private const val TAG = "NewsViewModel"
    }
}
