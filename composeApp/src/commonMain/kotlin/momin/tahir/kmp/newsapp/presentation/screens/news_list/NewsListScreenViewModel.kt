package momin.tahir.kmp.newsapp.presentation.screens.news_list

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SaveNewsUseCase
import kotlin.coroutines.CoroutineContext

class NewsListScreenViewModel(private val allNewsUseCase: GetAllNewsUseCase,
                              private val saveArticleUseCase: SaveNewsUseCase) : ScreenModel {

    private val job = SupervisorJob()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val viewModelScope = CoroutineScope(coroutineContext)

    val newsViewState = mutableStateOf<NewsListViewState>(NewsListViewState.Loading)

    init {
        viewModelScope.launch {
            try {
                val news = allNewsUseCase.invoke()
                newsViewState.value = NewsListViewState.Success(news = news)
            } catch (e: Exception) {
                e.printStackTrace()
                newsViewState.value = NewsListViewState.Failure(e.message.toString())
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            saveArticleUseCase.invoke(article)
        }
    }
}