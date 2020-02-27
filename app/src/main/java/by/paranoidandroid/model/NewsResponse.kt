package by.paranoidandroid.model

data class NewsResponse(
    val totalResults: Int,
    val articles: List<Article>
)