package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.example.model.game.GameState;
import org.example.model.game.GameStateImpl;
import org.example.model.player.Player;
import org.example.model.player.PlayerColor;
import org.example.util.JavaFxUtil;
import org.tinylog.Logger;

import java.util.EnumMap;

public class MainController {
    @FXML TextField playerRed;
    @FXML TextField playerBlue;

    @SneakyThrows
    private void loadGame(EnumMap<PlayerColor, Player> playerNameMapping, Stage stage) {
        GameState gameState = new GameStateImpl(playerNameMapping);
        GameController gameController = new GameController(gameState);
        JavaFxUtil.load(stage, "fxml/game.fxml", gameController);
    }

    public void onStart(ActionEvent actionEvent) {
        String playerRedName = playerRed.getText();
        String playerBlueName = playerBlue.getText();

        if (playerRedName.isEmpty() || playerBlueName.isEmpty()) {
            Logger.info("invalid player name(s)");
            return;
        }

        var playerNameMapping = new EnumMap<PlayerColor, Player>(PlayerColor.class);
        playerNameMapping.put(PlayerColor.RED, new Player(playerRedName));
        playerNameMapping.put(PlayerColor.BLUE, new Player(playerBlueName));

        loadGame(playerNameMapping, JavaFxUtil.getStageOfEvent(actionEvent));
    }
}
