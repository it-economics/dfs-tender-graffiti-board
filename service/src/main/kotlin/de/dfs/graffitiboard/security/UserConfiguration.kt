package de.dfs.graffitiboard.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "dfs.board.security")
data class UserConfiguration(
    val users: List<User>
)

data class User(
    val name: String,
    val password: String
)
