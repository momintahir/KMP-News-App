package momin.tahir.kmp.newsapp.presentation.screens.search_news

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import co.touchlab.kermit.Logger
import com.seiko.imageloader.rememberAsyncImagePainter
import momin.tahir.kmp.newsapp.domain.model.Article

class SearchNewsScreen:Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SearchNewsScreenViewModel>()
        MainContent(viewModel)
    }

    @Composable
    fun MainContent(viewModel: SearchNewsScreenViewModel) {
        var search by remember { mutableStateOf("") }
        Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(6.dp))
        Text("Discover", modifier = Modifier.padding(10.dp), style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 21.sp))
        Spacer(modifier = Modifier.height(16.dp))
        CustomSearchView(search, onValueChange = {
            search = it
           viewModel.onSearchTextChange(it)
        })
            val news = viewModel.search.collectAsState().value ?: return

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                items(news.articles) { article ->
                    NewsItem(
                        article = article,
                        onClick = {
//                            onCharacterClick(article.url)
                        },
                        onActionSave = {  }
                    )
                }
            }
    }
    }

    @Composable
    fun NewsItem(
        article: Article,
        onClick: () -> Unit,
        onActionSave : (article: Article) -> Unit
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
                    .padding(10.dp)
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
            }
        }
    }
    @Composable
    fun CustomSearchView(
        search: String,
        modifier: Modifier = Modifier,
        onValueChange: (String) -> Unit
    ) {
        Box(
            modifier = modifier
                .padding(20.dp)
                .clip(CircleShape)
                .background(Color(0XFF101921))

        ) {
            TextField(value = search,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0XFF101921),
                    placeholderColor = Color(0XFF888D91),
                    leadingIconColor = Color(0XFF888D91),
                    trailingIconColor = Color(0XFF888D91),
                    textColor = Color.White,
                    focusedIndicatorColor = Color.Transparent, cursorColor = Color(0XFF070E14)
                ),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
                trailingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
                placeholder = { Text(text = "Search") }
            )
        }

    }
}