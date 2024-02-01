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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
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
import momin.tahir.kmp.newsapp.presentation.screens.web_view.WebViewScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
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
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            when (val resultedState = state.value) {
                is HomeScreenViewState.Failure -> Failure(resultedState.error)
                HomeScreenViewState.Loading -> Loading()
                is HomeScreenViewState.Success -> TopNewsPager(resultedState.news, onItemClick = { webUrl ->
                    navigateToWebViewScreen(webUrl, navigator)
                }, actionSave = {
                    viewModel.saveArticle(it)
                })
            }
        }

    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TopNewsPager(news: News, onItemClick: (String) -> Unit, actionSave:(article:Article) -> Unit) {
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
                Box {
                    Image(
                        painter = rememberAsyncImagePainter(item.urlToImage ?: ""),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.height(180.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .graphicsLayer {
                                // Calculate the absolute offset for the current page from the
                                // scroll position. We use the absolute value which allows us to mirror
                                // any effects for both directions
                                val pageOffset = (
                                        (pagerState.currentPage - page) + pagerState
                                            .currentPageOffsetFraction
                                        ).absoluteValue

                                // We animate the alpha, between 50% and 100%
                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            }
                            .drawWithCache {
                                val gradient = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black),
                                    startY = size.height/3,
                                    endY = size.height
                                )
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(gradient,blendMode = BlendMode.Multiply)
                                }
                            }
                    )
                    Text(item.title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp), color = Color.White, modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 20.dp, vertical = 15.dp))
                }

            }
            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(7) { iteration ->
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


            CharactersList(news, onItemClick = {
                onItemClick(it)
            }, onActionSave = {
                actionSave(it)

            })
//            LazyColumn(
//                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
//                verticalArrangement = Arrangement.Top
//            ) {
//                items(news.articles) { article ->
//                    NewsItem(
//                        article = article,
//                        onClick = {
////                            onCharacterClick(article.url)
//                        },
//                        onActionSave = { }
//                    )
//                }
//            }
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

    @Composable
    fun CharactersList(
        news: News,
        onItemClick: (String) -> Unit,
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
                        onItemClick(article.url)
                    },
                    onActionSave = onActionSave
                )
            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun NewsItem(
        article: Article,
        onClick: () -> Unit,
        onActionSave: (article: Article) -> Unit
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
            Image(painterResource("ic_save.png"), contentDescription = null,modifier = Modifier.size(20.dp).clickable {
                onActionSave(article)
            })
        }
    }

    private fun navigateToWebViewScreen(webUrl: String, navigator: Navigator) {
        navigator.push(WebViewScreen(webUrl))
    }
}