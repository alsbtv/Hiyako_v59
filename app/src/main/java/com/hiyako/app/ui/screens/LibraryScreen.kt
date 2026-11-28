package com.hiyako.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.Seed
import com.hiyako.app.data.Store
import com.hiyako.app.data.genreLabel
import com.hiyako.app.data.matchScore
import com.hiyako.app.data.t
import com.hiyako.app.ui.components.AppIcons
import com.hiyako.app.ui.components.HiyakoTextField
import com.hiyako.app.ui.components.Pill
import com.hiyako.app.ui.components.PrimaryButton
import com.hiyako.app.ui.components.QuickSheet
import com.hiyako.app.ui.components.animeGrid
import com.hiyako.app.ui.components.gridColumns
import com.hiyako.app.ui.components.isArabic
import com.hiyako.app.ui.components.onAccentColor
import com.hiyako.app.ui.components.tr
import com.hiyako.app.ui.theme.LocalPalette

private fun sortList(list: List<Anime>, key: String): List<Anime> = when (key) {
    "name" -> list.sortedBy { it.title.lowercase() }
    "year" -> list.sortedByDescending { it.year }
    else -> list.sortedByDescending { it.mal }
}

private fun compute(query: String, genres: Set<String>, sortKey: String, statusFilter: String, showAdult: Boolean): List<Anime> {
    val q = query.trim()
    var list = Seed.anime.filter { a -> (showAdult || !a.adult) && genres.all { g -> g in a.genres } }
    list = when (statusFilter) {
        "airing" -> list.filter { it.status == "On Going" }
        "finished" -> list.filter { it.status == "Completed" }
        else -> list
    }
    if (q.isNotEmpty()) {
        val scored = list.map { it to matchScore(it, q) }.filter { it.second > 0 }
        list = if (sortKey == "relevance") {
            scored.sortedByDescending { it.second }.map { it.first }
        } else {
            sortList(scored.map { it.first }, sortKey)
        }
    } else {
        list = sortList(list, if (sortKey == "relevance") "rating" else sortKey)
    }
    return list
}

@Composable
fun LibraryScreen(onOpen: (String) -> Unit) {
    val p = LocalPalette.current
    val recents by Store.recents.collectAsState()
    val toggles by Store.toggles.collectAsState()
    var query by remember { mutableStateOf("") }
    var genres by remember { mutableStateOf<Set<String>>(emptySet()) }
    var sortKey by remember { mutableStateOf("relevance") }
    var statusFilter by remember { mutableStateOf("all") }
    var showFilters by remember { mutableStateOf(false) }
    var sheet by remember { mutableStateOf<Anime?>(null) }
    val gc = gridColumns()
    val showAdult = toggles["adult"] ?: true
    val results = remember(query, genres, sortKey, statusFilter, showAdult) {
        compute(query, genres, sortKey, statusFilter, showAdult)
    }
    val activeCount = genres.size + (if (statusFilter != "all") 1 else 0) + (if (sortKey != "relevance") 1 else 0)
    val open: (String) -> Unit = { id ->
        if (query.isNotBlank()) Store.addRecent(query.trim())
        onOpen(id)
    }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HiyakoTextField(
                value = query,
                onValueChange = { query = it },
                hint = t("search"),
                modifier = Modifier.weight(1f),
                leading = AppIcons.search,
                onSearch = { Store.addRecent(query.trim()) }
            )
            Spacer(Modifier.width(8.dp))
            Box(
                Modifier.size(46.dp).clip(RoundedCornerShape(14.dp))
                    .background(if (activeCount > 0) p.accent else p.surface)
                    .clickable { showFilters = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    AppIcons.filter, null,
                    tint = if (activeCount > 0) onAccentColor() else p.text,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        LazyColumn(Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(bottom = 16.dp)) {
            if (query.isBlank() && recents.isNotEmpty()) {
                item {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                t("recents"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                t("clearRecents"), color = p.accent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.clickable { Store.clearRecents() }
                            )
                        }
                        recents.forEach { r ->
                            Row(
                                Modifier.fillMaxWidth().clickable { query = r }.padding(vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(AppIcons.history, null, tint = p.textDim, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(10.dp))
                                Text(r, color = p.text, fontSize = 13.sp, maxLines = 1, modifier = Modifier.weight(1f))
                                Icon(
                                    AppIcons.close, null, tint = p.textDim,
                                    modifier = Modifier.size(18.dp).clickable { Store.removeRecent(r) }
                                )
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    "${results.size} ${t("results")}", color = p.textDim, fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            if (results.isEmpty()) {
                item {
                    Text(
                        t("noResults"), color = p.textDim, fontSize = 14.sp, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(32.dp)
                    )
                }
            } else {
                animeGrid(results, gc, open, { sheet = it })
            }
        }
    }

    if (showFilters) {
        FilterSheet(
            initGenres = genres,
            initSort = sortKey,
            initStatus = statusFilter,
            onDismiss = { showFilters = false },
            onApply = { g, s, st ->
                genres = g
                sortKey = s
                statusFilter = st
                showFilters = false
            }
        )
    }
    sheet?.let { QuickSheet(it, onDismiss = { sheet = null }, onOpen = onOpen) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterSheet(
    initGenres: Set<String>,
    initSort: String,
    initStatus: String,
    onDismiss: () -> Unit,
    onApply: (Set<String>, String, String) -> Unit
) {
    val p = LocalPalette.current
    val ar = isArabic()
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var g by remember { mutableStateOf(initGenres) }
    var s by remember { mutableStateOf(initSort) }
    var st by remember { mutableStateOf(initStatus) }
    val allGenres = remember { Seed.anime.flatMap { it.genres }.distinct().sorted() }
    val sortOptions = listOf(
        "relevance" to tr("الصلة", "Relevance"),
        "name" to t("name"),
        "year" to t("year"),
        "rating" to t("rating")
    )
    val statusOptions = listOf(
        "all" to tr("الكل", "All"),
        "airing" to t("airing"),
        "finished" to t("finished")
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = state,
        containerColor = p.surface,
        contentColor = p.text
    ) {
        Column(Modifier.fillMaxWidth().fillMaxHeight(0.85f)) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
                Text(t("sort"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    sortOptions.forEach { (k, label) -> Pill(label, s == k, { s = k }) }
                }
                Spacer(Modifier.height(16.dp))
                Text(t("status"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    statusOptions.forEach { (k, label) -> Pill(label, st == k, { st = k }) }
                }
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        t("genres"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Text("${g.size}/5", color = p.textDim, fontSize = 12.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(
                        t("clear"), color = p.accent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { g = emptySet() }
                    )
                }
                Spacer(Modifier.height(8.dp))
                allGenres.chunked(3).forEach { row ->
                    Row(
                        Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { name ->
                            Pill(
                                genreLabel(name, ar),
                                name in g,
                                { g = if (name in g) g - name else if (g.size < 5) g + name else g },
                                Modifier.weight(1f)
                            )
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                    }
                }
            }
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryButton(
                    text = t("clear"),
                    onClick = {
                        g = emptySet()
                        s = "relevance"
                        st = "all"
                    },
                    modifier = Modifier.weight(1f),
                    filled = false
                )
                PrimaryButton(text = t("apply"), onClick = { onApply(g, s, st) }, modifier = Modifier.weight(1f))
            }
        }
    }
}
