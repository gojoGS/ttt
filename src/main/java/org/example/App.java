package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.db.guice.PersistenceModule;
import org.example.model.gameresult.GameResult;
import org.example.model.gameresult.GameResultDao;
import org.example.model.player.PlayerColor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Injector injector = Guice.createInjector(new PersistenceModule("test"));
        GameResultDao gameResultDao = injector.getInstance(GameResultDao.class);

        GameResult gameResult = GameResult
            .builder()
            .colorOfWinner(PlayerColor.RED)
            .numberOfTurns(10)
            .bluePlayerName("asd")
            .redPlayerName("das")
            .build();

        gameResultDao.persist(gameResult);

        System.out.println( gameResultDao.findAll() );
    }
}
