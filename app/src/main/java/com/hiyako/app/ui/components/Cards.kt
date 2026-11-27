package com.hiyako.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.t
import com.hiyako.app.ui.theme.LocalPalette
import com.hiyako.app.ui.theme.grad

@Composable
fun PosterArt(a: Anime, modifier: Modifier = Modifier, variant: Int = 0) {
    val p = LocalPalette.current
    Box(modifier.clip(RoundedCornerShape(12.dp)).background(grad(a.colorA, a.colorB))) {
        Text(
            a.motif,
            color = Color.White.copy(alpha = 0.16f),
            fontSize = 44.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
        )
        Box(
            Modifier.align(Alignment.BottomStart).fillMaxWidth().height(56.dp)
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.72f))))
        )
        Column(Modifier.align(Alignment.BottomStart).padding(8.dp)) {
            Text(a.title, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 14.sp)
            Text("${a.type} · ${a.year}", color = Color.White.copy(alpha = 0.7f), fontSize = 9.sp)
        }
        if (variant == 1) Box(Modifier.matchParentSize().background(Brush.radialGradient(listOf(Color.White.copy(alpha = 0.14f), Color.Transparent))))
        if (variant == 2) Box(Modifier.matchParentSize().border(1.dp, Brush.linearGradient(listOf(p.accent.copy(alpha = 0.6f), Color.Transparent)), RoundedCornerShape(12.dp)))
    }
}

@Composable
fun StatusBadge(status: String, small: Boolean = false, modifier: Modifier = Modifier) {
    val (bg, label) = when (status) {
        "On Going" -> Color(0xFF2E7D32) to t("airing")
        "Completed" -> Color(0xFFC62828) to t("finished")
        "Upcoming" -> Color(0xFF1565C0) to t("upcomingSt")
        else -> Color(0xFF6D4C41) to t("hiatus")
    }
    Text(
        label, color = Color.White, fontSize = if (small) 8.sp else 9.sp, fontWeight = FontWeight.Bold,
        modifier = modifier.clip(RoundedCornerShape(6.dp)).background(bg).padding(horizontal = 6.dp, vertical = 2.dp)
    )
}

@Composable
fun AnimeCard(a: Anime, onClick: () -> Unit, onMore: () -> Unit, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Column(modifier.width(130.dp).clickable(onClick = onClick)) {
        Box(Modifier.fillMaxWidth().aspectRatio(130f / 180f)) {
            PosterArt(a, Modifier.matchParentSize())
            StatusBadge(a.status, true, Modifier.align(Alignment.TopStart).padding(6.dp))
            if (a.adult) Text(
                "+17", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopEnd).padding(6.dp).clip(RoundedCornerShape(6.dp)).background(p.danger).padding(horizontal = 5.dp, vertical = 2.dp)
            )
            Box(
                Modifier.align(Alignment.BottomEnd).padding(6.dp).size(22.dp).clip(RoundedCornerShape(6.dp))
                    .background(Color.Black.copy(alpha = 0.6f)).clickable(onClick = onMore),
                contentAlignment = Alignment.Center
            ) { Icon(AppIcons.more, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
        }
        Spacer(Modifier.height(6.dp))
        Text(a.title, color = p.text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(AppIcons.star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(3.dp))
            Text("${a.mal}", color = p.textDim, fontSize = 10.sp)
            Spacer(Modifier.width(4.dp))
            Text("(${a.votes / 1000}K)", color = p.textDim.copy(alpha = 0.7f), fontSize = 9.sp)
        }
    }
}

@Composable
fun ContinueCard(a: Anime, ep: Int, total: Int, progress: Float, ago: String, onClick: () -> Unit) {
    val p = LocalPalette.current
    Column(Modifier.width(180.dp).clickable(onClick = onClick)) {
        Box(Modifier.fillMaxWidth().aspectRatio(16f / 9f).clip(RoundedCornerShape(10.dp)).background(grad(a.colorA, a.colorB))) {
            Text(a.motif, color = Color.White.copy(alpha = 0.2f), fontSize = 26.sp, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.Center))
            Text(
                "Episode $ep /$total", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopStart).padding(6.dp).clip(RoundedCornerShape(6.dp)).background(Color.Black.copy(alpha = 0.6f)).padding(horizontal = 6.dp, vertical = 2.dp)
            )
            Box(Modifier.align(Alignment.BottomStart).fillMaxWidth().height(3.dp).background(Color.White.copy(alpha = 0.25f))) {
                Box(Modifier.fillMaxHeight().fillMaxWidth(progress.coerceIn(0f, 1f)).background(p.ok))
            }
        }
        Spacer(Modifier.height(5.dp))
        Text(a.title, color = p.text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(ago, color = p.textDim, fontSize = 10.sp)
    }
}

@Composable
fun SectionHeader(title: String, onMore: (() -> Unit)? = null) {
    val p = LocalPalette.current
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.width(3.dp).height(16.dp).clip(CircleShape).background(p.accent))
        Spacer(Modifier.width(8.dp))
        Text(title, color = p.text, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        if (onMore != null) Text(t("viewAll"), color = p.accent, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable(onClick = onMore))
    }
}

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val tr = rememberInfiniteTransition(label = "sh")
    val x by tr.animateFloat(-400f, 900f, infiniteRepeatable(tween(1100, easing = LinearEasing), RepeatMode.Restart), label = "shx")
    Box(
        modifier.clip(RoundedCornerShape(12.dp)).background(
            Brush.linearGradient(listOf(Color(0xFF1A1D22), Color(0xFF2A2F36), Color(0xFF1A1D22)), start = Offset(x, 0f), end = Offset(x + 400f, 0f))
        )
    )
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    Column(modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(72.dp).clip(CircleShape).background(grad(p.accentL, p.accent2L)), contentAlignment = Alignment.Center) {
            Icon(AppIcons.tv, null, tint = Color.White, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.height(14.dp))
        Text(t("empty"), color = p.text, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(t("emptySub"), color = p.textDim, fontSize = 12.sp)
    }
}

@Composable
fun GlassSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val p = LocalPalette.current
    Box(
        modifier.clip(RoundedCornerShape(16.dp))
            .background(p.surface.copy(alpha = 0.72f))
            .border(1.dp, Brush.linearGradient(listOf(p.text.copy(alpha = 0.14f), p.accent.copy(alpha = 0.18f), Color.Transparent)), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) { content() }
}
