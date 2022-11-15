package dev.pauldavies.katz.repository

import dev.pauldavies.katz.cache.ImagesCache
import dev.pauldavies.katz.cache.cacheThenNetwork
import dev.pauldavies.katz.domain.Image
import dev.pauldavies.katz.domain.toImage
import dev.pauldavies.katz.service.KatzImageService
import kotlinx.coroutines.flow.Flow

class KatImageRepository(
    private val service: KatzImageService,
    private val cache: ImagesCache
) {

    suspend fun images(breedId: String? = null): Flow<Result<List<Image>>> {
        return cacheThenNetwork(
            getCacheCall = { cache.images(breedId) },
            putCacheCall = { mappedServiceResult -> cache.cacheImages(breedId, mappedServiceResult) },
            networkCall = { service.images(breedId) },
            networkToDomainMapping = { serviceResult ->
                serviceResult.map { it.toImage() }
            }
        )
    }

}
