package momin.tahir.kmp.newsapp.presentation.screens.web_view

import WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen

class WebViewScreen(private val url: String) : Screen {
    @Composable
    override fun Content() {
        Column(modifier = Modifier.fillMaxSize()) {
            WebView(modifier = Modifier.fillMaxSize(), url)
        }
    }
}