package dev.pauldavies.katz.viewModel

import dev.pauldavies.katz.repository.BreedRepository
import dev.pauldavies.katz.service.KatzImageService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KatzListSharedViewModel internal constructor(
    private val breedRepository: BreedRepository,
    private val service: KatzImageService,
    ioScope: CoroutineScope
) {
    val state = MutableStateFlow(State())

    init {
        ioScope.launch {
            breedRepository.breeds().collect { breedsResult ->
                if (breedsResult.isSuccess) {
                    breedsResult.getOrDefault(emptyList()).map { breed ->
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
                    }.apply {
                        state.update {
                            it.copy(breeds = this)
                        }
                    }
                }
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
        val title = breeds?.firstOrNull { it.selected }?.name
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
