package momin.tahir.kmp.newsapp.presentation.screens.search_news

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import momin.tahir.kmp.newsapp.domain.usecase.GetSavedNewsUseCase

class SearchNewsScreenViewModel() : ScreenModel {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    val search = searchText.map {
        if (it.isNotEmpty()){

        }
    }
}