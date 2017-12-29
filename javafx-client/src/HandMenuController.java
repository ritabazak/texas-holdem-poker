import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Optional;


public class HandMenuController {
    private PokerEngine engine;
    private Stage stage;
    private RootController parent;
    private IntegerProperty maxBet;
    @FXML private Button foldButton;
    @FXML private Button checkButton;
    @FXML private Button callButton;
    @FXML private Button betButton;
    @FXML private Button raiseButton;

    public void setEngine(PokerEngine engine) {
        this.engine = engine;
    }

    public void setStage(Stage stage) { this.stage = stage; }

    public void setParentController(RootController rootController) {
        parent = rootController;
    }

    public void bindHandStatusProperties(IntegerProperty maxBet, BooleanProperty betActive) {
        this.maxBet = maxBet;
        checkButton.disableProperty().bind(betActive);
        callButton.disableProperty().bind(betActive.not());
        betButton.disableProperty().bind(betActive);
        raiseButton.disableProperty().bind(betActive.not().and(maxBet.lessThanOrEqualTo(0)));
    }

    @FXML private void foldButtonAction(ActionEvent action){
        engine.fold();
        parent.nextTurn();
    }

    @FXML private void checkButtonAction(ActionEvent action){
        engine.check();
        parent.nextTurn();
    }

    @FXML private void callButtonAction(ActionEvent action){
        engine.call();
        parent.nextTurn();
    }

    @FXML private void betButtonAction(ActionEvent action) {
        Optional<Integer> val = showSliderDialog(maxBet.intValue(), "Please enter a bet:");

        if (val.isPresent()) {
            engine.placeBet(val.get());
            parent.nextTurn();
        }
    }

    @FXML private void raiseButtonAction(ActionEvent action) {
        Optional<Integer> val = showSliderDialog(maxBet.intValue(), "Please enter a raise:");

        if (val.isPresent()) {
            engine.raise(val.get());
            parent.nextTurn();
        }
    }

    private Optional<Integer> showSliderDialog(int maxValue, String title){
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Prompt");
        dialog.setHeaderText(title);

        GridPane pane = new GridPane();

        Slider slider = new Slider(1.0, maxValue, 1);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);
        slider.setMinorTickCount(0);
        slider.setShowTickMarks(true);

        Label label = new Label();
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        label.textProperty().bind(slider.valueProperty().asString());
        pane.add(slider, 0, 0);
        pane.add(label, 0, 1);

        dialog.getDialogPane().setContent(pane);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

        dialog.setResultConverter(buttonType ->
                buttonType == ButtonType.OK? Double.valueOf(slider.getValue()).intValue(): null);

        return dialog.showAndWait();
    }
}
