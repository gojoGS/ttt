package org.example.gui.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.model.gameresult.GameResult;
import org.example.model.player.Player;
import org.example.model.player.PlayerColor;
import org.example.service.GameResultService;
import org.example.util.JavaFxUtil;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EndController implements Initializable {
    @FXML private Label winnerName;
    @FXML private Button back;

    @FXML
    public TableColumn<GameResult, String> redPlayerName;
    @FXML
    public TableColumn<GameResult, String> bluePlayerName;
    @FXML
    public TableColumn<GameResult, Integer> numberOfTurns;
    @FXML
    public TableColumn<GameResult, PlayerColor> colorOfWinner;
    @FXML
    TableView<GameResult> leaderBoard;

    private final Optional<Player> winner;
    private final List<GameResult> gameResults;

    public EndController(Optional<Player> winner) {
        this.winner = winner;
        this.gameResults = GameResultService.getInstance().findAll();
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {
        if (winner.isPresent()) {
            winnerName.setText(
                String.format("Masha'Allah! %s has triumphed!", winner.get().getName()));
        } else {
            winnerName.setText("Disaster! Arabia in flames!");
        }

        back.setOnAction(this::onBack);
        redPlayerName.setCellValueFactory(new PropertyValueFactory<>("redPlayerName"));
        bluePlayerName.setCellValueFactory(new PropertyValueFactory<>("bluePlayerName"));
        numberOfTurns.setCellValueFactory(new PropertyValueFactory<>("numberOfTurns"));
        colorOfWinner.setCellValueFactory(new PropertyValueFactory<>("colorOfWinner"));

        leaderBoard.setItems(FXCollections.observableList(gameResults));

    }

    private void onBack(ActionEvent actionEvent) {
        JavaFxUtil.load(JavaFxUtil.getStageOfEvent(actionEvent), "fxml/main.fxml");
    }
}
