package dev.pauldavies.katz.repository

import dev.pauldavies.katz.service.Breed
import dev.pauldavies.katz.service.BreedCache
import dev.pauldavies.katz.service.KatzImageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BreedRepository(
    private val service: KatzImageService,
    private val breedCache: BreedCache
) {

    fun breeds(): Flow<Result<List<Breed>>> {
        return flow {
            val cacheResult = breedCache.breeds()
            if (cacheResult.isSuccess) emit(cacheResult)

            val serviceResult = service.breeds()
            if (serviceResult.isSuccess) {
                emit(serviceResult)
                breedCache.cacheBreeds(serviceResult.getOrDefault(emptyList()))
            } else if (serviceResult.isFailure && cacheResult.isFailure) {
                emit(serviceResult)
            }
        }
    }

}
