package momin.tahir.kmp.newsapp.presentation.screens.news_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
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
import kotlin.math.sqrt

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        println("inside content")
        val viewModel = getScreenModel<NewsListScreenViewModel>()
        MainContent(viewModel)
    }

    @Composable
    fun MainContent(viewModel: NewsListScreenViewModel, navigator: Navigator = LocalNavigator.currentOrThrow) {
        val scaffoldState = rememberScaffoldState()
        val state = viewModel.newsViewState
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TopNewsPager()
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
                    )
            }
        }

    }

    @OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
    @Composable
    fun TopNewsPager() {
        val pagerState = rememberPagerState() { 5 }
        var offsetY by remember { mutableStateOf(0f) }
        HorizontalPager(state = pagerState, modifier = Modifier.background(Color.Yellow),
            contentPadding = PaddingValues(horizontal = 10.dp)
        ) { page ->
            Card(backgroundColor = Color.Red,
                modifier = Modifier
                    .size(200.dp)
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
            ) {
            }
        }
    }

    class CirclePath(private val progress: Float, private val origin: Offset = Offset(0f, 0f)) : Shape {
        override fun createOutline(
            size: Size, layoutDirection: LayoutDirection, density: Density
        ): Outline {

            val center = Offset(
                x = size.center.x - ((size.center.x - origin.x) * (1f - progress)),
                y = size.center.y - ((size.center.y - origin.y) * (1f - progress)),
            )
            val radius = (sqrt(
                size.height * size.height + size.width * size.width
            ) * .5f) * progress

            return Outline.Generic(Path().apply {
                addOval(
                    Rect(
                        center = center,
                        radius = radius,
                    )
                )
            })
        }
    }

    data class Destination(
        val location: String,
        val description: String,
    )

    val destinations = listOf(
        Destination(
            "Bali",
            "Known for its beautiful beaches, stunning temples, and lush greenery, Bali is a tropical paradise that offers a unique blend of natural beauty and cultural experiences. It's an ideal destination for those seeking a relaxing vacation filled with yoga, spa treatments, and sunsets."
        ),
        Destination(
            "Santorini",
            "A picturesque island in the Aegean Sea, Santorini is famous for its white-washed buildings, blue-domed churches, and stunning sunsets. It's a perfect destination for honeymooners or those looking for a romantic getaway."
        ),
        Destination(
            "Tokyo",
            "A bustling city that seamlessly blends tradition and modernity, Tokyo offers an exciting vacation experience that's both vibrant and unique. From traditional Japanese gardens and temples to modern shopping districts and futuristic technology, there's something for everyone in Tokyo."
        ),
        Destination(
            "Machu Picchu",
            "A UNESCO World Heritage Site, Machu Picchu is an ancient Incan citadel situated high in the Andes Mountains. It's a destination for adventure seekers, as hiking the Inca Trail is the only way to reach the site."
        ),
        Destination(
            "Banf",
            "A stunning national park in the Canadian Rockies, Banff is a year-round destination that offers scenic vistas, crystal-clear lakes, and abundant wildlife. It's an ideal spot for nature lovers, hikers, and skiers."
        ),
        Destination(
            "Marrakech",
            "A vibrant city known for its bustling markets, colorful architecture, and rich cultural heritage, Marrakech is an exotic destination that offers a unique blend of ancient and modern experiences."
        ),
        Destination(
            "The Maldives",
            "An island paradise located in the Indian Ocean, the Maldives is known for its turquoise waters, white sandy beaches, and overwater bungalows. It's an ideal destination for those seeking a luxurious and romantic getaway."
        ),
        Destination(
            "Sydney",
            "A cosmopolitan city situated on a beautiful harbor, Sydney is a destination that offers both natural beauty and urban sophistication. It's an ideal spot for beachgoers, foodies, and culture lovers."
        ),
        Destination(
            "Maui",
            "A tropical island paradise in the Pacific Ocean, Maui is known for its beautiful beaches, scenic drives, and outdoor activities such as surfing, snorkeling, and hiking. It's an ideal destination for those seeking a laid-back vacation filled with sunshine and relaxation."
        ),
        Destination(
            "Venice",
            "A romantic city built on a series of canals, Venice is known for its stunning architecture, art, and history. It's an ideal destination for culture lovers, foodies, and those seeking a romantic getaway."
        ),
    )
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