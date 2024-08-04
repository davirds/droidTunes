package com.davirdgs.tunes.ui

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.davirdgs.tunes.base.BaseActivity
import com.davirdgs.tunes.ui.navigation.Navigation
import com.davirdgs.tunes.ui.theme.MusicAIChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setWindowContent {
            Application {
                Navigation()
            }
        }
    }
}

@Composable
internal fun Application(
    content: @Composable () -> Unit
) {
    MusicAIChallengeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicationPreview() {
    Application {
        Navigation()
    }
}
