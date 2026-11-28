package com.hiyako.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Seed
import com.hiyako.app.data.Store
import com.hiyako.app.data.WatchStatus
import com.hiyako.app.data.t
import com.hiyako.app.ui.components.AppIcons
import com.hiyako.app.ui.components.AvatarCircle
import com.hiyako.app.ui.components.CoverArt
import com.hiyako.app.ui.components.EmptyState
import com.hiyako.app.ui.components.GlassSurface
import com.hiyako.app.ui.components.HiyakoTextField
import com.hiyako.app.ui.components.HistoryRow
import com.hiyako.app.ui.components.Pill
import com.hiyako.app.ui.components.PrimaryButton
import com.hiyako.app.ui.components.SectionHeader
import com.hiyako.app.ui.components.gradA
import com.hiyako.app.ui.components.isArabic
import com.hiyako.app.ui.components.onAccentColor
import com.hiyako.app.ui.components.statusKey
import com.hiyako.app.ui.components.systemStatuses
import com.hiyako.app.ui.theme.LocalPalette
import com.hiyako.app.ui.theme.Palettes
import com.hiyako.app.ui.theme.grad

@Composable
fun ProfileScreen(onOpen: (String) -> Unit) {
    val p = LocalPalette.current
    val ar = isArabic()
    val lang by Store.language.collectAsState()
    val paletteId by Store.paletteId.collectAsState()
    val name by Store.profileName.collectAsState()
    val avatarIdx by Store.avatarIdx.collectAsState()
    val coverIdx by Store.coverIdx.collectAsState()
    val watchlist by Store.watchlist.collectAsState()
    val history by Store.history.collectAsState()
    val offline by Store.offline.collectAsState()
    val toggles by Store.toggles.collectAsState()
    var editing by remember { mutableStateOf(false) }
    var draft by remember(name) { mutableStateOf(name) }
    val counts = systemStatuses.map { s -> s to watchlist.values.count { it == s } }
    val watched = history.count { it.durSec > 0 && it.posSec * 100 / it.durSec >= 90 }
    val storageMb = offline.sumOf { it.sizeMb }
    val prefs = listOf("continue" to "showContinue", "thumbs" to "thumbs", "notifyAll" to "notifyAll", "adult" to "adult")

    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Box(Modifier.fillMaxWidth().height(210.dp)) {
                CoverArt(coverIdx, Modifier.fillMaxSize())
                Box(
                    Modifier.fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.35f), p.bg)))
                )
                Box(
                    Modifier.align(Alignment.TopEnd).padding(12.dp).size(40.dp).clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f)).clickable { editing = !editing },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (editing) AppIcons.check else AppIcons.edit, null,
                        tint = Color.White, modifier = Modifier.size(20.dp)
                    )
                }
                Row(
                    Modifier.align(Alignment.BottomStart).padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarCircle(avatarIdx, 76.dp)
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(name, color = p.text, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Text("@$name", color = p.textDim, fontSize = 13.sp)
                    }
                }
            }
        }
        if (editing) {
            item {
                Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    HiyakoTextField(
                        value = draft,
                        onValueChange = { draft = it },
                        hint = t("name"),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(t("avatar"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(Seed.characters.size) { i ->
                            AvatarCircle(
                                i, 52.dp,
                                Modifier.then(if (i == avatarIdx) Modifier.border(3.dp, p.accent, CircleShape) else Modifier)
                                    .clickable { Store.setAvatar(i) }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(t("cover"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(Seed.anime.size) { i ->
                            CoverArt(
                                i,
                                Modifier.size(96.dp, 56.dp).clip(RoundedCornerShape(10.dp))
                                    .then(if (i == coverIdx) Modifier.border(3.dp, p.accent, RoundedCornerShape(10.dp)) else Modifier)
                                    .clickable { Store.setCover(i) }
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    PrimaryButton(
                        text = t("save"),
                        onClick = {
                            val n = draft.trim()
                            if (n.isNotEmpty()) Store.setName(n)
                            editing = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        icon = AppIcons.check
                    )
                }
            }
        }
        item {
            GlassSurface(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                Column {
                    Text(t("stats"), color = p.text, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(112.dp), contentAlignment = Alignment.Center) {
                            StatusPie(counts, Modifier.fillMaxSize())
                            Text(
                                "${counts.sumOf { it.second }}", color = p.text, fontSize = 22.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            counts.forEach { (s, n) ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(10.dp).clip(CircleShape).background(Color(s.gradA())))
                                    Spacer(Modifier.width(8.dp))
                                    Text(t(s.statusKey()), color = p.textDim, fontSize = 12.sp, modifier = Modifier.weight(1f))
                                    Text("$n", color = p.text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatBox(t("episodesWatched"), "$watched", Modifier.weight(1f))
                        StatBox(t("storage"), "$storageMb MB", Modifier.weight(1f))
                    }
                }
            }
        }
        item { SectionHeader(t("theme")) }
        items(Palettes.chunked(2)) { row ->
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { pal ->
                    val selected = pal.id == paletteId
                    Box(
                        Modifier.weight(1f).height(60.dp).clip(RoundedCornerShape(12.dp))
                            .background(grad(pal.accentL, pal.accent2L))
                            .then(if (selected) Modifier.border(3.dp, p.text, RoundedCornerShape(12.dp)) else Modifier)
                            .clickable { Store.setPalette(pal.id) }
                            .padding(10.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            if (ar) pal.nameAr else pal.nameEn, color = Color.White, fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                repeat(2 - row.size) { Spacer(Modifier.weight(1f)) }
            }
        }
        item { SectionHeader(t("language")) }
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Pill(t("arabic"), lang == "ar", { Store.setLanguage("ar") }, Modifier.weight(1f))
                Pill(t("english"), lang == "en", { Store.setLanguage("en") }, Modifier.weight(1f))
            }
        }
        item { SectionHeader(t("settings")) }
        item {
            Column {
                prefs.forEach { (key, label) ->
                    ToggleRow(t(label), toggles[key] ?: false) { Store.setToggle(key, it) }
                }
            }
        }
        item { SectionHeader(t("history")) }
        if (history.isEmpty()) {
            item { EmptyState() }
        } else {
            items(history.take(20), key = { "${it.animeId}:${it.ep}" }) { e ->
                HistoryRow(e) { onOpen(e.animeId) }
            }
        }
        item {
            Text(
                "Hiyako · ${t("version")} 2.2.0", color = p.textDim, fontSize = 11.sp,
                modifier = Modifier.fillMaxWidth().padding(24.dp)
            )
        }
    }
}

@Composable
private fun StatusPie(counts: List<Pair<WatchStatus, Int>>, modifier: Modifier = Modifier) {
    val track = LocalPalette.current.textDim.copy(alpha = 0.25f)
    val total = counts.sumOf { it.second }
    Canvas(modifier) {
        val stroke = 28f
        val topLeft = Offset(stroke / 2f, stroke / 2f)
        val arcSize = Size(size.width - stroke, size.height - stroke)
        if (total == 0) {
            drawArc(
                color = track, startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = topLeft, size = arcSize, style = Stroke(width = stroke)
            )
        } else {
            var start = -90f
            counts.forEach { (s, n) ->
                if (n > 0) {
                    val sweep = 360f * n / total
                    drawArc(
                        color = Color(s.gradA()), startAngle = start, sweepAngle = sweep, useCenter = false,
                        topLeft = topLeft, size = arcSize, style = Stroke(width = stroke)
                    )
                    start += sweep
                }
            }
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Column(modifier.clip(RoundedCornerShape(12.dp)).background(p.bg.copy(alpha = 0.5f)).padding(12.dp)) {
        Text(value, color = p.text, fontSize = 18.sp, fontWeight = FontWeight.Black)
        Text(label, color = p.textDim, fontSize = 11.sp, maxLines = 1)
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    val p = LocalPalette.current
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = p.text, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedTrackColor = p.accent,
                checkedThumbColor = onAccentColor(),
                uncheckedTrackColor = p.surface,
                uncheckedThumbColor = p.textDim,
                uncheckedBorderColor = p.textDim.copy(alpha = 0.4f)
            )
        )
    }
}
