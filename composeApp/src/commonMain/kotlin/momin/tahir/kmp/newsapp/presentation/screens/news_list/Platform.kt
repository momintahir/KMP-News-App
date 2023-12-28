package momin.tahir.kmp.newsapp.presentation.screens.news_list

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform