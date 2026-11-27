package com.hiyako.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.ui.theme.LocalPalette
import com.hiyako.app.ui.theme.grad
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HeroCarousel(items: List<Anime>, onOpen: (Anime) -> Unit) {
    if (items.isEmpty()) return
    val p = LocalPalette.current
    val scope = rememberCoroutineScope()
    val state = rememberPagerState(pageCount = { items.size })
    LaunchedEffect(items.size) {
        while (true) {
            delay(11000)
            state.animateScrollToPage((state.currentPage + 1) % items.size)
        }
    }
    Column {
        HorizontalPager(
            state = state,
            contentPadding = PaddingValues(horizontal = 26.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth().height(190.dp)
        ) { page ->
            val a = items[page]
            Box(Modifier.fillMaxSize().clip(RoundedCornerShape(18.dp)).background(grad(a.colorA, a.colorB)).clickable { onOpen(a) }) {
                Text(a.motif, color = Color.White.copy(alpha = 0.14f), fontSize = 84.sp, fontWeight = FontWeight.Black, modifier = Modifier.align(Alignment.TopEnd).padding(12.dp))
                Box(Modifier.align(Alignment.BottomStart).fillMaxWidth().height(96.dp).background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)))))
                Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                    Text(a.title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, maxLines = 1)
                    Text("${a.type} · ${a.year}", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                Box(Modifier.align(Alignment.TopStart).padding(12.dp).clip(RoundedCornerShape(8.dp)).background(p.accent).padding(horizontal = 8.dp, vertical = 3.dp)) {
                    Text(a.status, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(items.size) { i ->
                val active = state.currentPage == i
                Box(
                    Modifier.padding(horizontal = 3.dp).height(6.dp).width(if (active) 20.dp else 6.dp)
                        .clip(CircleShape).background(if (active) p.accent else p.textDim.copy(alpha = 0.4f))
                        .clickable { scope.launch { state.animateScrollToPage(i) } }
                )
            }
        }
    }
}
