package dev.pauldavies.katz.domain

import dev.pauldavies.katz.service.KatzImageService

data class Image(val id: String, val url: String)

internal fun KatzImageService.Image.toImage(): Image = Image(
    id = id,
    url = url
)

