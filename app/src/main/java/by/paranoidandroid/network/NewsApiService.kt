package by.paranoidandroid.network

import by.paranoidandroid.model.NewsResponse
import by.paranoidandroid.model.SourcesResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    fun getTopHeadlinesAsync(
        @Query("apiKey") apiKey: String,
        @Query("sources") sources: String
    ): Deferred<NewsResponse>

    @GET("v2/sources")
    fun getSourcesAsync(
        @Query("apiKey") apiKey: String,
        @Query("language") language: String
    ): Deferred<SourcesResponse>
}
