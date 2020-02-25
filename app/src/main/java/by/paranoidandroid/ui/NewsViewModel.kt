package by.paranoidandroid.ui

import android.util.Log
import androidx.lifecycle.ViewModel

class NewsViewModel : ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    fun getNews() {
        Log.d(TAG, "getNews")
    }

    private companion object {
        private const val TAG = "NewsViewModel"
    }
}
