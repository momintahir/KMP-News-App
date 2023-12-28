import androidx.compose.material.BottomNavigation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun App() {
    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation {
                    // Bottom Navigation items
                }
            },
            content = {
                // Content of the screen
            }
        )
    }
}

@Composable
fun BottomNavigationBar() {
    BottomNavigation {

        }
    }

sealed class BottomNavItems(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItems("home", Icons.Default.Home, "Home")
    object Search : BottomNavItems("search", Icons.Default.Search, "Search")
    object Profile : BottomNavItems("profile", Icons.Default.Person, "Profile")
}