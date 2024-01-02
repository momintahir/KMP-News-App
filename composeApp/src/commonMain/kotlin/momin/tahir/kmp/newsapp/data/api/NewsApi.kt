package momin.tahir.kmp.newsapp.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import momin.tahir.kmp.newsapp.data.remote.NewsDto

class NewsApi(private val baseUrl: String,
              private val httpClient: HttpClient):INewsApi {
    override suspend fun fetchAllNews():NewsDto = httpClient.get("$baseUrl/everything?q=bitcoin&apiKey=a8ddfed7b99c4f2483649d694b79b0fb").body()

}