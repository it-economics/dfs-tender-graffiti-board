package de.dfs.graffitiboard.message.api

import java.time.ZonedDateTime

data class MessageReadDto(
    val author: String?,
    val message: String,
    val createdAt: ZonedDateTime?
)
