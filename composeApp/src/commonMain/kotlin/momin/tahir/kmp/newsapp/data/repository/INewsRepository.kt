package momin.tahir.kmp.newsapp.data.repository

import momin.tahir.kmp.newsapp.data.remote.NewsDto

interface INewsRepository {
    suspend fun fetchAllNews(): NewsDto

}