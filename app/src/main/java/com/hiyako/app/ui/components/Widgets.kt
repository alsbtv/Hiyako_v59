package com.hiyako.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.data.Anime
import com.hiyako.app.data.Seed
import com.hiyako.app.data.WatchStatus
import com.hiyako.app.data.t
import com.hiyako.app.ui.theme.LocalPalette
import com.hiyako.app.ui.theme.MalBlue

@Composable
fun onAccentColor(): Color {
    val p = LocalPalette.current
    return if (p.accent.luminance() > 0.4f) p.bg else Color.White
}

@Composable
fun Pill(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val p = LocalPalette.current
    val onAcc = onAccentColor()
    val shape = RoundedCornerShape(50)
    Box(
        modifier
            .clip(shape)
            .background(if (selected) p.accent else p.surface)
            .border(1.dp, if (selected) p.accent else p.textDim.copy(alpha = 0.3f), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) onAcc else p.text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

@Composable
fun RatingRow(a: Anime) {
    val p = LocalPalette.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Row(
            Modifier.clip(RoundedCornerShape(6.dp)).background(MalBlue).padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("MAL", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.width(4.dp))
            Text("${a.mal}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(8.dp))
        Icon(AppIcons.star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
        Spacer(Modifier.width(3.dp))
        Text("${a.al}", color = p.text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(4.dp))
        Text("(${fmtVotes(a.votes)})", color = p.textDim, fontSize = 10.sp)
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    filled: Boolean = true
) {
    val p = LocalPalette.current
    val onAcc = onAccentColor()
    val shape = RoundedCornerShape(12.dp)
    val base = if (filled) {
        modifier.clip(shape).background(p.accent)
    } else {
        modifier.clip(shape).background(p.surface).border(1.dp, p.textDim.copy(alpha = 0.3f), shape)
    }
    val fg = if (filled) onAcc else p.text
    Row(
        base.clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icon != null) {
            Icon(icon, null, tint = fg, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(text, color = fg, fontSize = 14.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
fun StatusPicker(current: WatchStatus?, onPick: (WatchStatus?) -> Unit) {
    val rows = systemStatuses.chunked(3)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { s ->
                    Pill(
                        text = t(s.statusKey()),
                        selected = current == s,
                        onClick = { onPick(if (current == s) null else s) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun AvatarCircle(idx: Int, size: Dp, modifier: Modifier = Modifier) {
    val c = Seed.characters[idx.mod(Seed.characters.size)]
    Box(
        modifier.size(size).clip(CircleShape).background(com.hiyako.app.ui.theme.grad(c.colorA, c.colorB)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            c.name.take(1).uppercase(),
            color = Color.White,
            fontSize = (size.value * 0.42f).sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun CoverArt(idx: Int, modifier: Modifier = Modifier) {
    val a = Seed.anime[idx.mod(Seed.anime.size)]
    Box(modifier) {
        AnimeBackdrop(a, Modifier.fillMaxSize())
        Text(
            a.motif,
            color = Color.White.copy(alpha = 0.15f),
            fontSize = 96.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.CenterEnd).padding(16.dp)
        )
    }
}

@Composable
fun HiyakoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    leading: ImageVector? = null,
    onSearch: (() -> Unit)? = null
) {
    val p = LocalPalette.current
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier.clip(shape).background(p.surface).border(1.dp, p.textDim.copy(alpha = 0.25f), shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            Icon(leading, null, tint = p.textDim, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
        }
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) Text(hint, color = p.textDim, fontSize = 14.sp)
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = TextStyle(color = p.text, fontSize = 14.sp),
                cursorBrush = SolidColor(p.accent),
                keyboardOptions = KeyboardOptions(imeAction = if (onSearch != null) ImeAction.Search else ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch?.invoke() },
                    onDone = { onSearch?.invoke() }
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (value.isNotEmpty()) {
            Spacer(Modifier.width(8.dp))
            Icon(
                AppIcons.close, null, tint = p.textDim,
                modifier = Modifier.size(18.dp).clickable { onValueChange("") }
            )
        }
    }
}
