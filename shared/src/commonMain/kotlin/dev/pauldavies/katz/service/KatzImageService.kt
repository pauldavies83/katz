package dev.pauldavies.katz.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

private const val BASE_URL = "https://api.thecatapi.com/v1"

interface KatzImageService {
    suspend fun images(breedId: String? = null): Result<List<Image>>
    suspend fun breeds(): Result<List<Breed>>

    @Serializable
    data class Image(val id: String, val url: String)

    @Serializable
    data class Breed(
        val id: String,
        val name: String,
        val description: String,
        val origin: String
    )
}

internal class KatzImageServiceNetwork(private val httpClient: HttpClient): KatzImageService {
    override suspend fun images(breedId: String?): Result<List<KatzImageService.Image>> {
        return try {
            val response = httpClient.get("$BASE_URL/images/search") {
                url {
                    parameters.append("limit", "50")
                    breedId?.let {
                        parameters.append("breed_id", breedId)
                    }
                }
            }.body<List<KatzImageService.Image>>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun breeds(): Result<List<KatzImageService.Breed>> {
        return try {
            Result.success(httpClient.get("$BASE_URL/breeds").body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
 }
