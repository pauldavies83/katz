package dev.pauldavies.katz.di

import dev.pauldavies.katz.viewModel.KatzListSharedViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal fun sharedModule() = module {
    factoryOf(::KatzListSharedViewModel)
}
