package com.hiyako.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.Seed
import com.hiyako.app.data.Store
import com.hiyako.app.data.t
import com.hiyako.app.ui.components.AppIcons
import com.hiyako.app.ui.components.EmptyState
import com.hiyako.app.ui.components.HiyakoTextField
import com.hiyako.app.ui.components.Pill
import com.hiyako.app.ui.components.PrimaryButton
import com.hiyako.app.ui.components.QuickSheet
import com.hiyako.app.ui.components.animeGrid
import com.hiyako.app.ui.components.gradA
import com.hiyako.app.ui.components.gradB
import com.hiyako.app.ui.components.gridColumns
import com.hiyako.app.ui.components.isArabic
import com.hiyako.app.ui.components.statusKey
import com.hiyako.app.ui.components.systemStatuses
import com.hiyako.app.ui.theme.LocalPalette

@Composable
fun MyListScreen(onOpen: (String) -> Unit) {
    val p = LocalPalette.current
    val ar = isArabic()
    val watchlist by Store.watchlist.collectAsState()
    val colls by Store.collections.collectAsState()
    val offline by Store.offline.collectAsState()
    var selected by remember { mutableStateOf<String?>(null) }
    var offlineOnly by remember { mutableStateOf(false) }
    var creating by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf("") }
    var sheet by remember { mutableStateOf<Anime?>(null) }
    val gc = gridColumns()
    val offIds = offline.map { it.animeId }.toSet()
    val sel = selected
    val statusSel = systemStatuses.firstOrNull { sel == "s:" + it.name }
    val colSel = colls.firstOrNull { sel == "c:" + it.id }
    val inDetail = statusSel != null || colSel != null

    BackHandler(enabled = inDetail) { selected = null }

    val keep: (Anime) -> Boolean = { !offlineOnly || it.id in offIds }
    val detailTitle = if (statusSel != null) t(statusSel.statusKey()) else (colSel?.name ?: "")
    val detailList = when {
        statusSel != null -> Seed.anime.filter { watchlist[it.id] == statusSel && keep(it) }
        colSel != null -> colSel.ids.mapNotNull { Seed.anime(it) }.filter(keep)
        else -> emptyList()
    }

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 20.dp)) {
        item {
            Row(
                Modifier.fillMaxWidth().padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (inDetail) {
                    IconButton(onClick = { selected = null }) {
                        Icon(
                            AppIcons.back, null, tint = p.text,
                            modifier = Modifier.graphicsLayer(scaleX = if (ar) -1f else 1f)
                        )
                    }
                } else {
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    if (inDetail) detailTitle else t("mylist"),
                    color = p.text, fontSize = 20.sp, fontWeight = FontWeight.Black,
                    modifier = Modifier.weight(1f)
                )
                Pill(t("offline"), offlineOnly, { offlineOnly = !offlineOnly })
            }
        }
        if (inDetail) {
            if (detailList.isEmpty()) {
                item { EmptyState(Modifier.padding(top = 24.dp)) }
            } else {
                animeGrid(detailList, gc, onOpen, { sheet = it })
            }
        } else {
            items(systemStatuses.chunked(2)) { row ->
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { s ->
                        val count = Seed.anime.count { watchlist[it.id] == s && keep(it) }
                        ListTile(t(s.statusKey()), count, s.gradA(), s.gradB(), Modifier.weight(1f)) {
                            selected = "s:" + s.name
                        }
                    }
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        t("collections"), color = p.text, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        text = t("newCollection"),
                        onClick = { creating = !creating },
                        icon = AppIcons.add,
                        filled = false
                    )
                }
            }
            if (creating) {
                item {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HiyakoTextField(
                            value = draft,
                            onValueChange = { draft = it },
                            hint = t("newCollection"),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        PrimaryButton(
                            text = t("create"),
                            onClick = {
                                val n = draft.trim()
                                if (n.isNotEmpty()) {
                                    Store.addCollection(n)
                                    draft = ""
                                    creating = false
                                }
                            }
                        )
                    }
                }
            }
            if (colls.isEmpty()) {
                item {
                    Text(
                        t("emptySub"), color = p.textDim, fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
            items(colls, key = { it.id }) { c ->
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp)).background(p.surface)
                        .clickable { selected = "c:" + c.id }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(AppIcons.folder, null, tint = p.accent, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(c.name, color = p.text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                        Text("${c.ids.size}", color = p.textDim, fontSize = 11.sp)
                    }
                    Icon(
                        AppIcons.delete, null, tint = p.textDim,
                        modifier = Modifier.size(22.dp).clickable { Store.deleteCollection(c) }
                    )
                }
            }
        }
    }
    sheet?.let { QuickSheet(it, onDismiss = { sheet = null }, onOpen = onOpen) }
}

@Composable
private fun ListTile(title: String, count: Int, a: Long, b: Long, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier.height(88.dp).clip(RoundedCornerShape(14.dp))
            .background(Brush.linearGradient(listOf(Color(a), Color(b))))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Text(
            "$count", color = Color.White.copy(alpha = 0.3f), fontSize = 40.sp, fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        Text(
            title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}
