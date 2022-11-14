package dev.pauldavies.katz.repository

import dev.pauldavies.katz.domain.Breed
import dev.pauldavies.katz.domain.toBreed
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
            val serviceValue = serviceResult.getOrNull()?.map { it.toBreed() }
            if (serviceResult.isSuccess && serviceValue != null) {
                emit(Result.success(serviceValue))
                breedCache.cacheBreeds(serviceValue)
            } else if (serviceResult.isFailure && cacheResult.isFailure) {
                emit(Result.failure(serviceResult.exceptionOrNull() ?: Exception()))
            }
        }
    }

}
