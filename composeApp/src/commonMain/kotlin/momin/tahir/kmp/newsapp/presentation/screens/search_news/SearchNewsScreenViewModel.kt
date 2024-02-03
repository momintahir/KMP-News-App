package momin.tahir.kmp.newsapp.presentation.screens.search_news

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.model.News
import momin.tahir.kmp.newsapp.domain.usecase.SaveNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SearchNewsUseCase
import kotlin.coroutines.CoroutineContext

class SearchNewsScreenViewModel(private val searchNewsUseCase: SearchNewsUseCase, private val saveArticleUseCase: SaveNewsUseCase) : ScreenModel {

    private val job = SupervisorJob()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO
    private val viewModelScope = CoroutineScope(coroutineContext)

    private val _searchText = MutableStateFlow("")
    private val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _news = MutableStateFlow<News?>(null)
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    @OptIn(FlowPreview::class)
    val search = searchText
        .debounce(1000)
        .onEach { _isSearching.update { true } }
        .combine(_news) { text, news ->
            if (text.isNotBlank()) {
                searchNewsUseCase.invoke(text)
            } else {
                news
            }
        }.onEach { _isSearching.update { false } }
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5000),
            _news.value
        )

    fun saveArticle(article: Article) {
        viewModelScope.launch {
            saveArticleUseCase.invoke(article)
        }
    }
}