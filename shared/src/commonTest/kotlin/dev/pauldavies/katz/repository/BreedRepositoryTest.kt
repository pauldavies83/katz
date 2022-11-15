package dev.pauldavies.katz.repository

import dev.pauldavies.katz.cache.BreedCache
import dev.pauldavies.katz.domain.toBreed
import dev.pauldavies.katz.repository.KatzImageServiceFake.ServiceBreedCreator
import dev.pauldavies.katz.service.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BreedRepositoryTest {

    private val fakeImageService = KatzImageServiceFake()
    private val breedCache = BreedCache()
    private val repository = BreedRepository(
        service = fakeImageService,
        cache = breedCache
    )

    @Test
    fun when_cache_empty_and_network_failure_only_network_emits() = runTest {
        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 1)
        assertTrue(testResult[0].isFailure)
    }

    @Test
    fun when_cache_empty_and_network_success_only_network_emits() = runTest {
        fakeImageService.breeds = Result.success(
            listOf(ServiceBreedCreator.breed())
        )

        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 1)
        assertTrue(testResult[0].isSuccess)
    }

    @Test
    fun when_cache_not_empty_and_network_success_cache_and_network_emits() = runTest {
        val serviceBreed1 = ServiceBreedCreator.breed()
        val serviceBreed2 = ServiceBreedCreator.breed()

        breedCache.cacheBreeds(listOf(serviceBreed1.toBreed()))
        fakeImageService.breeds = Result.success(listOf(serviceBreed2))

        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 2)
        assertTrue(testResult[0].isSuccess)
        assertEquals(testResult[0].getOrDefault(emptyList())[0], serviceBreed1.toBreed())
        assertTrue(testResult[1].isSuccess)
        assertEquals(testResult[1].getOrDefault(emptyList())[0], serviceBreed2.toBreed())
    }

    @Test
    fun when_cache_not_empty_and_network_failure_only_cache_emits() = runTest {
        val serviceBreed = ServiceBreedCreator.breed()

        breedCache.cacheBreeds(listOf(serviceBreed.toBreed()))
        fakeImageService.breeds = Result.failure(Exception())

        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 1)
        assertTrue(testResult[0].isSuccess)
        assertEquals(testResult[0].getOrDefault(emptyList())[0], serviceBreed.toBreed())
    }
}

private fun randomString() = Random.nextFloat().toString()

private class KatzImageServiceFake: KatzImageService {

    object ServiceBreedCreator {
        fun breed() = KatzImageService.Breed(
            id = randomString(),
            description = randomString(),
            name = randomString(),
            origin = randomString()
        )
    }

    var images: Result<List<KatzImageService.Image>> = Result.failure(Exception())
    var breeds: Result<List<KatzImageService.Breed>> = Result.failure(Exception())

    override suspend fun images(breedId: String?): Result<List<KatzImageService.Image>> = images
    override suspend fun breeds(): Result<List<KatzImageService.Breed>> = breeds
}
