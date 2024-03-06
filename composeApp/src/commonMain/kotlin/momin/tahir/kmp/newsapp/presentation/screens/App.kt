package momin.tahir.kmp.newsapp.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import momin.tahir.kmp.newsapp.presentation.screens.home.HomeScreen
import momin.tahir.kmp.newsapp.presentation.screens.profile.ProfileScreen
import momin.tahir.kmp.newsapp.presentation.screens.saved_news.SavedNewsListScreen
import momin.tahir.kmp.newsapp.presentation.screens.search_news.SearchNewsScreen

@Composable
fun App() {
    val screens = listOf("News", "Search", "Saved", "Profile")
    var selectedScreen by remember { mutableStateOf(screens.firstOrNull()) }
    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = Color.White) {
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
                if (selectedScreen == "News") NewsScreen() else if (selectedScreen == "Search") SearchNewsScreenFun() else if (selectedScreen == "Profile") UserProfileScreen() else SavedNewsScreen()
            }
        )
    }
}

@Composable
fun NewsScreen() {
    Navigator(HomeScreen())
}

@Composable
fun UserProfileScreen() {
    Navigator(ProfileScreen())
}


@Composable
fun SearchNewsScreenFun() {
    Navigator(SearchNewsScreen())
}

@Composable
fun SavedNewsScreen() {
    Navigator(SavedNewsListScreen())
}

@Composable
fun getIconForScreen(screen: String): ImageVector {
    return when (screen) {
        "News" -> Icons.Default.Home
        "Discover" -> Icons.Default.Search
        "Saved" -> Icons.Default.FavoriteBorder
        "Profile" -> Icons.Default.Menu
        else -> Icons.Default.List
    }
}