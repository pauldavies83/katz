package dev.pauldavies.katz.android.screen.katzlist

import androidx.lifecycle.ViewModel
import dev.pauldavies.katz.viewModel.KatzListSharedViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal class KatzListViewModel(sharedViewModel: KatzListSharedViewModel) : ViewModel() {
    val state: StateFlow<KatzListSharedViewModel.State> = sharedViewModel.state
    val events: Flow<KatzListSharedViewModel.Event> = sharedViewModel.events
}
