package by.paranoidandroid.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import by.paranoidandroid.BuildConfig
import by.paranoidandroid.db.NewsRoomDatabase
import by.paranoidandroid.model.Source
import by.paranoidandroid.network.ApiClient
import by.paranoidandroid.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewSourceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NewsRepository
    private val _response = MutableLiveData<List<Source>>()
    private val _error = MutableLiveData<String>()

    val response: LiveData<List<Source>> get() = _response
    val error: LiveData<String> get() = _error

    init {
        Log.d(TAG, "init")
        val sourceDao = NewsRoomDatabase.getDatabase(application).sourceDao()
        val articleDao = NewsRoomDatabase.getDatabase(application).articleDao()
        repository = NewsRepository(sourceDao, articleDao)
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    /**
     * If the app is launched the first time, data are downloaded from network.
     * Otherwise, it's downloaded from database.
     */
    fun displaySources() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "displaySources")
                val sources = getSourcesFromDatabase()
                if (sources.isEmpty()) {
                    getSourcesFromNetwork()
                } else {
                    _response.postValue(sources)
                }
            }
        }
    }

    fun updateSource(source: Source, isChecked: Boolean) {
        source.enabled = isChecked
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "updateSource")
                repository.updateSource(source)
            }
        }
    }

    private fun getSourcesFromNetwork() {
        Log.d(TAG, "getSourcesFromNetwork")
        // We're still executing code on the main thread (because Retrofit does all its work on a background thread),
        // but now we're letting coroutines manage concurrency.
        viewModelScope.launch {
            val getSourcesDeferred = ApiClient.retrofitService.getSourcesAsync(BuildConfig.API_KEY, LANGUAGE_EN)
            try {
                val response: List<Source> = getSourcesDeferred.await().sources
                if (!response.isNullOrEmpty()) {
                    Log.d(TAG, "Total results: ${response.size}")
                    _response.value = response
                    saveIntoDatabase(response)
                } else {
                    Log.d(TAG, "No data.")
                }
            } catch (e: Exception) {
                _error.value = "Failure: something went wrong"
                Log.e(TAG, "Failure: ${e.message}")
            }
        }
    }

    private fun getSourcesFromDatabase(): List<Source> {
        Log.d(TAG, "getSourcesFromDatabase")
        val sources = repository.getAllSources()
        Log.d(TAG,"Data from database are received. Size is ${sources.size}")
        return sources
    }

    private fun saveIntoDatabase(sources: List<Source>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "saveIntoDatabase")
                repository.insertAllSources(sources)
            }
        }
    }

    private companion object {
        private const val TAG = "NewSourceViewModel"
        private const val LANGUAGE_EN = "en"
    }
}