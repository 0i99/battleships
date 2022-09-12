package pl.battleships.kotlinspringship.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import pl.battleships.core.api.BattleshipGame
import pl.battleships.core.api.BattleshipGameImpl
import pl.battleships.core.api.HistoryProvider
import pl.battleships.core.api.ShotHandler
import pl.battleships.kotlinspringship.client.RestTemplateResponseErrorHandler
import pl.battleships.kotlinspringship.client.SimpleShotHandler
import pl.battleships.kotlinspringship.history.SimpleHistoryProvider
import pl.battleships.kotlinspringship.service.GameService
import pl.battleships.kotlinspringship.service.GameServiceImpl

@Configuration
class Config {

    @Bean
    fun restTemplate(properties: GameProperties): RestTemplate? {
        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory()
        clientHttpRequestFactory.setConnectTimeout(properties.timeout)
        val template = RestTemplate(clientHttpRequestFactory)
        template.errorHandler = RestTemplateResponseErrorHandler()
        return template
    }

    @Bean
    fun shotHandler(restTemplate: RestTemplate, gameProperties: GameProperties): ShotHandler {
        return SimpleShotHandler(restTemplate, gameProperties)
    }

    @Bean
    fun historyProvider(): HistoryProvider {
        return SimpleHistoryProvider()
    }

    @Bean
    fun battleshipGame(historyProvider: HistoryProvider, shotHandler: ShotHandler): BattleshipGame {
        return BattleshipGameImpl(historyProvider, shotHandler)
    }

    @Bean
    fun gameService(battleshipGame: BattleshipGame, historyProvider: HistoryProvider): GameService {
        return GameServiceImpl(battleshipGame, historyProvider)
    }
}