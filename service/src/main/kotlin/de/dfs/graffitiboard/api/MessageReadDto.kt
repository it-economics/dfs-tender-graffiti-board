package de.dfs.graffitiboard.api

import java.time.ZonedDateTime

data class MessageReadDto(
    val author: String?,
    val message: String,
    val createdAt: ZonedDateTime
)
