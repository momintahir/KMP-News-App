package momin.tahir.kmp.newsapp.data.repository

import momin.tahir.kmp.newsapp.data.api.INewsApi

class NewsRepositoryImp(private val newsApi: INewsApi):INewsRepository {
    override suspend fun fetchAllNews()= newsApi.fetchAllNews()
}