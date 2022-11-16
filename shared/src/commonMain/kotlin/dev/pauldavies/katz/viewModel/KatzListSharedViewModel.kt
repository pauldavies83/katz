package dev.pauldavies.katz.viewModel

import dev.pauldavies.katz.domain.Breed
import dev.pauldavies.katz.repository.BreedRepository
import dev.pauldavies.katz.repository.KatImageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KatzListSharedViewModel internal constructor(
    private val breedRepository: BreedRepository,
    private val imageRepository: KatImageRepository,
    ioScope: CoroutineScope
) {
    val state = MutableStateFlow(State())
    private val _events = Channel<Event>()
    val events = _events.receiveAsFlow()

    init {
        ioScope.launch {
            breedRepository.breeds().collect { breedsResult ->
                if (breedsResult.isSuccess) {
                    breedsResult.getOrNull()?.map { breed ->
                        breed.toBreedListItem(
                            selectedBreedId = state.value.selectedBreedId,
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
                        state.update { it.copy(breeds = this) }
                    }
                } else {
                    _events.send(Event.ShowError(breedsResult.exceptionOrNull()?.message))
                }
            }
        }

        ioScope.launch {
            state
                .map { it.selectedBreedId }
                .distinctUntilChanged()
                .onEach { selectedBreedId ->
                    state.update {
                        it.copy(
                            breeds = it.breeds?.map { item ->
                                item.copy(selected = item.id == selectedBreedId)
                            },
                            kats = null
                        )
                    }
                }
                .flatMapLatest { breedId -> imageRepository.images(breedId) }
                .collect { imagesResult ->
                    if (imagesResult.isSuccess && imagesResult.getOrNull() != null) {
                        state.update { state ->
                            state.copy(kats = imagesResult.getOrNull()!!.map { it.url })
                        }
                    } else {
                        _events.send(Event.ShowError(imagesResult.exceptionOrNull()?.message))
                    }
            }
        }
    }

    data class State(
        val selectedBreedId: String? = null,
        val breeds: List<BreedListItem>? = null,
        val kats: List<String>? = null
    ) {
        val title = breeds?.firstOrNull { it.selected }?.name
        val breedDetails = breeds?.firstOrNull { it.selected }?.details

        companion object {
            @Suppress("unused") // used by iOS
            fun initial() = State()
        }
    }

    sealed class Event {
        data class ShowError(val message: String?): Event()
    }
}

data class BreedListItem(
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

private fun Breed.toBreedListItem(
    selectedBreedId: String? = null,
    onClick: () -> Unit
) = BreedListItem(
    id = id,
    name = name,
    details = BreedListItem.Details(
        description = description,
        originCountry = origin
    ),
    selected = id == selectedBreedId,
    onClick = onClick
)
