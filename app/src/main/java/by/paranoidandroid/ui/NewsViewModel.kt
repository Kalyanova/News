package by.paranoidandroid.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.paranoidandroid.BuildConfig
import by.paranoidandroid.model.Article
import by.paranoidandroid.model.NewsResponse
import by.paranoidandroid.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private val _response = MutableLiveData<List<Article>>()
    private val _error = MutableLiveData<String>()
    val response: LiveData<List<Article>> get() = _response
    val error: LiveData<String> get() = _error

    init {
        Log.d(TAG, "init")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    fun getNews() {
        Log.d(TAG, "getNews")
        ApiClient.retrofitService.getTopHeadlines(
            BuildConfig.API_KEY,
            "google-news"
        ).enqueue(
            object: Callback<NewsResponse> {

                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Response: ${response.body()}")
                        response.body()?.let {
                            Log.d(TAG, "Total results: ${it.totalResults}")
                            _response.value = it.articles
                        }

                    } else {
                        _error.value = "Failure: something went wrong"
                        Log.d(TAG, "Failure: " + response.code())
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Log.d(TAG, "Failure: " + t.message)
                    _error.value = "Failure: " + t.message
                }
            }
        )
    }

    private companion object {
        private const val TAG = "NewsViewModel"
    }
}
