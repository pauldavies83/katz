package dev.pauldavies.katz.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

class KatzImageService(private val httpClient: HttpClient) {
    suspend fun images(): Result<List<Image>> {
        return try {
            Result.success(httpClient.get("https://api.thecatapi.com/v1/images/search").body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

@Serializable
data class Image(val id: String, val url: String)
