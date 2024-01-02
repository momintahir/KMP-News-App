package momin.tahir.kmp.newsapp.presentation.screens.saved_news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.presentation.screens.web_view.WebViewScreen

class SavedNewsListScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        println("inside content")
        val viewModel = getScreenModel<SavedNewsListScreenViewModel>()

        MainScreen(viewModel)
    }

    @Composable
    fun MainScreen(viewModel: SavedNewsListScreenViewModel, navigator: Navigator = LocalNavigator.currentOrThrow) {
        val news = remember { mutableStateOf<List<Article>>(emptyList()) }
        val scaffoldState = rememberScaffoldState()
        LaunchedEffect(Unit) {
            viewModel.getSavedArticles().collect {
                news.value = it
            }
        }
        Scaffold(
            scaffoldState = scaffoldState,
            content = { padding ->
                CharactersList(news.value, onCharacterClick =  {
                    navigateToWebViewScreen(it,navigator)
                }, onActionSave = {
//                    viewModel.saveArticle(it)
                })
            },
            topBar = { TopAppBar(title = {
                Text("Save", modifier = Modifier.clickable {

                })
            }, actions = {
            } )},
            drawerContent = {
              },
        )
    }

    @Composable
    fun CharactersList(
        article: List<Article>,
        onCharacterClick: (String) -> Unit,
        onActionSave: (article: Article) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            items(article) { article ->
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
        Row(modifier = Modifier.fillMaxWidth().height(110.dp).background(Color.Yellow).clickable(onClick = onClick), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(article.urlToImage ?: ""),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .padding(10.dp)
                    .width(110.dp)
                    .height(110.dp)
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
            }
        }
    }

    private fun navigateToWebViewScreen(webUrl: String, navigator: Navigator) {
        navigator.push(WebViewScreen(webUrl))
    }
}