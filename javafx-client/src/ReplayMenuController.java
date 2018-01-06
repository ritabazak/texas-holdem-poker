import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ReplayMenuController {
    @FXML private Button prevButton;
    @FXML private Button nextButton;

    private RootController parent;

    public void bindButtonDisabledStates(NumberBinding replaySize, IntegerProperty replayStep) {
        prevButton.disableProperty().bind(replayStep.isEqualTo(0));
        nextButton.disableProperty().bind(replayStep.isEqualTo(replaySize.subtract(1)));
    }

    public void setParentController(RootController parent) {
        this.parent = parent;
    }

    @FXML private void prevButtonAction(ActionEvent actionEvent) {
        parent.prevReplayStep();
    }

    @FXML private void nextButtonAction(ActionEvent actionEvent) {
        parent.nextReplayStep();
    }

    @FXML private void endButtonAction(ActionEvent actionEvent) {
        parent.endReplayMode();
    }
}