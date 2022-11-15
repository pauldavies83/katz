package dev.pauldavies.katz.cache

import dev.pauldavies.katz.domain.Image

class ImagesCache {
    private var images: MutableMap<String?, List<Image>> = mutableMapOf()

    fun images(breedId: String?): Result<List<Image>> {
        return images[breedId]?.let {
            Result.success(it)
        } ?: Result.failure(CacheMissException())
    }

    fun cacheImages(breedId: String?, images: List<Image>) {
        this.images[breedId] = images
    }
}
