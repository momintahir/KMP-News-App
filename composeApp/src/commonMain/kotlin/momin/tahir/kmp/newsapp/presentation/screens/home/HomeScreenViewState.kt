package momin.tahir.kmp.newsapp.presentation.screens.home

import momin.tahir.kmp.newsapp.domain.model.News

sealed interface HomeScreenViewState {
    data object Loading : HomeScreenViewState
    data class Success(
        val news: News,
    ) : HomeScreenViewState

    data class Failure(val error: String) : HomeScreenViewState
}