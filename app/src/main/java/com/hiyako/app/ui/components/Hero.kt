package com.hiyako.app.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.genreLabel
import com.hiyako.app.ui.theme.LocalPalette
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarousel(
    items: List<Anime>,
    onTap: (Anime) -> Unit,
    onMore: (Anime) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) return
    val p = LocalPalette.current
    val ar = isArabic()
    val scope = rememberCoroutineScope()
    val state = rememberPagerState(pageCount = { items.size })
    val dragged by state.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(state.currentPage, dragged, items.size) {
        if (!dragged) {
            delay(11000)
            state.animateScrollToPage((state.currentPage + 1) % items.size)
        }
    }
    Column(modifier) {
        HorizontalPager(
            state = state,
            contentPadding = PaddingValues(horizontal = 26.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth().height(200.dp)
        ) { page ->
            val a = items[page]
            Box(
                Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .clickable { onTap(a) }
            ) {
                AnimeBackdrop(a, Modifier.fillMaxSize())
                Text(
                    a.motif,
                    color = Color.White.copy(alpha = 0.14f),
                    fontSize = 90.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
                )
                Box(
                    Modifier.align(Alignment.BottomStart).fillMaxWidth().height(110.dp)
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))))
                )
                Row(
                    Modifier.align(Alignment.TopStart).padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    StatusBadge(a.status)
                    if (a.adult) {
                        Text(
                            "+17", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold,
                            modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(p.danger)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Box(
                    Modifier.align(Alignment.TopEnd).padding(10.dp).size(28.dp).clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.45f)).clickable { onMore(a) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(AppIcons.more, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                    Text(
                        a.title, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Black,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "${a.type} · ${a.year} · " + a.genres.take(2).joinToString(" · ") { genreLabel(it, ar) },
                        color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(6.dp))
                    RatingRow(a)
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
