package dev.pauldavies.katz.repository

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
        breedCache = breedCache
    )

    @Test
    fun when_cache_empty_and_network_failure_only_network_emits() = runTest {
        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 1)
        assertTrue(testResult[0].isFailure)
    }

    @Test
    fun when_cache_empty_and_network_success_only_network_emits() = runTest {
        fakeImageService.breeds = Result.success(listOf(BreedCreator.breed()))

        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 1)
        assertTrue(testResult[0].isSuccess)
    }

    @Test
    fun when_cache_not_empty_and_network_success_cache_and_network_emits() = runTest {
        val breed1 = BreedCreator.breed()
        val breed2 = BreedCreator.breed()

        breedCache.cacheBreeds(listOf(breed1))
        fakeImageService.breeds = Result.success(listOf(breed2))

        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 2)
        assertTrue(testResult[0].isSuccess)
        assertEquals(testResult[0].getOrDefault(emptyList())[0], breed1)
        assertTrue(testResult[1].isSuccess)
        assertEquals(testResult[1].getOrDefault(emptyList())[0], breed2)
    }

    @Test
    fun when_cache_not_empty_and_network_failure_only_cache_emits() = runTest {
        val breed = BreedCreator.breed()

        breedCache.cacheBreeds(listOf(breed))
        fakeImageService.breeds = Result.failure(Exception())

        val testResult = repository.breeds().toList()

        assertEquals(testResult.size, 1)
        assertTrue(testResult[0].isSuccess)
        assertEquals(testResult[0].getOrDefault(emptyList())[0], breed)
    }
}

private object BreedCreator {
    fun breed() = Breed(
        id = randomString(),
        description = randomString(),
        name = randomString(),
        origin = randomString()
    )
}

private fun randomString() = Random.nextFloat().toString()

private class KatzImageServiceFake: KatzImageService {
    var images: Result<List<Image>> = Result.failure(Exception())
    var breeds: Result<List<Breed>> = Result.failure(Exception())

    override suspend fun images(breedId: String?): Result<List<Image>> = images
    override suspend fun breeds(): Result<List<Breed>> = breeds
}
