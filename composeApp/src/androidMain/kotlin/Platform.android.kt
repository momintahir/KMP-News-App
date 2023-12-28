import android.os.Build
import momin.tahir.kmp.newsapp.presentation.screens.news_list.Platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()