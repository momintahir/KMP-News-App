package momin.tahir.kmp.newsapp.presentation.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.usecase.DeleteNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.GetSavedNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SaveNewsUseCase
import kotlin.coroutines.CoroutineContext

class HomeScreenViewModel(private val allNewsUseCase: GetAllNewsUseCase,
                          private val saveArticleUseCase: SaveNewsUseCase,
                          private val deleteArticleUseCase: DeleteNewsUseCase,
                          private val savedArticles: GetSavedNewsUseCase) : ScreenModel {

    private val job = SupervisorJob()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val viewModelScope = CoroutineScope(coroutineContext)

    val newsViewState = mutableStateOf<HomeScreenViewState>(HomeScreenViewState.Loading)
    var savedNews =  mutableStateListOf<Article>()

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

    fun getSavedArticles() {
        viewModelScope.launch {
            savedArticles.invoke().collect{
                savedNews.clear()
                savedNews.addAll(it)
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            saveArticleUseCase.invoke(article)
        }
    }

    fun removeArticle(article: Article) {
        viewModelScope.launch {
            deleteArticleUseCase.invoke(article)
        }
    }
}