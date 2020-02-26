package by.paranoidandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.paranoidandroid.R
import by.paranoidandroid.databinding.NewsItemBinding
import by.paranoidandroid.db.NewsEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class NewsAdapter(
    private val context: Context,
    private val news: List<NewsEntity>
) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = NewsItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    class ViewHolder(
        private val viewBinding: NewsItemBinding
    ) : RecyclerView.ViewHolder(viewBinding.root) {

        fun bind(newsEntity: NewsEntity) {
            viewBinding.apply {
                newsTitle.text = newsEntity.title
                newsDesc.text = newsEntity.description

                val options = RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.news_image_placeholder)
                    .error(R.drawable.news_image_placeholder)
                Glide.with(newsImage.context)
                    .load(newsEntity.imageUrl)
                    .apply(options)
                    .into(newsImage)
            }
        }
    }
}