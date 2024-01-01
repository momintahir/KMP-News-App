package momin.tahir.kmp.newsapp.data.repository

import kotlinx.coroutines.flow.Flow
import momin.tahir.kmp.newsapp.data.api.INewsApi
import momin.tahir.kmp.newsapp.domain.model.Article

class NewsRepositoryImp(private val newsApi: INewsApi,
    private val cachedData: ICacheData
):INewsRepository {
    override suspend fun fetchAllNews()= newsApi.fetchAllNews()
    override suspend fun saveArticle(article: Article) {
        cachedData.saveArticle(article)
    }

    override suspend fun getSavedArticles()= cachedData.getSavedNewsList()

}