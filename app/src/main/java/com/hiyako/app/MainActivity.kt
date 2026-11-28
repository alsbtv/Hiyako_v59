package com.hiyako.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.hiyako.app.data.Store
import com.hiyako.app.ui.MainScreen
import com.hiyako.app.ui.splash.SplashScreen
import com.hiyako.app.ui.theme.HiyakoTheme
import com.hiyako.app.ui.theme.LocalPalette
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiyakoTheme {
                val lang by Store.language.collectAsState()
                val dir = if (lang == "ar") LayoutDirection.Rtl else LayoutDirection.Ltr
                CompositionLocalProvider(LocalLayoutDirection provides dir) {
                    Box(Modifier.fillMaxSize().background(LocalPalette.current.bg)) {
                        var splashFinished by rememberSaveable { mutableStateOf(false) }
                        if (splashFinished) {
                            MainScreen()
                        } else {
                            SplashScreen(onFinished = { splashFinished = true })
                        }
                    }
                }
            }
        }
    }
}
