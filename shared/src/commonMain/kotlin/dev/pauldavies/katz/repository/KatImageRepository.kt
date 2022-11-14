package dev.pauldavies.katz.repository

import dev.pauldavies.katz.domain.Image
import dev.pauldavies.katz.domain.toImage
import dev.pauldavies.katz.service.KatzImageService

class KatImageRepository(private val service: KatzImageService) {

    suspend fun images(breedId: String? = null): Result<List<Image>> {
        val result = service.images(breedId)
        return if (result.isFailure) {
            Result.failure(result.exceptionOrNull() ?: Exception())
        } else {
            Result.success(result.getOrDefault(emptyList()).map { it.toImage() })
        }
    }

}
