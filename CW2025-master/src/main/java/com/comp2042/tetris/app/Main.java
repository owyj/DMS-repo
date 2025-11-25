package com.comp2042.tetris.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.comp2042.tetris.util.MusicManager;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("startMenu.fxml");
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("TetrisJFX - Main Menu");
        Scene scene = new Scene(root, 350, 600); //increased width and height to fit new layout
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); //prevent resizing the window

        // Stop music when window is closed
        primaryStage.setOnCloseRequest(event -> MusicManager.getInstance().stopMusic());

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
