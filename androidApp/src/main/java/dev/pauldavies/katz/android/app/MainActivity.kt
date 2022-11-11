package dev.pauldavies.katz.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.pauldavies.katz.android.screen.katzlist.KatzListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KatzTheme {
                KatzListScreen()
            }
        }
    }
}
