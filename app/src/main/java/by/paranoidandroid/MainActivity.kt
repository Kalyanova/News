package by.paranoidandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import by.paranoidandroid.databinding.ActivityMainBinding
import by.paranoidandroid.ui.NewSourceFragment
import by.paranoidandroid.ui.NewsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.replace(R.id.container, NewsFragment.newInstance())
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d(TAG, "handleOnBackPressed")
                when (supportFragmentManager.findFragmentById(R.id.container)) {
                    is NewSourceFragment -> {
                        binding.fabAddNewSource.isVisible = true
                        supportFragmentManager.replace(R.id.container, NewsFragment.newInstance())
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

    private companion object {
        private const val TAG = "MainActivity"
    }
}