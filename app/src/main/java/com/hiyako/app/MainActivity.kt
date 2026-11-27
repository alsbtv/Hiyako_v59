package com.hiyako.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.hiyako.app.ui.MainScreen
import com.hiyako.app.ui.splash.SplashScreen
import com.hiyako.app.ui.theme.DarkBg
import com.hiyako.app.ui.theme.HiyakoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HiyakoTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = DarkBg) {
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
