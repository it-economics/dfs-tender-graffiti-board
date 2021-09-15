package de.dfs.graffitiboard.message.service

import java.time.ZonedDateTime

data class Message(
    val author: String?,
    val message: String,
    val createdAt: ZonedDateTime? = null
)
