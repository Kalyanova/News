package by.paranoidandroid.network

import by.paranoidandroid.model.NewsResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("sources") sources: String
    ): Deferred<NewsResponse>
}
