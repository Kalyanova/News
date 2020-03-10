package by.paranoidandroid

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import by.paranoidandroid.databinding.ActivityMainBinding
import by.paranoidandroid.ui.NewSourceFragment
import by.paranoidandroid.ui.NewsFragment
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.replace(R.id.container, NewsFragment.newInstance(getLastEntryTime()))
            saveEntryTime()
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed")
                when (supportFragmentManager.findFragmentById(R.id.container)) {
                    is NewSourceFragment -> {
                        binding.fabAddNewSource.isVisible = true
                        supportFragmentManager.replace(R.id.container, NewsFragment.newInstance(getLastEntryTime()))
                        supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    }
                    is NewsFragment -> finish()
                }
            }
        })

        binding.fabAddNewSource.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.container) !is NewSourceFragment) {
                supportFragmentManager.replace(R.id.container, NewSourceFragment.newInstance())
                it.isVisible = false
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected")
        if (item.itemId == android.R.id.home) {
            Log.d(TAG, "android.R.id.home")
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getLastEntryTime(): Long {
        return try {
            val sharedPreferences = getPreferences(MODE_PRIVATE)
            sharedPreferences.getLong(getString(R.string.entry_time), 0)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get the last entry time: $e")
            0
        }
    }

    private fun saveEntryTime() {
        try {
            val sharedPreferences = getPreferences(MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                val newEntryTime = Calendar.getInstance().timeInMillis
                putLong(getString(R.string.entry_time), newEntryTime)
                commit()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save the last entry time: $e")
        }
    }

    private companion object {
        private const val TAG = "MainActivity"
    }
}