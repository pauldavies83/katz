package dev.pauldavies.katz.viewModel

import kotlinx.coroutines.flow.MutableStateFlow

class KatzListSharedViewModel {
    val state = MutableStateFlow(listOf("I'm a Kat"))
}
