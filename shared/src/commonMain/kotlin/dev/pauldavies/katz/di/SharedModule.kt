package dev.pauldavies.katz.di

import dev.pauldavies.katz.service.BreedCache
import dev.pauldavies.katz.repository.BreedRepository
import dev.pauldavies.katz.repository.KatImageRepository
import dev.pauldavies.katz.service.KatzImageService
import dev.pauldavies.katz.service.KatzImageServiceNetwork
import dev.pauldavies.katz.viewModel.KatzListSharedViewModel
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal fun sharedModule() = module {
    singleOf { engine: HttpClientEngineFactory<HttpClientEngineConfig> ->
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                header("x-api-key", "live_XoPraPnCs6IHEUTw1beepu7eRzXrilG3qZaXVxrGMSmbTk867b4iWFo8ZFHkOQul")
            }
        }
    }

    singleOf(::KatzImageServiceNetwork) bind KatzImageService::class

    singleOf(::BreedCache)

    singleOf(::KatImageRepository)
    singleOf(::BreedRepository)

    factoryOf(::KatzListSharedViewModel)
}
