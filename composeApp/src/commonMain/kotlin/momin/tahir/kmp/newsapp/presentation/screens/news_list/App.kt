package momin.tahir.kmp.newsapp.presentation.screens.news_list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import momin.tahir.kmp.newsapp.presentation.screens.saved_news.SavedNewsListScreen

@Composable
fun App() {
    val screens = listOf("News", "Saved")
    var selectedScreen by remember { mutableStateOf(screens.firstOrNull()) }
    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation {
                        screens.forEach { screen ->
                            BottomNavigationItem(
                                icon = { Icon(getIconForScreen(screen), contentDescription = screen) },
                                label = { Text(screen) },
                                selected = screen == selectedScreen,
                                onClick = { selectedScreen = screen },
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                }
            },
            content = {
                if (selectedScreen== "News") NewsScreen() else SavedNewsScreen()
            }
        )
    }
}
@Composable
fun NewsScreen() {
    Navigator(HomeScreen())
}

@Composable
fun SavedNewsScreen() {
    Navigator(SavedNewsListScreen())
}
@Composable
fun getIconForScreen(screen: String): ImageVector {
    return when (screen) {
        "News" -> Icons.Default.List
        "Saved" -> Icons.Default.Favorite
        else -> Icons.Default.List
    }
}