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
import by.paranoidandroid.db.NewsEntity

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

        context?.let {
            val fakeNewsEntity1 = NewsEntity(
                "Trump’s second day in India: Violence in Delhi and praise for Modi’s efforts on religious freedom",
                "At least 13 people were killed in Delhi on Monday and Tuesday when clashes broke out between Hindus and Muslims in the northeastern part of the city.",
                "https://www.washingtonpost.com/resizer/kVjDUstwHV6rqV_YwnHkuHopRlo=/1440x0/smart/arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/UEAHXUSX2YI6VDX5B6IEXXMAK4.jpg"
            )
            val fakeNewsEntity2 = NewsEntity(
                "Moderate and establishment Democrats struggle to challenge Sanders",
                "As Sen. Bernie Sanders emerges from his commanding victory in last weekend's Nevada caucuses, the Democratic establishment and the party's sizable moderate wing are increasingly anxious over his steady march to the presidential nomination -- yet they lack any…",
                "https://cdn.cnn.com/cnnnext/dam/assets/200224151311-03-bernie-sanders-voices-super-tease.jpg"
            )

            binding.newsList.adapter = NewsAdapter(it, listOf(
                fakeNewsEntity1, fakeNewsEntity2, fakeNewsEntity1, fakeNewsEntity2, fakeNewsEntity1, fakeNewsEntity2
            ))
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
