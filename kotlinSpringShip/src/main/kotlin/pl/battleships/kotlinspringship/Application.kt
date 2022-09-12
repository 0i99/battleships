package pl.battleships.kotlinspringship

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import pl.battleships.kotlinspringship.config.GameProperties

@SpringBootApplication
@EnableConfigurationProperties(GameProperties::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
