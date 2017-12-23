import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class JavaFXPokerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("root.fxml");
        loader.setLocation(url);
        Parent root = loader.load(url.openStream());

        RootController rootController = loader.getController();

        rootController.setStage(primaryStage);
        rootController.setEngine(new PokerEngine());

        primaryStage.setTitle("Texas Hold'Em Poker");
        primaryStage.setScene(new Scene(root, 1050, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}