package org.example.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.db.guice.PersistenceModule;
import org.example.model.gameresult.GameResult;
import org.example.model.gameresult.GameResultDao;

import java.util.List;

public class GameResultService {
    private static GameResultService instance;
    private final GameResultDao gameResultDao;

    private GameResultService() {
        Injector injector = Guice.createInjector(new PersistenceModule("test"));
        this.gameResultDao = injector.getInstance(GameResultDao.class);
    }

    public static GameResultService getInstance() {
        if (instance == null) {
            instance = new GameResultService();
        }

        return instance;
    }

    public void save(GameResult gameResult) {
        gameResultDao.persist(gameResult);
    }

    public List<GameResult> findAll() {
        return gameResultDao.findAll();
    }
}
