package momin.tahir.kmp.newsapp.presentation.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberAsyncImagePainter
import momin.tahir.kmp.newsapp.domain.model.Article
import momin.tahir.kmp.newsapp.domain.model.News
import momin.tahir.kmp.newsapp.presentation.NewsList
import momin.tahir.kmp.newsapp.presentation.screens.web_view.WebViewScreen
import kotlin.math.absoluteValue

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        MainContent(viewModel)
    }

    @Composable
    fun MainContent(viewModel: HomeScreenViewModel, navigator: Navigator = LocalNavigator.currentOrThrow) {
        val state = viewModel.newsViewState
        LaunchedEffect(Unit){
            viewModel.getSavedArticles()
        }
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            when (val resultedState = state.value) {
                is HomeScreenViewState.Failure -> Failure(resultedState.error)
                HomeScreenViewState.Loading -> Loading()
                is HomeScreenViewState.Success -> TopNewsPager(resultedState.news,viewModel.savedNews, onItemClick = { webUrl ->
                    navigateToWebViewScreen(webUrl, navigator)
                }, actionSave = {
                    viewModel.saveArticle(it)
                    viewModel.getSavedArticles()
                }, onActionRemove = {
                    viewModel.removeArticle(it)
                    viewModel.getSavedArticles()
                })
            }
        }
    }
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TopNewsPager(news: News,savedNews:List<Article>, onItemClick: (String) -> Unit, actionSave: (article: Article) -> Unit,onActionRemove:(article:Article) -> Unit) {
        val pagerState = rememberPagerState() { news.articles.take(7).size }
        Column {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Breaking News", modifier = Modifier.padding(10.dp), style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 21.sp))
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalPager(
                state = pagerState, modifier = Modifier.fillMaxWidth(),
                pageSpacing = 20.dp, contentPadding = PaddingValues(horizontal = 30.dp)
            ) { page ->
                val item = news.articles[page]
                Box(modifier=Modifier.clickable { onItemClick(item.url) }) {
                    Image(
                        painter = rememberAsyncImagePainter(item.urlToImage ?: ""),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.height(180.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .graphicsLayer {
                                val pageOffset = (
                                        (pagerState.currentPage - page) + pagerState
                                            .currentPageOffsetFraction
                                        ).absoluteValue
                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                            .drawWithCache {
                                val gradient = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black),
                                    startY = size.height / 3,
                                    endY = size.height
                                )
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(gradient, blendMode = BlendMode.Multiply)
                                }
                            }
                    )
                    Text(
                        item.title,
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                        color = Color.White,
                        modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 20.dp, vertical = 15.dp)
                    )
                }
            }
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(news.articles.take(7).size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color.Blue.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.1f)
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)

                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Recommendation", modifier = Modifier.padding(10.dp), style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black))
            Spacer(modifier = Modifier.height(6.dp))
            NewsList(news.articles,savedNews, onActionSave = { article, index ->
                actionSave(article)
                },
                onActionRemove = { article, _ ->
                    onActionRemove(article)
                }, onItemClick = {
                    onItemClick(it)
                }, showSaveIcon = true)
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
                color = Color.Black,
            )
        }
    }

    private fun navigateToWebViewScreen(webUrl: String, navigator: Navigator) {
        navigator.push(WebViewScreen(webUrl))
    }
}