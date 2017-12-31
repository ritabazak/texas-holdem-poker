import exceptions.BadFileExtensionException;
import exceptions.DuplicatePlayerIdException;
import exceptions.InvalidBlindsException;
import exceptions.InvalidHandsCountException;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;

public class GameMenuController {
    @FXML Button startGameButton;

    private Stage stage;
    private PokerEngine engine;
    private RootController parent;

    @FXML private void loadXmlButtonAction(ActionEvent event) {
        FileChooser chooser = new FileChooser();

        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        chooser.setTitle("Choose a config file...");

        File f = chooser.showOpenDialog(stage);

        if (f != null) {
            //TODO: Load Bar xml
            try {
                engine.loadConfigFile(f.getAbsolutePath());
                parent.updateXmlLoaded();
            }
            catch (FileNotFoundException | BadFileExtensionException e) {
                // Cannot happen
            }
            catch (InvalidHandsCountException e) {
                showAlert("Invalid hands count in the config file!");
            }
            catch (InvalidBlindsException e) {
                showAlert("Invalid blinds in the config file!");
            }
            catch (DuplicatePlayerIdException e) {
                showAlert("Duplicate player IDs in the config file!");
            }
        }
    }

    @FXML private void startGameButtonAction(ActionEvent event) {
        engine.startGame();
        parent.updateGameStatus();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message + "\nPlease try again.");
        alert.show();
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
        startGameButton.disableProperty().bind(xmlLoaded.not());
    }
}