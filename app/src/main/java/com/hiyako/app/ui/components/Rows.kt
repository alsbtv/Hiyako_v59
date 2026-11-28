package com.hiyako.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.HistoryEntry
import com.hiyako.app.data.Seed
import com.hiyako.app.data.Store
import com.hiyako.app.ui.theme.LocalPalette
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun gridColumns(): Int {
    val w = LocalConfiguration.current.screenWidthDp
    return ((w - 16) / 140).coerceIn(2, 5)
}

fun LazyListScope.animeGrid(list: List<Anime>, columns: Int, onOpen: (String) -> Unit, onMore: (Anime) -> Unit) {
    items(list.chunked(columns), key = { row -> row.first().id }) { row ->
        Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            row.forEach { a -> AnimeCard(a, { onOpen(a.id) }, { onMore(a) }) }
            repeat(columns - row.size) { Spacer(Modifier.width(130.dp)) }
        }
    }
}

@Composable
fun HistoryRow(e: HistoryEntry, onClick: () -> Unit) {
    val a = Seed.anime(e.animeId) ?: return
    val p = LocalPalette.current
    val ar = isArabic()
    val pct = if (e.durSec > 0) (e.posSec * 100 / e.durSec).coerceIn(0, 100) else 0
    val done = pct >= 95
    val date = remember(e.at) { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date(e.at)) }
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.width(48.dp).aspectRatio(130f / 180f).clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            AnimeBackdrop(a, Modifier.fillMaxSize())
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(a.title, color = p.text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(tr("الحلقة ${e.ep}", "Episode ${e.ep}"), color = p.textDim, fontSize = 11.sp)
                Spacer(Modifier.width(8.dp))
                if (done) {
                    Icon(AppIcons.check, null, tint = p.ok, modifier = Modifier.size(14.dp))
                } else {
                    Text("$pct%", color = p.warn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(4.dp))
            Box(Modifier.fillMaxWidth().height(4.dp).clip(CircleShape).background(p.textDim.copy(alpha = 0.25f))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth(pct / 100f).background(if (done) p.ok else p.warn))
            }
            Spacer(Modifier.height(4.dp))
            Text(relTime(e.at, ar) + " · " + date, color = p.textDim, fontSize = 10.sp)
        }
        IconButton(onClick = { Store.deleteHistory(e) }, modifier = Modifier.size(36.dp)) {
            Icon(AppIcons.delete, null, tint = p.textDim, modifier = Modifier.size(20.dp))
        }
    }
}
