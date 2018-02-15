import engine.PokerEngine;
import immutables.PlayerGameInfo;
import immutables.PlayerInfo;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BetweenHandsMenuController {
    @FXML private Button replayButton;
    @FXML private Button playNextHandButton;
    @FXML private Button buyInButton;
    @FXML private Button endGameButton;

    private RootController parent;
    private PokerEngine engine;
    private ObservableList<PlayerGameInfo> playerGameInfoProperty;

    public void bindButtonDisabledStates(IntegerProperty handsPlayed, IntegerProperty totalHands, BooleanProperty canStartHand) {
        replayButton.disableProperty().bind(handsPlayed.isEqualTo(0));
        playNextHandButton.disableProperty().bind(handsPlayed.isEqualTo(totalHands).or(canStartHand.not()));
        buyInButton.disableProperty().bind(handsPlayed.isEqualTo(totalHands));
        playNextHandButton.disableProperty().bind(canStartHand.not());
    }

    @FXML private void replayButtonAction(ActionEvent event) {
        parent.startReplayMode();
    }

    @FXML private void playNextHandButtonAction(ActionEvent event) {
        engine.startHand();
        if (!engine.isHumanTurn()) {
            parent.nextTurn();
        }

        parent.updateGameStatus();
        parent.updateHandStatus();
    }

    @FXML private void buyInButtonAction(ActionEvent event) {
        List<PlayerGameInfo> humanPlayers = playerGameInfoProperty.stream()
                .filter(player -> player.getType() == PlayerInfo.PlayerType.HUMAN)
                .collect(Collectors.toList());

        Optional<PlayerGameInfo> selectedPlayer = showPlayerSelectionDialog(humanPlayers);

        selectedPlayer.ifPresent(player -> engine.addBuyIn(engine.getPlayerIndexById(player.getId())));
        parent.updateGameStatus();
    }

    @FXML private void retireButtonAction(ActionEvent actionEvent) {
        List<PlayerGameInfo> humanPlayers = playerGameInfoProperty.stream()
                .filter(player -> player.getType() == PlayerInfo.PlayerType.HUMAN)
                .collect(Collectors.toList());

        Optional<PlayerGameInfo> selectedPlayer = showPlayerSelectionDialog(humanPlayers);

        selectedPlayer.ifPresent(player -> engine.retirePlayer(engine.getPlayerIndexById(player.getId())));

        parent.updateGameStatus();

        if (!parent.isGameOn()) {
            parent.resetHandStatus();
            parent.updateGameStatus();
        }
    }

    @FXML private void endGameButtonAction(ActionEvent event) {
        int maxChips = playerGameInfoProperty
                .stream()
                .mapToInt(PlayerGameInfo::getChips)
                .max().orElse(0);

        PlayerGameInfo winner = playerGameInfoProperty
                .stream()
                .filter(player -> player.getChips() == maxChips)
                .findFirst().orElse(playerGameInfoProperty.get(0));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setHeaderText("Winner:");
        alert.setTitle("Game Finished!");
        alert.setContentText(String.format("%s won the game with %d chips!", winner.getName(), winner.getChips()));

        alert.showAndWait();

        engine.endGame();
        parent.updateGameStatus();
        parent.resetHandStatus();
    }

    private Optional<PlayerGameInfo> showPlayerSelectionDialog(List<PlayerGameInfo> players) {
        ChoiceDialog<PlayerGameInfo> dialog = new ChoiceDialog<>(null, players);
        dialog.getDialogPane().getContent();
        dialog.setTitle("Select");
        dialog.setHeaderText("Please choose a player:");

        return dialog.showAndWait();
    }

    public void setParentController(RootController parent) {
        this.parent = parent;
    }

    public void setEngine(PokerEngine engine) {
        this.engine = engine;
    }

    public void setPlayerGameInfoProperty(ObservableList<PlayerGameInfo> playerGameInfoProperty) {
        this.playerGameInfoProperty = playerGameInfoProperty;
    }
}
