package com.hiyako.app.ui.screens

import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.hiyako.app.data.Anime
import com.hiyako.app.data.HistoryEntry
import com.hiyako.app.data.Pics
import com.hiyako.app.data.Seed
import com.hiyako.app.data.Snack
import com.hiyako.app.data.Store
import com.hiyako.app.data.WatchStatus
import com.hiyako.app.data.genreLabel
import com.hiyako.app.data.t
import com.hiyako.app.ui.components.AnimeBackdrop
import com.hiyako.app.ui.components.AppIcons
import com.hiyako.app.ui.components.EmptyState
import com.hiyako.app.ui.components.GlassSurface
import com.hiyako.app.ui.components.HistoryRow
import com.hiyako.app.ui.components.Pill
import com.hiyako.app.ui.components.PosterArt
import com.hiyako.app.ui.components.PrimaryButton
import com.hiyako.app.ui.components.QuickSheet
import com.hiyako.app.ui.components.RatingRow
import com.hiyako.app.ui.components.StatusBadge
import com.hiyako.app.ui.components.StatusPicker
import com.hiyako.app.ui.components.animeGrid
import com.hiyako.app.ui.components.gridColumns
import com.hiyako.app.ui.components.isArabic
import com.hiyako.app.ui.components.statusKey
import com.hiyako.app.ui.components.tr
import com.hiyako.app.ui.theme.LocalPalette
import kotlinx.coroutines.delay
import java.util.Locale

private val tabsAr = listOf("التفاصيل", "الحلقات", "سجل المشاهدة", "الحلقات المفضلة", "المعرض", "أعمال مشابهة")
private val tabsEn = listOf("Details", "Episodes", "Watch History", "Favorite Episodes", "Gallery", "Similar Works")

@Composable
fun DetailsScreen(id: String, onBack: () -> Unit, onOpen: (String) -> Unit) {
    val a = Seed.anime(id)
    if (a == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { EmptyState() }
    } else {
        DetailsContent(a, onBack, onOpen)
    }
}

@Composable
private fun DetailsContent(a: Anime, onBack: () -> Unit, onOpen: (String) -> Unit) {
    val p = LocalPalette.current
    val ar = isArabic()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val watchlist by Store.watchlist.collectAsState()
    val history by Store.history.collectAsState()
    val favEps by Store.favEps.collectAsState()
    val colls by Store.collections.collectAsState()
    val gc = gridColumns()
    val current = watchlist[a.id]
    val mine = history.filter { it.animeId == a.id }
    val last = mine.firstOrNull()
    val galleryUrls = remember(a.id) { Pics.gallery(a.id) }
    var tab by remember { mutableStateOf(0) }
    var variant by remember { mutableStateOf(0) }
    var showList by remember { mutableStateOf(false) }
    var translated by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf<Anime?>(null) }
    var viewer by remember { mutableStateOf(-1) }
    var range by remember { mutableStateOf(0) }
    val copyMsg = t("copyOk")
    val watchMsg = tr("تم تسجيل المشاهدة (محاكاة)", "Watch recorded (simulated)")
    val statusLabel = when (a.status) {
        "On Going" -> t("airing")
        "Completed" -> t("finished")
        "Upcoming" -> t("upcomingSt")
        else -> t("hiatus")
    }

    LaunchedEffect(a.id) {
        while (true) {
            delay(5000)
            variant = (variant + 1) % 3
        }
    }

    val watch: (Int) -> Unit = { ep ->
        val dur = 1440
        val prev = mine.firstOrNull { it.ep == ep }
        val pos = if (prev == null) 300 else minOf(prev.posSec + 300, dur)
        if (current == null) Store.setStatus(a.id, WatchStatus.WATCHING)
        Store.recordWatch(a.id, ep, pos, dur)
        Store.snack.value = Snack(watchMsg)
    }

    Box(Modifier.fillMaxSize().background(p.bg)) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Box(Modifier.fillMaxWidth().height(290.dp)) {
                    Box(Modifier.fillMaxSize().blur(28.dp)) {
                        AnimeBackdrop(a, Modifier.fillMaxSize())
                    }
                    Box(
                        Modifier.fillMaxSize()
                            .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.25f), Color.Transparent, p.bg)))
                    )
                    IconButton(onClick = onBack, modifier = Modifier.align(Alignment.TopStart).padding(4.dp)) {
                        Icon(
                            AppIcons.back, null, tint = Color.White,
                            modifier = Modifier.graphicsLayer(scaleX = if (ar) -1f else 1f)
                        )
                    }
                    Row(Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                        Box(Modifier.width(120.dp).aspectRatio(130f / 180f).clip(RoundedCornerShape(12.dp))) {
                            Crossfade(targetState = variant, label = "poster") { v ->
                                PosterArt(a, Modifier.fillMaxSize(), v)
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f).align(Alignment.Bottom)) {
                            Text(
                                a.title, color = p.text, fontSize = 20.sp, fontWeight = FontWeight.Black,
                                maxLines = 3, overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.clickable {
                                    clipboard.setText(AnnotatedString(a.title))
                                    Toast.makeText(context, copyMsg, Toast.LENGTH_SHORT).show()
                                }
                            )
                            Text(a.jp, color = p.textDim, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(Modifier.height(6.dp))
                            RatingRow(a)
                            Spacer(Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                StatusBadge(a.status)
                                Text(
                                    "${a.type} · ${a.year} · ${a.episodes} " + tr("حلقة", "eps"),
                                    color = p.textDim, fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PrimaryButton(
                            text = if (last != null) "${t("resume")} · ${tr("حلقة", "Ep")} ${last.ep}" else t("play"),
                            onClick = { watch(last?.ep ?: 1) },
                            modifier = Modifier.weight(1f),
                            icon = AppIcons.playArrow
                        )
                        PrimaryButton(
                            text = if (current != null) t(current.statusKey()) else t("addList"),
                            onClick = { showList = !showList },
                            modifier = Modifier.weight(1f),
                            icon = AppIcons.add,
                            filled = false
                        )
                    }
                    if (showList) {
                        Spacer(Modifier.height(10.dp))
                        StatusPicker(current) { Store.setStatus(a.id, it) }
                    }
                }
            }
            if (a.nextEpHours > 0) {
                item { NextEpisodeBanner(a) }
            }
            item {
                Row(
                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val names = if (ar) tabsAr else tabsEn
                    names.forEachIndexed { i, n -> Pill(n, tab == i, { tab = i }) }
                }
            }
            when (tab) {
                0 -> {
                    item {
                        Column(Modifier.padding(horizontal = 16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    t("story"), color = p.text, fontSize = 15.sp, fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                PrimaryButton(
                                    text = t("translate"),
                                    onClick = { translated = !translated },
                                    icon = AppIcons.translate,
                                    filled = false
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            val showAr = if (translated) !ar else ar
                            Text(
                                if (showAr) a.synopsisAr else a.synopsisEn,
                                color = p.textDim, fontSize = 13.sp, lineHeight = 21.sp
                            )
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                a.genres.forEach { g -> Pill(genreLabel(g, ar), false, {}) }
                            }
                            Spacer(Modifier.height(14.dp))
                            GlassSurface(Modifier.fillMaxWidth()) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    val rows = listOf(
                                        t("studio") to a.studio,
                                        t("country") to a.country,
                                        t("type") to a.type,
                                        t("status") to statusLabel,
                                        t("year") to "${a.season} ${a.year}",
                                        t("episodes") to "${a.episodes}"
                                    )
                                    rows.forEach { (k, v) ->
                                        Row(Modifier.fillMaxWidth()) {
                                            Text(k, color = p.textDim, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                            Text(v, color = p.text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }
                            if (colls.isNotEmpty()) {
                                Spacer(Modifier.height(14.dp))
                                Text(t("collections"), color = p.text, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    colls.forEach { c ->
                                        Pill(c.name, a.id in c.ids, { Store.toggleInCollection(c.id, a.id) })
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    val total = a.episodes
                    if (total > 100) {
                        item {
                            Row(
                                Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (r in 0 until (total + 99) / 100) {
                                    Pill("${r * 100 + 1}-${minOf(total, r * 100 + 100)}", range == r, { range = r })
                                }
                            }
                        }
                    }
                    val from = if (total > 100) range * 100 else 0
                    val count = minOf(100, total - from).coerceAtLeast(0)
                    items(count) { i ->
                        val ep = from + i + 1
                        EpisodeRow(a, ep, mine.firstOrNull { it.ep == ep }, "${a.id}:$ep" in favEps, watch)
                    }
                }
                2 -> {
                    if (mine.isEmpty()) {
                        item { EmptyState() }
                    } else {
                        items(mine, key = { it.ep }) { e -> HistoryRow(e) { watch(e.ep) } }
                    }
                }
                3 -> {
                    val eps = favEps.filter { it.startsWith(a.id + ":") }
                        .mapNotNull { it.substringAfter(":").toIntOrNull() }
                        .sorted()
                    if (eps.isEmpty()) {
                        item { EmptyState() }
                    } else {
                        items(eps, key = { it }) { ep ->
                            EpisodeRow(a, ep, mine.firstOrNull { it.ep == ep }, true, watch)
                        }
                    }
                }
                4 -> {
                    if (galleryUrls.isEmpty()) {
                        item { EmptyState() }
                    } else {
                        items(galleryUrls.indices.toList().chunked(3)) { row ->
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 3.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                row.forEach { i ->
                                    PhotoTile(galleryUrls[i], Modifier.weight(1f).aspectRatio(1f).clickable { viewer = i })
                                }
                                repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                            }
                        }
                    }
                }
                else -> {
                    val sim = Seed.anime.filter { it.id != a.id }
                        .map { it to it.genres.count { g -> g in a.genres } }
                        .filter { it.second > 0 }
                        .sortedByDescending { it.second }
                        .map { it.first }
                    if (sim.isEmpty()) {
                        item { EmptyState() }
                    } else {
                        animeGrid(sim, gc, onOpen, { sheet = it })
                    }
                }
            }
        }
    }

    if (viewer >= 0 && viewer < galleryUrls.size) {
        Dialog(onDismissRequest = { viewer = -1 }, properties = DialogProperties(usePlatformDefaultWidth = false)) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.94f))) {
                PhotoTile(
                    galleryUrls[viewer],
                    Modifier.align(Alignment.Center).fillMaxWidth().padding(16.dp).aspectRatio(0.75f)
                )
                Row(Modifier.align(Alignment.TopEnd).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        Modifier.size(42.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.18f))
                            .clickable { Store.saveArt(a.id + "#" + viewer) },
                        contentAlignment = Alignment.Center
                    ) { Icon(AppIcons.download, null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                    Box(
                        Modifier.size(42.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.18f))
                            .clickable { viewer = -1 },
                        contentAlignment = Alignment.Center
                    ) { Icon(AppIcons.close, null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                }
            }
        }
    }
    sheet?.let { QuickSheet(it, onDismiss = { sheet = null }, onOpen = onOpen) }
}

@Composable
private fun PhotoTile(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(10.dp))
    )
}

@Composable
private fun NextEpisodeBanner(a: Anime) {
    val p = LocalPalette.current
    val target = remember(a.id) { System.currentTimeMillis() + a.nextEpHours * 3_600_000L }
    var now by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(a.id) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }
    val rem = (target - now).coerceAtLeast(0L)
    val d = rem / 86_400_000L
    val clock = String.format(Locale.US, "%02d:%02d:%02d", (rem / 3_600_000L) % 24, (rem / 60_000L) % 60, (rem / 1000L) % 60)
    val label = if (d > 0) "$d${t("days")} $clock" else clock
    GlassSurface(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(AppIcons.clock, null, tint = p.accent, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(10.dp))
            Text(t("nextEp"), color = p.textDim, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text(label, color = p.text, fontSize = 16.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun EpisodeRow(a: Anime, ep: Int, entry: HistoryEntry?, fav: Boolean, onPlay: (Int) -> Unit) {
    val p = LocalPalette.current
    val pct = if (entry != null && entry.durSec > 0) (entry.posSec * 100 / entry.durSec).coerceIn(0, 100) else 0
    val done = pct >= 95
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.width(112.dp).aspectRatio(16f / 9f).clip(RoundedCornerShape(10.dp))
                .clickable { onPlay(ep) },
            contentAlignment = Alignment.Center
        ) {
            AnimeBackdrop(a, Modifier.fillMaxSize())
            Icon(AppIcons.playArrow, null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(28.dp))
            if (entry != null) {
                Box(
                    Modifier.align(Alignment.BottomStart).fillMaxWidth().height(3.dp)
                        .background(Color.White.copy(alpha = 0.25f))
                ) {
                    Box(Modifier.fillMaxHeight().fillMaxWidth(pct / 100f).background(if (done) p.ok else p.warn))
                }
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(tr("الحلقة $ep", "Episode $ep"), color = p.text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            if (entry != null) {
                Text(
                    if (done) tr("مكتملة", "Completed") else "$pct%",
                    color = if (done) p.ok else p.warn, fontSize = 11.sp
                )
            }
        }
        IconButton(onClick = { Store.toggleFavEp(a.id, ep) }, modifier = Modifier.size(36.dp)) {
            Icon(
                if (fav) AppIcons.heartFull else AppIcons.heart, null,
                tint = if (fav) p.accent else p.textDim, modifier = Modifier.size(20.dp)
            )
        }
        IconButton(onClick = { Store.recordWatch(a.id, ep, 1440, 1440) }, modifier = Modifier.size(36.dp)) {
            Icon(AppIcons.check, null, tint = if (done) p.ok else p.textDim, modifier = Modifier.size(20.dp))
        }
        IconButton(onClick = { Store.saveOffline(a.id, ep) }, modifier = Modifier.size(36.dp)) {
            Icon(AppIcons.download, null, tint = p.textDim, modifier = Modifier.size(20.dp))
        }
    }
}
