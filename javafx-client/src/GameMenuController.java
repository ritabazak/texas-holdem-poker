import exceptions.BadFileExtensionException;
import exceptions.DuplicatePlayerIdException;
import exceptions.InvalidBlindsException;
import exceptions.InvalidHandsCountException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;

public class GameMenuController {
    @FXML private Button loadXmlButton;
    @FXML private Button startGameButton;
    @FXML private ProgressBar xmlLoadProgressBar;

    private Stage stage;
    private PokerEngine engine;
    private RootController parent;

    private BooleanProperty disableButtons = new SimpleBooleanProperty(false);

    @FXML private void loadXmlButtonAction(ActionEvent event) {
        FileChooser chooser = new FileChooser();

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        chooser.setTitle("Choose a config file...");

        File f = chooser.showOpenDialog(stage);

        if (f != null) {
            disableButtons.setValue(true);

            Task<Void> task = engine.loadConfigFileTask(f.getAbsolutePath());

            xmlLoadProgressBar.progressProperty().bind(task.progressProperty());
            task.setOnSucceeded((taskEvent) -> {
                parent.updateXmlLoaded();
                disableButtons.setValue(false);
            });
            task.setOnFailed(taskEvent -> {
                if (task.getException() instanceof FileNotFoundException) {
                    showAlert("File not found!");
                }
                if (task.getException() instanceof BadFileExtensionException) {
                    showAlert("Invalid file extension!");
                }
                if (task.getException() instanceof InvalidHandsCountException) {
                    showAlert("Invalid hands count in the config file!");
                }
                if (task.getException() instanceof InvalidBlindsException) {
                    showAlert("Invalid blinds in the config file!");
                }
                if (task.getException() instanceof DuplicatePlayerIdException) {
                    showAlert("Duplicate player IDs in the config file!");
                }
                disableButtons.setValue(false);
                xmlLoadProgressBar.progressProperty().unbind();
                xmlLoadProgressBar.setProgress(0);
            });

            Thread th = new Thread(task);
            th.setDaemon(true);
            th.start();
        }
    }

    @FXML private void startGameButtonAction(ActionEvent event) {
        engine.startGame();
        parent.updateGameStatus();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message + "\nPlease try again.");
        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setEngine(PokerEngine engine) {
        this.engine = engine;
    }

    public void setParentController(RootController rootController) {
        parent = rootController;
    }

    public void bindXmlLoaded(BooleanProperty xmlLoaded) {
        loadXmlButton.disableProperty().bind(disableButtons);
        startGameButton.disableProperty().bind(xmlLoaded.not().or(disableButtons));
    }
}