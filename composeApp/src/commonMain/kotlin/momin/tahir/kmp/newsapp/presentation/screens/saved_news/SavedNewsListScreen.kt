package momin.tahir.kmp.newsapp.presentation.screens.saved_news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberAsyncImagePainter
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.model.News
import momin.tahir.kmp.newsapp.presentation.screens.web_view.WebViewScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

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
        Column {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Saved News", modifier = Modifier.padding(10.dp), style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 21.sp))
            Spacer(modifier = Modifier.height(16.dp))

            CharactersList(news.value, onItemClick = {
                navigateToWebViewScreen(it,navigator)
            })
        }
    }
    @Composable
    fun CharactersList(
        articles: List<Article>,
        onItemClick: (String) -> Unit,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            items(articles) { article ->
                NewsItem(
                    article = article,
                    onClick = {
                        onItemClick(article.url)
                    },
                )
            }
        }
    }


//    @Composable
//    fun CharactersList(
//        article: List<Article>,
//        onCharacterClick: (String) -> Unit,
//        onActionSave: (article: Article) -> Unit,
//    ) {
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Top
//        ) {
//            items(article) { article ->
//                NewsItem(
//                    article = article,
//                    onClick = {
//                        onCharacterClick(article.url)
//                    },
//                    onActionSave = onActionSave
//                )
//            }
//        }
//    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun NewsItem(
        article: Article,
        onClick: () -> Unit,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(110.dp).clickable(onClick = onClick), horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(article.urlToImage ?: ""),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(120.dp)
                    .height(110.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = article.title,
                    modifier = Modifier,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )
                Text(
                    text = article.description.orEmpty(),
                    modifier = Modifier,
                    maxLines = 2,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }
    }

//    @Composable
//    fun NewsItem(
//        article: Article,
//        onClick: () -> Unit,
//    ) {
//        Row(modifier = Modifier.fillMaxWidth().height(110.dp).clickable(onClick = onClick), horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically) {
//            Image(
//                painter = rememberAsyncImagePainter(article.urlToImage ?: ""),
//                contentDescription = null,
//                contentScale = ContentScale.FillBounds,
//                modifier = Modifier
//                    .width(110.dp)
//                    .height(110.dp)
//                    .padding(10.dp)
//                    .clip(CircleShape)
//            )
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//            ) {
//                Text(
//                    text = article.title,
//                    modifier = Modifier,
//                    maxLines = 1
//                )
//                Text(
//                    text = article.description.orEmpty(),
//                    modifier = Modifier,
//                    maxLines = 2
//                )
//            }
//        }
//    }

    private fun navigateToWebViewScreen(webUrl: String, navigator: Navigator) {
        navigator.push(WebViewScreen(webUrl))
    }
}