package dev.pauldavies.katz.repository

import dev.pauldavies.katz.domain.Breed
import dev.pauldavies.katz.domain.toBreed
import dev.pauldavies.katz.cache.BreedCache
import dev.pauldavies.katz.cache.cacheThenNetwork
import dev.pauldavies.katz.service.KatzImageService
import kotlinx.coroutines.flow.Flow

class BreedRepository(
    private val service: KatzImageService,
    private val cache: BreedCache
) {

    fun breeds(): Flow<Result<List<Breed>>> {
        return cacheThenNetwork(
            getCacheCall = { cache.breeds() },
            putCacheCall = { mappedServiceResult -> cache.cacheBreeds(mappedServiceResult) },
            networkCall = { service.breeds() },
            networkToDomainMapping = { serviceResult ->
                serviceResult.map { it.toBreed() }
            }
        )
    }
}
