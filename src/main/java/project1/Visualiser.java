package project1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Visualiser extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Team 2: Electric Boogaloo");
        primaryStage.show();
    }

    public static void Main(String[] args) {
        launch(args);
    }
}

