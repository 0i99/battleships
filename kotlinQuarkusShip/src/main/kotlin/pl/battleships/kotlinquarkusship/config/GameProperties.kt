package pl.battleships.kotlinquarkusship.config

import io.quarkus.runtime.annotations.StaticInitSafe
import io.smallrye.config.ConfigMapping

@ConfigMapping(prefix = "game")
@StaticInitSafe
interface GameProperties {

    fun timeout(): Int

    fun sleep(): Long

    fun opponentUrl(): String
}