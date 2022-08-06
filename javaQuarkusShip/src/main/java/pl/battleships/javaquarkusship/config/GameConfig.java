package pl.battleships.javaquarkusship.config;

import io.quarkus.arc.DefaultBean;
import pl.battleships.core.api.BattleshipGame;
import pl.battleships.core.api.BattleshipGameImpl;
import pl.battleships.core.api.HistoryProvider;
import pl.battleships.core.api.ShotHandler;
import pl.battleships.javaquarkusship.mapper.GameMapper;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class GameConfig {

    @Produces
    @ApplicationScoped
    @DefaultBean
    public GameMapper mapper() {
        return GameMapper.INSTANCE;
    }

    @Produces
    @ApplicationScoped
    public BattleshipGame battleshipGame(HistoryProvider historyProvider, ShotHandler shotHandler) {
        return new BattleshipGameImpl(historyProvider, shotHandler);
    }
}
