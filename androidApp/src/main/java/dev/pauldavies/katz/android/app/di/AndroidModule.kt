package dev.pauldavies.katz.android.app.di

import dev.pauldavies.katz.android.screen.katzlist.KatzListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

fun androidAppModule() = module {
    viewModelOf(::KatzListViewModel)
}
