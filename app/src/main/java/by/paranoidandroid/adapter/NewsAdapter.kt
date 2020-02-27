package by.paranoidandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.paranoidandroid.R
import by.paranoidandroid.databinding.NewsItemBinding
import by.paranoidandroid.model.Article
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class NewsAdapter(
    private val news: List<Article>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = NewsItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    class ViewHolder(
        private val viewBinding: NewsItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(article: Article) {
            viewBinding.apply {
                newsTitle.text = article.title
                newsDesc.text = article.description

                val options = RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.news_image_placeholder)
                    .error(R.drawable.news_image_placeholder)
                Glide.with(newsImage.context)
                    .load(article.urlToImage)
                    .apply(options)
                    .into(newsImage)
            }
        }
    }
}