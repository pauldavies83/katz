package dev.pauldavies.katz.cache

import dev.pauldavies.katz.domain.Breed

class BreedCache {
    private var breeds: List<Breed>? = null

    fun breeds(): Result<List<Breed>> {
        return breeds?.let {
            Result.success(it)
        } ?: Result.failure(CacheMissException())
    }

    fun cacheBreeds(breeds: List<Breed>) {
        this.breeds = breeds
    }
}
