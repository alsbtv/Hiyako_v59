package com.hiyako.app.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hiyako.app.ui.theme.LocalPalette
import com.hiyako.app.ui.theme.grad
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val p = LocalPalette.current
    val tr = rememberInfiniteTransition(label = "pulse")
    val pulse by tr.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pulseValue"
    )
    var shown by remember { mutableStateOf(false) }
    val a by animateFloatAsState(if (shown) 1f else 0f, tween(700), label = "splashAlpha")
    LaunchedEffect(Unit) {
        shown = true
        delay(2000)
        onFinished()
    }
    Box(Modifier.fillMaxSize().background(p.bg), contentAlignment = Alignment.Center) {
        Box(
            Modifier.size(280.dp).scale(pulse).alpha(a)
                .background(Brush.radialGradient(listOf(p.accent.copy(alpha = 0.38f), Color.Transparent)), CircleShape)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.alpha(a)) {
            Box(
                Modifier.size(88.dp).scale(pulse * 0.92f).clip(RoundedCornerShape(26.dp)).background(grad(p.accentL, p.accent2L)),
                contentAlignment = Alignment.Center
            ) {
                Text("H", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Black)
            }
            Spacer(Modifier.height(18.dp))
            Text("Hiyako", color = p.text, fontSize = 34.sp, fontWeight = FontWeight.Black)
            Spacer(Modifier.height(4.dp))
            Text("ANIME · ANYWHERE", color = p.textDim, fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 3.sp)
        }
    }
}
