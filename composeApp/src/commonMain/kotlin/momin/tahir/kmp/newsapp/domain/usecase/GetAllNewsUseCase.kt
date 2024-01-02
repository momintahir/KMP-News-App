package momin.tahir.kmp.newsapp.domain.usecase

import kotlinx.coroutines.flow.flow
import momin.tahir.kmp.newsapp.data.remote.mapper.toDomain
import momin.tahir.kmp.newsapp.data.repository.INewsRepository

class GetAllNewsUseCase(private val repository: INewsRepository) {

    suspend operator fun invoke() = repository.fetchAllNews().toDomain()
}