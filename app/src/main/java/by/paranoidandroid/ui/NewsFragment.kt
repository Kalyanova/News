package by.paranoidandroid.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import by.paranoidandroid.R
import by.paranoidandroid.adapter.NewsAdapter
import by.paranoidandroid.databinding.FragmentNewsBinding
import by.paranoidandroid.observeNonNull

class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private var _binding: FragmentNewsBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.attributionLink2.setOnClickListener {
            val url = getString(R.string.attribution_link_url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        viewModel.response.observeNonNull(viewLifecycleOwner) { articles ->
            with(binding) {
                Log.d(TAG, "response: $articles")
                progressBar.isVisible = false
                val isNoDataFound = articles.isEmpty()
                binding.emptyStatePlaceholder.root.isVisible = isNoDataFound
                newsList.isVisible = !isNoDataFound
                if (!isNoDataFound) {
                    newsList.adapter = NewsAdapter(articles)
                }
            }
        }
        viewModel.isDataOutdated.observeNonNull(viewLifecycleOwner) { isOutdated ->
            val message = getString(
                if (isOutdated) R.string.data_are_outdated else R.string.data_are_actual
            )
            showToast(message)
        }
        viewModel.error.observeNonNull(viewLifecycleOwner) { message ->
            showToast(message)
        }
        val lastEntryTime = arguments?.getLong(LAST_ENTRY_TIME) ?: 0
        viewModel.getNews(lastEntryTime)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected")
        if (item.itemId == R.id.action_sync) {
            Log.d(TAG, "action_sync")
            binding.emptyStatePlaceholder.root.isVisible = false
            binding.newsList.isVisible = false
            binding.progressBar.isVisible = true
            viewModel.refresh()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = "NewsFragment"
        private const val LAST_ENTRY_TIME = "LAST_ENTRY_TIME"

        fun newInstance(lastEntryTime: Long) = NewsFragment().apply {
            arguments = bundleOf(LAST_ENTRY_TIME to lastEntryTime)
        }
    }
}
