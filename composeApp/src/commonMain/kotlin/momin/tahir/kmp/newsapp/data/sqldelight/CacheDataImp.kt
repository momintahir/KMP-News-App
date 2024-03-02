package momin.tahir.kmp.newsapp.data.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import momin.tahir.kmp.newsapp.data.repository.ICacheData
import momin.tahir.kmp.newsapp.domain.model.Article

class CacheDataImp(private val appDatabase: Database) : ICacheData {
    override suspend fun saveArticle(article: Article) {
        appDatabase {
        it.appDatabaseQueries.insertNewsFavourite(
            article.author,
            article.content,
            article.description,
            article.publishedAt,
            article.title,
            article.url,
            article.urlToImage
        )
        }
    }

    override suspend fun deleteArticle(article: Article) {
        appDatabase {
            it.appDatabaseQueries.deleteArticle(article.url)
        }
    }

    override suspend fun getSavedNewsList()  = appDatabase { appDatabase ->
            appDatabase.appDatabaseQueries.selectAllNewsFavorite(::mapFavorite).executeAsList()
    }


    private fun mapFavorite(
         author: String?,
         content: String,
         description: String?,
         publishedAt: String,
         title: String,
         url: String,
         urlToImage: String?
    ) = Article(author = author, content = content, description = description, publishedAt = publishedAt,
        title = title, url = url, urlToImage = urlToImage)
}