package dev.pauldavies.katz.viewModel

import dev.pauldavies.katz.service.KatzImageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class KatzListSharedViewModel(
    private val service: KatzImageService,
    ioScope: CoroutineScope
) {
    val state = MutableStateFlow(emptyList<String>())

    init {
        ioScope.launch {
            val katz = service.images()
            state.value = katz.getOrDefault(emptyList()).map { it.url }
        }
    }
}
