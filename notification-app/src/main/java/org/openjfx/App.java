package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        int x = 1;
        while (x != 0) {
            Process process = java.lang.Runtime.getRuntime().exec("ping www.google.com");
            x = process.waitFor();
            if (x != 0) {
                Thread.sleep(1000);
            }
        }

        scene = new Scene(loadFXML("/org/openjfx/ledicom/dashboard"));
        stage.setScene(scene);
        stage.setTitle("Ледиком Уведомления");
        stage.show();
        stage.setResizable(false);
        stage.getIcons().add(new Image(App.class.getResourceAsStream("/org/openjfx/ledicom/icon.png")));
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}