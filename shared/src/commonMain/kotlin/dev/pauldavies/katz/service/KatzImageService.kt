package dev.pauldavies.katz.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

private const val BASE_URL = "https://api.thecatapi.com/v1"

class KatzImageService(private val httpClient: HttpClient) {
    suspend fun images(breedId: String? = null): Result<List<Image>> {
        return try {
            val response = httpClient.get("$BASE_URL/images/search") {
                url {
                    parameters.append("limit", "50")
                    breedId?.let {
                        parameters.append("breed_id", breedId)
                    }
                }
            }.body<List<Image>>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun breeds(): Result<List<Breed>> {
        return try {
            Result.success(httpClient.get("$BASE_URL/breeds").body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
 }

@Serializable
data class Image(val id: String, val url: String)

@Serializable
data class Breed(val id: String, val name: String)
