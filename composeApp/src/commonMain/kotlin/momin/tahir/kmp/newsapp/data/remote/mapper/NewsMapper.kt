package momin.tahir.kmp.newsapp.data.remote.mapper

import momin.tahir.kmp.newsapp.data.remote.ArticleDto
import momin.tahir.kmp.newsapp.data.remote.NewsDto
import momin.tahir.kmp.newsapp.data.remote.SourceDto
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.model.News
import momin.tahir.kmp.newsapp.domain.model.Source

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
    source = source.toDomain(),
    title = title,
    url = url,
    urlToImage = urlToImage
)

fun SourceDto.toDomain() = Source(
    id = id,
    name = name
)

