package momin.tahir.kmp.newsapp.presentation.screens.news_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import momin.tahir.kmp.newsapp.presentation.screens.saved_news.SavedNewsListScreen

@Composable
fun App() {
    val screens = listOf("Home", "Feed")
    var selectedScreen by remember { mutableStateOf(screens.firstOrNull()) }
    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    with(screens.iterator()) {
                        forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(getIconForScreen(screen), contentDescription = screen) },
                                label = { Text(screen) },
                                selected = screen == selectedScreen,
                                onClick = { selectedScreen = screen },
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            },
            content = {
                if (selectedScreen== "Home") NewsScreen() else SavedNewsScreen()
            }
        )
    }
}
@Composable
fun NewsScreen() {
    Navigator(NewsListScreen())
}

@Composable
fun SavedNewsScreen() {
    Navigator(SavedNewsListScreen())
}
@Composable
fun getIconForScreen(screen: String): ImageVector {
    return when (screen) {
        "Home" -> Icons.Default.Home
        "Feed" -> Icons.Default.AccountBox
        else -> Icons.Default.Home
    }
}