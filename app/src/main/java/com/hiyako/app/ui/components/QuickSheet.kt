package com.hiyako.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.Store
import com.hiyako.app.data.genreLabel
import com.hiyako.app.data.t
import com.hiyako.app.ui.theme.LocalPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickSheet(a: Anime, onDismiss: () -> Unit, onOpen: (String) -> Unit) {
    val p = LocalPalette.current
    val ar = isArabic()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val watchlist by Store.watchlist.collectAsState()
    val cols by Store.collections.collectAsState()
    val current = watchlist[a.id]
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = p.surface,
        contentColor = p.text
    ) {
        Column(
            Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp).padding(bottom = 24.dp)
        ) {
            Row {
                PosterArt(
                    a,
                    Modifier.width(96.dp).aspectRatio(130f / 180f).clickable {
                        onOpen(a.id)
                        onDismiss()
                    }
                )
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(a.title, color = p.text, fontSize = 17.sp, fontWeight = FontWeight.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(a.jp, color = p.textDim, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(8.dp))
                    RatingRow(a)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        a.genres.joinToString(" · ") { genreLabel(it, ar) },
                        color = p.textDim, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(6.dp))
                    StatusBadge(a.status, true)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                if (ar) a.synopsisAr else a.synopsisEn,
                color = p.textDim, fontSize = 13.sp, lineHeight = 20.sp,
                maxLines = 4, overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(14.dp))
            PrimaryButton(
                text = t("details"),
                onClick = {
                    onOpen(a.id)
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                icon = AppIcons.info
            )
            Spacer(Modifier.height(14.dp))
            Text(t("addList"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            StatusPicker(current) { Store.setStatus(a.id, it) }
            if (cols.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                Text(t("collections"), color = p.text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    cols.forEach { c ->
                        Pill(c.name, a.id in c.ids, { Store.toggleInCollection(c.id, a.id) })
                        Spacer(Modifier.width(8.dp))
                    }
                }
            }
        }
    }
}
