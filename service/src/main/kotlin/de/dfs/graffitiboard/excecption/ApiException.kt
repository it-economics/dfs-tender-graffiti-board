package de.dfs.graffitiboard.excecption

import org.springframework.http.HttpStatus

internal data class ApiException(
    val status: HttpStatus,
    val message: String,
    var errors: Map<String, List<String?>>
)
