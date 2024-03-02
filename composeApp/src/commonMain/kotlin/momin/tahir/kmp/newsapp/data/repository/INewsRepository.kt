package momin.tahir.kmp.newsapp.data.repository

import kotlinx.coroutines.flow.Flow
import momin.tahir.kmp.newsapp.data.remote.NewsDto
import momin.tahir.kmp.newsapp.domain.model.Article

interface INewsRepository {
    suspend fun fetchAllNews(): NewsDto
    suspend fun searchNews(query:String): NewsDto

    suspend fun saveArticle(article: Article)
    suspend fun deleteArticle(article: Article)
    suspend fun getSavedArticles():List<Article>

}