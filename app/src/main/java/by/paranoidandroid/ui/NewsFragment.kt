package by.paranoidandroid.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            binding.newsList.adapter = NewsAdapter(articles)
        }
        viewModel.getNews()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}
