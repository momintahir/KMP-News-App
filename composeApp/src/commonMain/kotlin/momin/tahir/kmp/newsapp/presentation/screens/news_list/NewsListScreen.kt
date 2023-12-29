package momin.tahir.kmp.newsapp.presentation.screens.news_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel

class NewsListScreen:Screen {
    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {
        println("inside content")
        val viewModel = getScreenModel<NewsListScreenViewModel>()
        MainScreen(viewModel)
    }

    @Composable
    fun MainScreen(viewModel: NewsListScreenViewModel) {
       val news = viewModel.fetchAllNews().collectAsState(initial = null)
        println("currentNews" + news.value?.articles?.map { it })
    }
}