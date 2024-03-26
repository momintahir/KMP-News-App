package momin.tahir.kmp.newsapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberAsyncImagePainter
import momin.tahir.kmp.newsapp.domain.model.Article
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun NewsList(
    articles: List<Article>?,
    savedNews: List<Article>? = null,
    onItemClick: (String) -> Unit,
    showSaveIcon: Boolean,
    onActionSave: (article: Article, index: Int) -> Unit = { _, _ -> },
    onActionRemove: (article: Article, index: Int) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(bottom = 70.dp)
    ) {
        if (articles != null) {
            itemsIndexed(articles) { index, article ->
                NewsItem(
                    article = article,
                    showSaveIcon = showSaveIcon,
                    isSelected = savedNews?.contains(article) == true,
                    onClick = { onItemClick(article.url) },
                    onActionSave = {
                        onActionSave(article, index)
                    },
                    onActionRemove = { onActionRemove(article, index) }
                )
            }
        }
    }
}
@OptIn(ExperimentalResourceApi::class)
@Composable
fun NewsItem(
    article: Article,
    showSaveIcon: Boolean,
    onClick: () -> Unit,
    isSelected: Boolean,
    onActionSave: (article: Article) -> Unit,
    onActionRemove: (article: Article) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(110.dp).clickable(onClick = onClick), horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(article.urlToImage ?: ""),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .width(120.dp)
                .height(110.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = article.title,
                modifier = Modifier,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp
            )
            Text(
                text = article.description.orEmpty(),
                modifier = Modifier,
                maxLines = 2,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 0.6f)
            )
        }
        if (showSaveIcon) {
            Image(painterResource(if (isSelected) "ic_saved.png" else "ic_save.png"), contentDescription = null,
                modifier = Modifier.size(20.dp).clickable {
                    if (isSelected) onActionRemove(article) else onActionSave(article)
                })
        }
    }
}
