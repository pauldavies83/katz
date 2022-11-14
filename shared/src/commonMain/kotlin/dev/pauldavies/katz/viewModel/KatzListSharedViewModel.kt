package dev.pauldavies.katz.viewModel

import dev.pauldavies.katz.service.KatzImageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KatzListSharedViewModel(
    private val service: KatzImageService,
    ioScope: CoroutineScope
) {
    val state = MutableStateFlow(State())

    init {
        ioScope.launch {
            val breeds = service.breeds().getOrDefault(emptyList()).map { breed ->
                BreedDrawerItem(
                    id = breed.id,
                    name = breed.name,
                    details = BreedDrawerItem.Details(
                        description = breed.description,
                        originCountry = breed.origin
                    ),
                    selected = breed.id == state.value.selectedBreedId,
                    onClick = {
                        state.update {
                            if (it.selectedBreedId == breed.id) {
                                it.copy(selectedBreedId = null)
                            } else {
                                it.copy(selectedBreedId = breed.id)
                            }
                        }
                    }
                )
            }
            state.update {
                it.copy(breeds = breeds)
            }
        }

        ioScope.launch {
            state
                .map { it.selectedBreedId }
                .distinctUntilChanged()
                .collect { selectedBreedId ->
                    state.update {
                        it.copy(
                            breeds = it.breeds?.map { item ->
                                item.copy(selected = item.id == selectedBreedId)
                            },
                            kats = null
                        )
                    }

                    val kats = service
                        .images(state.value.selectedBreedId)
                        .getOrDefault(emptyList())
                        .map { it.url }

                    state.update { it.copy(kats = kats) }
            }
        }
    }

    data class State(
        val selectedBreedId: String? = null,
        val breeds: List<BreedDrawerItem>? = null,
        val kats: List<String>? = null
    ) {
        val title: String = breeds?.firstOrNull { it.selected }?.name ?: "Katz"
        val topBarDetails = breeds?.firstOrNull { it.selected }?.details
    }
}

data class BreedDrawerItem(
    val id: String,
    val name: String,
    val details: Details,
    val selected: Boolean = false,
    val onClick: () -> Unit
) {
    data class Details(
        val description: String? = null,
        val originCountry: String? = null
    )
}
