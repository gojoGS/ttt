package org.example.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.example.model.game.GameState;
import org.example.model.game.Position;
import org.example.model.gameresult.GameResult;
import org.example.model.player.Player;
import org.example.model.player.PlayerColor;
import org.example.service.GameResultService;
import org.example.util.JavaFxUtil;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    private final GameState gameState;
    @FXML Button yield;
    @FXML Button endTurn;
    @FXML Label messageBox;
    @FXML Label playerName;
    @FXML GridPane board;

    public GameController(GameState gameState) {
        this.gameState = gameState;
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        playerName.setText(gameState.getCurrentPlayer().getName());
        yield.setOnAction(this::onYield);
        endTurn.setOnAction(this::onEndTurn);

        long boardSize = gameState.getBoardSize();
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {

                Rectangle rectangle = new Rectangle(250, 250);
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setOnMouseClicked(this::onFieldClick);

                GridPane.setMargin(rectangle, new Insets(1));
                GridPane.setHalignment(rectangle, HPos.CENTER);
                GridPane.setValignment(rectangle, VPos.CENTER);
                board.add(rectangle, i, j);
            }
        }
    }

    private void onYield(ActionEvent actionEvent) {
        gameState.yield();
        Player winner = gameState.getCurrentPlayer();
        endGame(Optional.of(winner), JavaFxUtil.getStageOfEvent(actionEvent));
    }

    private void endGame(Optional<Player> winner, Stage stage) {
        GameResult gameResult =
            winner.isEmpty() ? gameState.getDrawGameResult() : gameState.getGameResult();
        GameResultService.getInstance().save(gameResult);

        EndController endController = new EndController(winner);

        JavaFxUtil.load(stage, "fxml/end.fxml", endController);
    }

    private Rectangle getSquareByPosition(Position position) {
        for (var node : board.getChildren()) {
            if (node instanceof Rectangle rectangle) {
                if (GridPane.getColumnIndex(rectangle) == position.getColumn()
                    && GridPane.getRowIndex(rectangle) == position.getRow()) {
                    return rectangle;
                }
            }
        }

        throw new IllegalArgumentException();
    }

    private ImagePattern getPattern(PlayerColor playerColor, boolean isPlanned) {
        if (!isPlanned) {
            return switch (playerColor) {
                case BLUE -> new ImagePattern(new Image(
                    GameController.class.getClassLoader().getResource("image/blue.png")
                        .toString()));
                case RED -> new ImagePattern(new Image(
                    GameController.class.getClassLoader().getResource("image/red.png").toString()));
            };
        } else {
            return switch (playerColor) {
                case BLUE -> new ImagePattern(new Image(
                    GameController.class.getClassLoader().getResource("image/blue_t.png")
                        .toString()));
                case RED -> new ImagePattern(new Image(
                    GameController.class.getClassLoader().getResource("image/red_t.png")
                        .toString()));
            };

        }
    }

    private void onEndTurn(ActionEvent actionEvent) {
        var plannedPosition = gameState.getPlannedPosition();

        if (plannedPosition.isEmpty()) {
            return;
        }

        var position = plannedPosition.get();
        gameState.occupyPositionByCurrentPlayer(position);

        Rectangle rectangle = getSquareByPosition(position);
        rectangle.setFill(getPattern(gameState.getCurrentPlayerColor(), false));

        if (gameState.isDraw()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Allah punishes the wrathful!");
            var result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                endGame(Optional.empty(), JavaFxUtil.getStageOfEvent(actionEvent));
            }
        }

        if (gameState.isCurrentPlayerWinner()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Insa'Allah! Praised be God!");
            var result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                endGame(Optional.of(gameState.getCurrentPlayer()),
                    JavaFxUtil.getStageOfEvent(actionEvent));
            }
            return;
        }

        gameState.endTurn();
        playerName.setText(gameState.getCurrentPlayer().getName());
    }

    private void onFieldClick(MouseEvent mouseEvent) {
        var inputButtonType = mouseEvent.getButton();

        if (!inputButtonType.equals(MouseButton.PRIMARY)) {
            return;
        }

        var eventSource = (Rectangle) mouseEvent.getSource();
        int inputCol = GridPane.getColumnIndex(eventSource);
        int inputRow = GridPane.getRowIndex(eventSource);

        Position position = new Position(inputRow, inputCol);

        if (gameState.isPositionAlreadyOccupied(position)) {
            return;
        }

        Rectangle rectangle = getSquareByPosition(position);

        if (gameState.isPositionAlreadySelectedAsPlanned(position)) {
            gameState.unsetPositionOfNextMove();
            rectangle.setFill(Color.TRANSPARENT);
        } else if (!gameState.doesPlayerHavePlannedPosition()) {
            gameState.setPositionOfNextMove(position);
            rectangle.setFill(getPattern(gameState.getCurrentPlayerColor(), true));
        } else {
            return;
        }
    }
}
