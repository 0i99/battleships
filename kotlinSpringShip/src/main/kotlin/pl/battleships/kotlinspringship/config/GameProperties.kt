package pl.battleships.kotlinspringship.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "game")
@ConstructorBinding
data class GameProperties(
    val timeout: Int,
    val sleep: Long = 150,
    val opponentUrl: String,
)
