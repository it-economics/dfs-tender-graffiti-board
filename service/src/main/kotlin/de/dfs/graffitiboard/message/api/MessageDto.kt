package de.dfs.graffitiboard.message.api

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class MessageDto(

    val author: String?,

    @field:NotBlank
    @field:Size(max = 300)
    val message: String

)
