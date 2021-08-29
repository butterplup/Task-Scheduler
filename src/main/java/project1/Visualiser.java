package project1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/**
 * This class extends the JavaFX application class to create and run the GUI
 * which displays the state of the search algorithm
 */
public class Visualiser extends Application {
    // Keep a reference to our MediaPlayer for the sound
    MediaPlayer soundPlayer;
    /**
     * The start method loads in the fxml file, sets up basic attributes of the stage
     * and then runs the algorithm on a new thread
     * @param primaryStage - the stage which contains all the UI shown to the user
     * @throws Exception - may be caused when input files do not exist or output files already exist
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane background = new StackPane();
        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));

        // Music by Karl Casey @ White Bat Audio
        soundPlayer = new MediaPlayer(new Media(getClass().getResource("/media/Karl Casey - The New Order.mp3").toURI().toString()));

        // Load in fallback image
        ImageView fallback = new ImageView();
        Image fallbackImg = new Image(getClass().getResource("/media/bg720Fallback.png").openStream());
        fallback.setImage(fallbackImg);

        background.getChildren().add(fallback);
        background.getChildren().add(new MediaView());
        background.getChildren().add(root);

        primaryStage.setScene(new Scene(background));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Team 2: Electric Boogaloo");
        primaryStage.show();

        soundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        soundPlayer.play();
    }

    /**
     * The entry point to this class simply launches the JavaFX application
     * @param args - unused by the JavaFX application
     */
    public static void Main(String[] args) {
        launch();
    }
}

