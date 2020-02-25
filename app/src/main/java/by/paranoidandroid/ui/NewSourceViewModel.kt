package by.paranoidandroid.ui

import android.util.Log
import androidx.lifecycle.ViewModel

class NewSourceViewModel : ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    fun addNewSource() {
        Log.d(TAG, "addNewSource")
    }

    private companion object {
        private const val TAG = "NewSourceViewModel"
    }
}