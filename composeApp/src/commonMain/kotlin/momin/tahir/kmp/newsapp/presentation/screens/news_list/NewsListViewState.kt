package momin.tahir.kmp.newsapp.presentation.screens.news_list

import momin.tahir.kmp.newsapp.domain.model.News

sealed interface NewsListViewState {
    object Loading : NewsListViewState
    data class Success(
        val news: News,
    ) : NewsListViewState

    data class Failure(val error: String) : NewsListViewState
}