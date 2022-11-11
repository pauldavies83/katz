package dev.pauldavies.katz.android.screen.katzlist

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import dev.pauldavies.katz.android.app.KatzTheme
import org.koin.androidx.compose.getViewModel

@Composable
internal fun KatzListScreen(viewModel: KatzListViewModel = getViewModel()) {
    val state = viewModel.state.collectAsState()
    KatzList(katz = state.value)
}

@Composable
fun KatzList(katz: List<String>) {
    katz.forEach {
        Text(text = it)
    }
}

@Preview
@Composable
fun KatzListPreview() {
    KatzTheme {
        KatzList(listOf("Hello, Katz!"))
    }
}
