package momin.tahir.kmp.newsapp.data.api

import momin.tahir.kmp.newsapp.data.remote.NewsDto

interface INewsApi {
    suspend fun fetchAllNews(): NewsDto
    suspend fun searchNews(query:String): NewsDto
}