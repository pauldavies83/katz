package dev.pauldavies.katz.android.app.di

import android.content.Context
import coil.ImageLoader
import coil.memory.MemoryCache
import dev.pauldavies.katz.android.screen.katzlist.KatzListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun androidAppModule() = module {
    singleOf<ImageLoader, Context> {
        ImageLoader.Builder(it)
            .memoryCache { MemoryCache.Builder(it).maxSizePercent(0.25).build() }
            .crossfade(true)
            .build()
    }

    viewModelOf(::KatzListViewModel)
}
