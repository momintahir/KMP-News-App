package momin.tahir.kmp.newsapp.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SaveNewsUseCase
import kotlin.coroutines.CoroutineContext

class HomeScreenViewModel(private val allNewsUseCase: GetAllNewsUseCase,
                          private val saveArticleUseCase: SaveNewsUseCase) : ScreenModel {

    private val job = SupervisorJob()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val viewModelScope = CoroutineScope(coroutineContext)

    val newsViewState = mutableStateOf<HomeScreenViewState>(HomeScreenViewState.Loading)

    init {
        viewModelScope.launch {
            try {
                val news = allNewsUseCase.invoke()
                newsViewState.value = HomeScreenViewState.Success(news = news)
            } catch (e: Exception) {
                e.printStackTrace()
                newsViewState.value = HomeScreenViewState.Failure(e.message.toString())
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            saveArticleUseCase.invoke(article)
        }
    }
}