package dev.pauldavies.katz.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import dev.pauldavies.katz.android.screen.katzlist.KatzListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KatzTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    KatzListScreen()
                }
            }
        }
    }
}
