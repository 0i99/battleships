package pl.battleships.javaspringship.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.BattleshipGameImpl;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.javaspringship.history.SimpleHistoryProvider;
import pl.battleships.javaspringship.mapper.GameModelMapper;
import pl.battleships.javaspringship.service.GameService;
import pl.battleships.javaspringship.service.GameServiceImpl;

@Configuration
public class GameConfig {

    @Bean
    public HistoryProvider historyProvider() {
        return new SimpleHistoryProvider();
    }

    @Bean
    public BattleshipGame battleshipGame(HistoryProvider historyProvider) {
        return new BattleshipGameImpl(historyProvider);
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
}
