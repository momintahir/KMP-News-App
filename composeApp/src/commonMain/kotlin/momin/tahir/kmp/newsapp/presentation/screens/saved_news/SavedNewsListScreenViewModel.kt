package momin.tahir.kmp.newsapp.presentation.screens.saved_news

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.GetSavedNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SaveNewsUseCase
import kotlin.coroutines.CoroutineContext

class SavedNewsListScreenViewModel(private val allNewsUseCase: GetAllNewsUseCase,
                                   private val savedArticles: GetSavedNewsUseCase) : ScreenModel {
    private val job = SupervisorJob()
    private val coroutineContextX: CoroutineContext = job + Dispatchers.IO
    private val viewModelScope = CoroutineScope(coroutineContextX)

//    fun getSavedArticles()=savedArticles.invoke()
}