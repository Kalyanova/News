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
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fakeNewsEntity = NewsEntity(
            "Trump’s second day in India: Violence in Delhi and praise for Modi’s efforts on religious freedom",
            "At least 13 people were killed in Delhi on Monday and Tuesday when clashes broke out between Hindus and Muslims in the northeastern part of the city.",
            "https://www.washingtonpost.com/resizer/kVjDUstwHV6rqV_YwnHkuHopRlo=/1440x0/smart/arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/UEAHXUSX2YI6VDX5B6IEXXMAK4.jpg"
        )
        holder.bind(fakeNewsEntity)
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