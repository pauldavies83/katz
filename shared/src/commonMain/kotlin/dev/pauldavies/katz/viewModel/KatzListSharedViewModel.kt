package dev.pauldavies.katz.viewModel

import dev.pauldavies.katz.service.KatzImageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KatzListSharedViewModel(
    private val service: KatzImageService,
    ioScope: CoroutineScope
) {
    val state = MutableStateFlow(State())

    init {
        ioScope.launch {
            val breeds = service.breeds().getOrDefault(emptyList()).map { it.name }
            state.update {
                it.copy(breeds = breeds)
            }
        }
        ioScope.launch {
            val kats = service.images().getOrDefault(emptyList()).map { it.url }
            state.update {
                it.copy(kats = kats)
            }
        }
    }

    data class State(
        val breeds: List<String>? = null,
        val kats: List<String>? = null
    )
}
