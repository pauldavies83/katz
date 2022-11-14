package dev.pauldavies.katz.di

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal actual fun platformModule() = module {
    singleOf<HttpClientEngineFactory<HttpClientEngineConfig>> { OkHttp }

    factoryOf<CoroutineScope> { CoroutineScope(Dispatchers.IO) }
}
