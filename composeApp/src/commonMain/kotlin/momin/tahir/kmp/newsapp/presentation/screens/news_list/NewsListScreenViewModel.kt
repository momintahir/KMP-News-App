package momin.tahir.kmp.newsapp.presentation.screens.news_list

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SaveNewsUseCase
import kotlin.coroutines.CoroutineContext

class NewsListScreenViewModel(private val allNewsUseCase: GetAllNewsUseCase,
                              private val saveArticleUseCase: SaveNewsUseCase) : ScreenModel {
    private val job = SupervisorJob()
    private val coroutineContextX: CoroutineContext = job + Dispatchers.IO
    private val viewModelScope = CoroutineScope(coroutineContextX)

    init {
        viewModelScope.launch {
            allNewsUseCase.invoke().collectLatest {
                println("getting data from api $it")
            }
        }
    }
    fun fetchAllNews()=allNewsUseCase.invoke()
    fun saveArticle(article: Article) {
        viewModelScope.launch {
            saveArticleUseCase.invoke(article)
        }

    }
}