package momin.tahir.kmp.newsapp.data.repository

import kotlinx.coroutines.flow.Flow
import momin.tahir.kmp.newsapp.domain.model.Article

interface ICacheData {
    suspend fun saveArticle(article: Article)
    suspend fun deleteArticle(article: Article)

    suspend fun getSavedNewsList(): List<Article>

}