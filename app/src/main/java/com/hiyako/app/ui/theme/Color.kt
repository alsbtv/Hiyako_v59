package com.hiyako.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class Palette(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val bg: Color,
    val surface: Color,
    val drawer: Color,
    val accent: Color,
    val accent2: Color,
    val text: Color,
    val textDim: Color,
    val ok: Color,
    val warn: Color,
    val danger: Color,
    val bgL: Long,
    val surfaceL: Long,
    val accentL: Long,
    val accent2L: Long
)

val Palettes = listOf(
    Palette("hiyako", "نيون هياكو", "Hiyako Neon", Color(0xFF0F1115), Color(0xFF1E1E1E), Color(0xFF0A0A0A), Color(0xFFE50046), Color(0xFF4FC3F7), Color(0xFFFFFFFF), Color(0xFF8899A6), Color(0xFF4CAF50), Color(0xFFFFB300), Color(0xFFFF5252), 0xFF0F1115, 0xFF1E1E1E, 0xFFE50046, 0xFF4FC3F7),
    Palette("sakura", "ساكورا ليل", "Sakura Night", Color(0xFF140F16), Color(0xFF241A26), Color(0xFF0D0A0E), Color(0xFFFF4D8D), Color(0xFFB388FF), Color(0xFFFDF7FF), Color(0xFFA893B2), Color(0xFF69F0AE), Color(0xFFFFD54F), Color(0xFFFF6E6E), 0xFF140F16, 0xFF241A26, 0xFFFF4D8D, 0xFFB388FF),
    Palette("ocean", "عمق المحيط", "Ocean Depth", Color(0xFF08131A), Color(0xFF12222D), Color(0xFF050D12), Color(0xFF00E5FF), Color(0xFF7C4DFF), Color(0xFFF2FBFF), Color(0xFF7FA3B5), Color(0xFF69F0AE), Color(0xFFFFC400), Color(0xFFFF5C7A), 0xFF08131A, 0xFF12222D, 0xFF00E5FF, 0xFF7C4DFF),
    Palette("ember", "جمرة الغروب", "Ember Dusk", Color(0xFF170F0B), Color(0xFF2A1C14), Color(0xFF100A07), Color(0xFFFF6D00), Color(0xFFFFD740), Color(0xFFFFF8F2), Color(0xFFB59C8A), Color(0xFF9CCC65), Color(0xFFFFAB40), Color(0xFFFF5252), 0xFF170F0B, 0xFF2A1C14, 0xFFFF6D00, 0xFFFFD740),
    Palette("mint", "نعناع الليل", "Night Mint", Color(0xFF0B1512), Color(0xFF16241F), Color(0xFF071009), Color(0xFF00E676), Color(0xFF18FFFF), Color(0xFFF4FFF9), Color(0xFF8AA79B), Color(0xFF69F0AE), Color(0xFFFFD740), Color(0xFFFF6E6E), 0xFF0B1512, 0xFF16241F, 0xFF00E676, 0xFF18FFFF)
)

fun grad(a: Long, b: Long): Brush = Brush.linearGradient(listOf(Color(a), Color(b)))

val DarkBg = Color(0xFF0F1115)
val CardSurface = Color(0xFF1E1E1E)
val DrawerBg = Color(0xFF0A0A0A)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF8899A6)
val AccentRed = Color(0xFFE50046)
val AccentGreen = Color(0xFF4CAF50)
val MalBlue = Color(0xFF2E51A2)
