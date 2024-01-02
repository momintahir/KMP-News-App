package momin.tahir.kmp.newsapp.data.remote.mapper

import momin.tahir.kmp.newsapp.data.remote.ArticleDto
import momin.tahir.kmp.newsapp.data.remote.NewsDto
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.model.News

fun NewsDto.toDomain() = News(
    articles = articles.map { it.toDomain() },
    status = status,
    totalResults = totalResults
)

fun ArticleDto.toDomain() = Article(
    author = author,
    content = content,
    description = description,
    publishedAt = publishedAt,
    title = title,
    url = url,
    urlToImage = urlToImage
)

