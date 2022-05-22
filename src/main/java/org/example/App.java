package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.util.JavaFxUtil;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override public void start(Stage stage) throws Exception {
        stage.setTitle("TicTacToe");
        JavaFxUtil.load(stage, "fxml/main.fxml");
    }
}
