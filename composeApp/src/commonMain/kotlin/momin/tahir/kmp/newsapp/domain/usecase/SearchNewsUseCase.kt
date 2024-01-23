package momin.tahir.kmp.newsapp.domain.usecase

import kotlinx.coroutines.flow.flow
import momin.tahir.kmp.newsapp.data.remote.NewsDto
import momin.tahir.kmp.newsapp.data.remote.mapper.toDomain
import momin.tahir.kmp.newsapp.data.repository.INewsRepository
import momin.tahir.kmp.newsapp.domain.model.News

class SearchNewsUseCase(private val repository: INewsRepository) {

    suspend operator fun invoke(query:String) = repository.searchNews(query).toDomain()
}