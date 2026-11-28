package com.hiyako.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.Seed
import com.hiyako.app.data.Snack
import com.hiyako.app.data.Store
import com.hiyako.app.data.t
import com.hiyako.app.ui.components.AnimeCard
import com.hiyako.app.ui.components.AppIcons
import com.hiyako.app.ui.components.ContinueCard
import com.hiyako.app.ui.components.HeroCarousel
import com.hiyako.app.ui.components.QuickSheet
import com.hiyako.app.ui.components.SectionHeader
import com.hiyako.app.ui.components.isArabic
import com.hiyako.app.ui.components.relTime
import com.hiyako.app.ui.components.tr
import com.hiyako.app.ui.theme.LocalPalette

@Composable
fun HomeScreen(onMenu: () -> Unit, onOpen: (String) -> Unit, onSearch: () -> Unit) {
    val p = LocalPalette.current
    val ar = isArabic()
    val history by Store.history.collectAsState()
    val toggles by Store.toggles.collectAsState()
    val notifs by Store.notifs.collectAsState()
    var sheet by remember { mutableStateOf<Anime?>(null) }
    var barVisible by remember { mutableStateOf(true) }
    val soon = tr("هذه الشاشة قادمة في مرحلة لاحقة", "This screen arrives in a later phase")
    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -6f) barVisible = false
                else if (available.y > 6f) barVisible = true
                return Offset.Zero
            }
        }
    }
    val showAdult = toggles["adult"] ?: true
    val all = remember(showAdult) { Seed.anime.filter { showAdult || !it.adult } }
    val hero = remember(all) { all.sortedByDescending { it.likes }.take(5) }
    val recent = remember(all) {
        all.sortedWith(compareByDescending<Anime> { it.status == "On Going" }.thenByDescending { it.year })
    }
    val trending = remember(all) { all.sortedByDescending { it.likes } }
    val popular = remember(all) { all.sortedByDescending { it.votes } }
    val top = remember(all) { all.sortedByDescending { it.mal } }
    val dub = remember(all) { all.filter { it.dub }.sortedByDescending { it.year } }
    val movies = remember(all) { all.filter { it.type == "Movie" } }
    val unread = notifs.any { !it.read }

    Box(Modifier.fillMaxSize().nestedScroll(connection)) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 56.dp, bottom = 16.dp)) {
            item {
                HeroCarousel(items = hero, onTap = { sheet = it }, onMore = { sheet = it })
                Spacer(Modifier.height(8.dp))
            }
            if (toggles["continue"] != false && history.isNotEmpty()) {
                item {
                    Column {
                        SectionHeader(t("continue"))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(history.take(10), key = { "${it.animeId}:${it.ep}" }) { h ->
                                val a = Seed.anime(h.animeId)
                                if (a != null) {
                                    val progress = if (h.durSec > 0) h.posSec.toFloat() / h.durSec else 0f
                                    ContinueCard(a, h.ep, a.episodes, progress, relTime(h.at, ar)) { onOpen(a.id) }
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
            item { AnimeRow(t("recent"), recent, onOpen) { sheet = it } }
            item { AnimeRow(t("trending"), trending, onOpen) { sheet = it } }
            item { AnimeRow(t("popular"), popular, onOpen) { sheet = it } }
            item { AnimeRow(t("top"), top, onOpen) { sheet = it } }
            item { AnimeRow(t("dub"), dub, onOpen) { sheet = it } }
            item { AnimeRow(t("movies"), movies, onOpen) { sheet = it } }
        }
        AnimatedVisibility(
            visible = barVisible,
            modifier = Modifier.align(Alignment.TopCenter),
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut()
        ) {
            Row(
                Modifier.fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(p.bg.copy(alpha = 0.92f), Color.Transparent)))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMenu) { Icon(AppIcons.menu, null, tint = p.text) }
                Text(
                    t("browse"), color = p.text, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
                IconButton(onClick = { Store.snack.value = Snack(soon) }) {
                    Box {
                        Icon(AppIcons.bell, null, tint = p.text)
                        if (unread) {
                            Box(Modifier.align(Alignment.TopEnd).size(8.dp).clip(CircleShape).background(p.accent))
                        }
                    }
                }
                IconButton(onClick = onSearch) { Icon(AppIcons.search, null, tint = p.text) }
            }
        }
    }
    sheet?.let { QuickSheet(it, onDismiss = { sheet = null }, onOpen = onOpen) }
}

@Composable
private fun AnimeRow(title: String, list: List<Anime>, onOpen: (String) -> Unit, onMore: (Anime) -> Unit) {
    if (list.isEmpty()) return
    Column {
        SectionHeader(title)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(list, key = { it.id }) { a -> AnimeCard(a, { onOpen(a.id) }, { onMore(a) }) }
        }
        Spacer(Modifier.height(8.dp))
    }
}
