package momin.tahir.kmp.newsapp.presentation.screens.search_news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import momin.tahir.kmp.newsapp.presentation.NewsList
import momin.tahir.kmp.newsapp.presentation.screens.web_view.WebViewScreen

class SearchNewsScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SearchNewsScreenViewModel>()
        MainContent(viewModel)
    }

    @Composable
    fun MainContent(viewModel: SearchNewsScreenViewModel) {
        var search by remember { mutableStateOf("") }
        val navigator = LocalNavigator.currentOrThrow
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("Discover", modifier = Modifier.padding(10.dp), style = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 21.sp))
            Spacer(modifier = Modifier.height(16.dp))
            CustomSearchView(search, onValueChange = {
                search = it
                viewModel.onSearchTextChange(it)
            })
            val news = viewModel.search.collectAsState().value
            val isSearching = viewModel.isSearching.collectAsState().value
            Spacer(Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                NewsList(news?.articles, onActionSave = { article, _ ->
                    viewModel.saveArticle(article) }, onItemClick = { navigateToWebViewScreen(it, navigator) }, showSaveIcon = true)
                if (isSearching) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.TopCenter))
                }
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
                .padding(horizontal = 20.dp)
                .clip(CircleShape)
                .height(55.dp)
                .background(Color.LightGray.copy(0.2f)), contentAlignment = Alignment.Center

        ) {
            TextField(value = search,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.LightGray.copy(0.2f),
                    placeholderColor = Color.Gray.copy(0.3f),
                    leadingIconColor = Color.Gray.copy(0.9f),
                    trailingIconColor = Color.Gray.copy(0.9f),
                    textColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Gray.copy(0.3f),
                    unfocusedLabelColor = Color.Gray.copy(0.3f),
                    focusedIndicatorColor = Color.Transparent, cursorColor = Color.Transparent
                ),
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "") },
                trailingIcon = { Icon(imageVector = Icons.Default.Menu, contentDescription = "") },
                placeholder = { Text(text = "Search", color = Color.Gray.copy(0.9f)) }
            )
        }
    }

    private fun navigateToWebViewScreen(webUrl: String, navigator: Navigator) {
        navigator.push(WebViewScreen(webUrl))
    }
}