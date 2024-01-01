package momin.tahir.kmp.newsapp.domain.usecase

import kotlinx.coroutines.flow.flow
import momin.tahir.kmp.newsapp.data.remote.mapper.toDomain
import momin.tahir.kmp.newsapp.data.repository.INewsRepository

class GetSavedNewsUseCase(private val repository: INewsRepository) {

    operator fun invoke() = flow {
        val response = repository.getSavedArticles()
        emit(response)
    }
}