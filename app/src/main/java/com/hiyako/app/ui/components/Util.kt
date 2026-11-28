package com.hiyako.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.hiyako.app.data.Anime
import com.hiyako.app.data.Pics
import com.hiyako.app.data.Store
import com.hiyako.app.data.WatchStatus
import com.hiyako.app.ui.theme.grad
import java.util.Locale

@Composable
fun isArabic(): Boolean {
    val lang by Store.language.collectAsState()
    return lang == "ar"
}

@Composable
fun tr(ar: String, en: String): String = if (isArabic()) ar else en

/** Real picture from a trusted link when one is filled in Pics; otherwise a plain colored placeholder. */
@Composable
fun AnimeBackdrop(a: Anime, modifier: Modifier = Modifier) {
    val url = Pics.banner(a.id)
    if (url.isEmpty()) {
        Box(modifier.background(grad(a.colorA, a.colorB)))
    } else {
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

fun relTime(at: Long, ar: Boolean): String {
    val mins = ((System.currentTimeMillis() - at) / 60000L).coerceAtLeast(0L)
    return when {
        mins < 1L -> if (ar) "الآن" else "just now"
        mins < 60L -> if (ar) "منذ $mins د" else "${mins}m ago"
        mins < 1440L -> if (ar) "منذ ${mins / 60L} س" else "${mins / 60L}h ago"
        else -> if (ar) "منذ ${mins / 1440L} ي" else "${mins / 1440L}d ago"
    }
}

fun fmtVotes(v: Int): String = when {
    v >= 1_000_000 -> String.format(Locale.US, "%.1fM", v / 1_000_000.0)
    v >= 1_000 -> "${v / 1_000}K"
    else -> "$v"
}

val systemStatuses = listOf(
    WatchStatus.WATCHING,
    WatchStatus.FAVORITE,
    WatchStatus.COMPLETED,
    WatchStatus.PLAN,
    WatchStatus.LATER,
    WatchStatus.DROPPED
)

fun WatchStatus.statusKey(): String = when (this) {
    WatchStatus.WATCHING -> "watching"
    WatchStatus.FAVORITE -> "favorite"
    WatchStatus.COMPLETED -> "completed"
    WatchStatus.PLAN -> "plan"
    WatchStatus.LATER -> "later"
    WatchStatus.DROPPED -> "dropped"
}

fun WatchStatus.gradA(): Long = when (this) {
    WatchStatus.WATCHING -> 0xFF1565C0
    WatchStatus.FAVORITE -> 0xFFE50046
    WatchStatus.COMPLETED -> 0xFF2E7D32
    WatchStatus.PLAN -> 0xFF6A1B9A
    WatchStatus.LATER -> 0xFFF9A825
    WatchStatus.DROPPED -> 0xFF616161
}

fun WatchStatus.gradB(): Long = when (this) {
    WatchStatus.WATCHING -> 0xFF04142B
    WatchStatus.FAVORITE -> 0xFF3B0D1B
    WatchStatus.COMPLETED -> 0xFF0C2B12
    WatchStatus.PLAN -> 0xFF1D0726
    WatchStatus.LATER -> 0xFF2B1600
    WatchStatus.DROPPED -> 0xFF1A1A1A
}
