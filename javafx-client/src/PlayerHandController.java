import immutables.Card;
import immutables.PlayerHandInfo;
import javafx.beans.binding.*;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PlayerHandController {
    @FXML private GridPane grid;
    @FXML private CardController cardOneImageController;
    @FXML private CardController cardTwoImageController;
    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label chipsLabel;
    @FXML private Label betLabel;
    private BooleanBinding currentPlayer;

    public void bindPlayerProperties(ObjectBinding<PlayerHandInfo> playerInfo, BooleanProperty handInProgress) {
        StringBinding safeName = Bindings.createStringBinding(() ->
                playerInfo.get() == null? "": playerInfo.get().getName(), playerInfo);

        StringBinding safeType = Bindings.createStringBinding(() ->
                playerInfo.get() == null? "": playerInfo.get().getState().toShortString(), playerInfo);

        NumberBinding safeChips = Bindings.createIntegerBinding(() ->
                playerInfo.get() == null? 0: playerInfo.get().getChips(), playerInfo);

        NumberBinding safeBet = Bindings.createIntegerBinding(() ->
                playerInfo.get() == null? 0: playerInfo.get().getBet(), playerInfo);

        ObjectBinding<Card> safeCardOne = Bindings.createObjectBinding(() ->
                playerInfo.get() == null? null: playerInfo.get().getFirstCard(), playerInfo);

        ObjectBinding<Card> safeCardTwo = Bindings.createObjectBinding(() ->
                playerInfo.get() == null? null: playerInfo.get().getSecondCard(), playerInfo);

        BooleanBinding safePlayerFolded = Bindings.createBooleanBinding(() ->
                playerInfo.get() != null && playerInfo.get().isFolded(), playerInfo);

        nameLabel.textProperty().bind(safeName);
        typeLabel.textProperty().bind(safeType);
        chipsLabel.textProperty().bind(safeChips.asString());
        betLabel.textProperty().bind(safeBet.asString());
        cardOneImageController.bindCard(safeCardOne, handInProgress.not());
        cardTwoImageController.bindCard(safeCardTwo, handInProgress.not());
        typeLabel.visibleProperty().bind(safeType.isNotEmpty());
        typeLabel.managedProperty().bind(typeLabel.visibleProperty());
        grid.opacityProperty().bind(
                Bindings.when(safePlayerFolded)
                        .then(0.5)
                        .otherwise(1)
        );

        if (currentPlayer != null) {
            currentPlayer.dispose();
            currentPlayer = null;
        }

        currentPlayer = Bindings.createBooleanBinding(() ->
                playerInfo.get() != null && playerInfo.get().isCurrent(), playerInfo);

        handleCurrentPlayerChange(currentPlayer.get());
        currentPlayer.addListener((observer, oldValue, newValue) -> handleCurrentPlayerChange(newValue));
    }

    private void handleCurrentPlayerChange(boolean current) {
        ObservableList<String> classes = grid.getStyleClass();

        if (current) {
            if (!classes.contains("currentPlayer")) {
                classes.add("currentPlayer");
            }
        }
        else {
            if (classes.contains("currentPlayer")) {
                classes.remove("currentPlayer");
            }
        }
    }
}
