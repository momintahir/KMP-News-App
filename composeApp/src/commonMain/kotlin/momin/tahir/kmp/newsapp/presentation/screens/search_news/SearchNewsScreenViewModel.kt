package momin.tahir.kmp.newsapp.presentation.screens.search_news

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import momin.tahir.kmp.newsapp.domain.model.News
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.GetSavedNewsUseCase
import momin.tahir.kmp.newsapp.domain.usecase.SearchNewsUseCase

class SearchNewsScreenViewModel(private val searchNewsUseCase: SearchNewsUseCase) : ScreenModel {

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


}