package momin.tahir.kmp.newsapp.domain.usecase

import kotlinx.coroutines.flow.flow
import momin.tahir.kmp.newsapp.data.repository.INewsRepository
import momin.tahir.kmp.newsapp.domain.model.Article

class DeleteNewsUseCase(private val repository: INewsRepository)  {
    suspend operator fun invoke(article: Article) = repository.deleteArticle(article)
}