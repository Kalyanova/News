package by.paranoidandroid.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import by.paranoidandroid.R
import by.paranoidandroid.adapter.NewsAdapter
import by.paranoidandroid.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private var _binding: FragmentNewsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        binding.attributionLink.text =
            Html.fromHtml(getString(R.string.link_to_news_api), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.attributionLink.movementMethod = LinkMovementMethod.getInstance()
        context?.let {
            binding.newsList.adapter = NewsAdapter(it, listOf())
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
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
