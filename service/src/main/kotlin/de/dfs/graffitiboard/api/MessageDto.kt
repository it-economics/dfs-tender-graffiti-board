package de.dfs.graffitiboard.api

import javax.validation.constraints.Size

data class MessageDto(

    val author: String?,

    @field:Size(max = 300)
    val message: String

)
