package project1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

        MediaPlayer player = new MediaPlayer( new Media(getClass().getResource("/media/bg720slow.mp4").toExternalForm()));
        // MediaView to play the visual file
        MediaView mediaView = new MediaView(player);

        // Music by Karl Casey @ White Bat Audio
        soundPlayer = new MediaPlayer(new Media(getClass().getResource("/media/Karl Casey - The New Order.mp3").toURI().toString()));

        background.getChildren().add(mediaView);
        background.getChildren().add(root);

        primaryStage.setScene(new Scene(background));
        primaryStage.setResizable(false);
        primaryStage.setTitle("Team 2: Electric Boogaloo");
        primaryStage.show();

        // Loop background and sound media
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
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

