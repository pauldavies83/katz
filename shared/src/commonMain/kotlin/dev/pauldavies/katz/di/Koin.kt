package dev.pauldavies.katz.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
    appModule: Module = module {},
) =
    startKoin {
        appDeclaration()
        modules(appModule + sharedModule() + platformModule())
    }
