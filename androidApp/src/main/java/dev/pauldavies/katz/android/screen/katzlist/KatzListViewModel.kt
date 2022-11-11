package dev.pauldavies.katz.android.screen.katzlist

import androidx.lifecycle.ViewModel
import dev.pauldavies.katz.viewModel.KatzListSharedViewModel
import kotlinx.coroutines.flow.StateFlow

internal class KatzListViewModel(sharedViewModel: KatzListSharedViewModel) : ViewModel() {
    val state: StateFlow<List<String>> = sharedViewModel.state
}
