package dev.pauldavies.katz.domain

import dev.pauldavies.katz.service.KatzImageService

data class Breed(
    val id: String,
    val name: String,
    val description: String,
    val origin: String
)

internal fun KatzImageService.Breed.toBreed() = Breed(
    id = id,
    name = name,
    description = description,
    origin = origin
)
