package momin.tahir.kmp.newsapp.presentation.screens.news_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberAsyncImagePainter
import kotlinx.coroutines.flow.collect
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.model.News
import momin.tahir.kmp.newsapp.presentation.screens.saved_news.SavedNewsListScreen
import momin.tahir.kmp.newsapp.presentation.screens.web_view.WebViewScreen
import org.jetbrains.compose.resources.painterResource

class NewsListScreen : Screen {
    @Composable
    override fun Content() {
        println("inside content")
        val viewModel = getScreenModel<NewsListScreenViewModel>()
        MainScreen(viewModel)
    }

    @Composable
    fun MainScreen(viewModel: NewsListScreenViewModel, navigator: Navigator = LocalNavigator.currentOrThrow) {
        val scaffoldState = rememberScaffoldState()
        val state = viewModel.newsViewState
        when (val resultedState = state.value) {
            is NewsListViewState.Failure -> Failure(resultedState.error)
            NewsListViewState.Loading -> Loading()
            is NewsListViewState.Success ->
                Scaffold(
                    scaffoldState = scaffoldState,
                    content = { padding ->
                        CharactersList(resultedState.news, onCharacterClick = {
                            navigateToWebViewScreen(it, navigator)
                        }, onActionSave = {
                            viewModel.saveArticle(it)
                        })
                    },
                    topBar = { TopAppBar(title = { Text("News App") }) },
                )
        }
    }
    @Composable
    fun Failure(message: String) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = message, modifier = Modifier.align(Alignment.Center))
        }
    }
    @Composable
    fun Loading() {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Magenta,
            )
        }
    }
    @Composable
    fun CharactersList(
        news: News,
        onCharacterClick: (String) -> Unit,
        onActionSave: (article: Article) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            items(news.articles) { article ->
                NewsItem(
                    article = article,
                    onClick = {
                        onCharacterClick(article.url)
                    },
                    onActionSave = onActionSave
                )
            }
        }
    }

    @Composable
    fun NewsItem(
        article: Article,
        onClick: () -> Unit,
        onActionSave : (article:Article) -> Unit
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(110.dp).clickable(onClick = onClick), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(article.urlToImage ?: ""),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(110.dp)
                    .height(110.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = article.title,
                    modifier = Modifier,
                    maxLines = 1
                )
                Text(
                    text = article.description.orEmpty(),
                    modifier = Modifier,
                    maxLines = 2
                )
                Button(content = {
                    Text("Save article")
                }, onClick = {
                    onActionSave(article)
                })
            }
        }
    }

    private fun navigateToWebViewScreen(webUrl: String, navigator: Navigator) {
        navigator.push(WebViewScreen(webUrl))
    }
}