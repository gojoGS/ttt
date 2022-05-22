package org.example.util;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

public class JavaFxUtil {
    public static void load(Stage stage, String path) {
        try {
            Parent root = FXMLLoader.load(JavaFxUtil.class.getClassLoader().getResource(path));
            var scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.error(String.format("Failed to load %s.", path));
        }
    }

    public static void load(Stage stage, String path, Initializable controller) {
        try {
            FXMLLoader loader = new FXMLLoader(JavaFxUtil.class.getClassLoader().getResource(path));
            loader.setController(controller);

            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            Logger.error(String.format("Failed to load %s.", path));
        }
    }

    public static Stage getStageOfEvent(Event event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
