package pl.battleships.javaspringship.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.BattleshipGameImpl;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.core.api.ShotHandler;
import pl.battleships.javaspringship.client.RestTemplateResponseErrorHandler;
import pl.battleships.javaspringship.client.SimpleShotHandler;
import pl.battleships.javaspringship.history.SimpleHistoryProvider;
import pl.battleships.javaspringship.mapper.GameModelMapper;
import pl.battleships.javaspringship.service.GameService;
import pl.battleships.javaspringship.service.GameServiceImpl;

@Configuration
@Import(GameProperties.class)
public class GameConfig {

    @Autowired
    GameProperties properties;

    @Bean
    public HistoryProvider historyProvider() {
        return new SimpleHistoryProvider();
    }

    @Bean
    public BattleshipGame battleshipGame(HistoryProvider historyProvider, ShotHandler shotHandler) {
        return new BattleshipGameImpl(historyProvider, shotHandler);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public GameModelMapper gameMapper(ModelMapper modelMapper) {
        return new GameModelMapper(modelMapper);
    }

    @Bean
    public GameService gameService(BattleshipGame battleshipGame, GameModelMapper modelMapper, HistoryProvider historyProvider) {
        return new GameServiceImpl(battleshipGame, modelMapper, historyProvider);
    }

    @Bean
    public RestTemplate restTemplate(GameProperties properties) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(properties.getTimeout());
        RestTemplate template = new RestTemplate(clientHttpRequestFactory);
        template.setErrorHandler(new RestTemplateResponseErrorHandler());
        return template;
    }

    @Bean
    public ShotHandler simpleShotHandler(GameProperties properties, RestTemplate restTemplate) {
        return new SimpleShotHandler(properties, restTemplate);
    }
}
