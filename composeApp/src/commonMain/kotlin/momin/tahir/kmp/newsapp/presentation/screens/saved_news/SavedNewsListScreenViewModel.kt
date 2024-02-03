package momin.tahir.kmp.newsapp.presentation.screens.saved_news

import cafe.adriel.voyager.core.model.ScreenModel
import momin.tahir.kmp.newsapp.domain.usecase.GetSavedNewsUseCase

class SavedNewsListScreenViewModel(
    private val savedArticles: GetSavedNewsUseCase
) : ScreenModel {

    fun getSavedArticles() = savedArticles.invoke()
}