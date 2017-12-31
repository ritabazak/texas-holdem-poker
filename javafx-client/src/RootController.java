import immutables.Card;
import immutables.PlayerGameInfo;
import immutables.PlayerHandInfo;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RootController {
    @FXML private GameBoardController gameBoardController;
    @FXML private GameMenuController gameMenuController;
    @FXML private HandMenuController handMenuController;
    @FXML private BetweenHandsMenuController betweenHandsMenuController;
    @FXML private GameInfoPaneController gameInfoPaneController;

    @FXML private Pane gameInfoPane;
    @FXML private Pane gameMenu;
    @FXML private Pane handMenu;
    @FXML private Pane betweenHandsMenu;

    private PokerEngine engine;
    private Stage stage;

    private BooleanProperty gameOn = new SimpleBooleanProperty(false);
    private BooleanProperty xmlLoaded = new SimpleBooleanProperty(false);
    private BooleanProperty handInProgress = new SimpleBooleanProperty(false);
    private ObservableList<PlayerGameInfo> playerGameInfo = FXCollections.observableList(new ArrayList<>());
    private IntegerProperty handsPlayed = new SimpleIntegerProperty(-1);
    private IntegerProperty totalHands = new SimpleIntegerProperty(0);
    private IntegerProperty smallBlind = new SimpleIntegerProperty(0);
    private IntegerProperty bigBlind = new SimpleIntegerProperty(0);
    private IntegerProperty initialSmallBlind = new SimpleIntegerProperty(0);
    private IntegerProperty initialBigBlind = new SimpleIntegerProperty(0);
    private IntegerProperty buyIn = new SimpleIntegerProperty(0);

    private ObservableList<Card> communityCards = FXCollections.observableList(new ArrayList<>());
    private ObservableList<PlayerHandInfo> playerHandInfo = FXCollections.observableList(new ArrayList<>());
    private IntegerProperty pot = new SimpleIntegerProperty(0);
    private BooleanProperty betActive = new SimpleBooleanProperty(false);
    private IntegerProperty maxBet = new SimpleIntegerProperty(0);

    private BooleanProperty disableHandMenu = new SimpleBooleanProperty(false);

    @FXML private void initialize() {
        handMenu.visibleProperty().bind(handInProgress);
        gameMenu.visibleProperty().bind(gameOn.not().and(handsPlayed.isNotEqualTo(totalHands)));
        betweenHandsMenu.visibleProperty().bind(
                handInProgress.not()
                        .and(gameOn.or(handsPlayed.isEqualTo(totalHands)))
        );

        gameInfoPane.visibleProperty().bind(xmlLoaded);
        gameInfoPane.managedProperty().bind(xmlLoaded);

        gameMenuController.setParentController(this);
        gameMenuController.bindXmlLoaded(xmlLoaded);
        handMenuController.setParentController(this);
        betweenHandsMenuController.setParentController(this);
        betweenHandsMenuController.setPlayerGameInfoProperty(playerGameInfo);

        gameInfoPaneController.bindGameStatusProperties(
                playerGameInfo,
                handsPlayed,
                totalHands,
                smallBlind,
                bigBlind,
                initialSmallBlind,
                initialBigBlind,
                buyIn
        );

        gameBoardController.bindHandStatusProperties(
                communityCards,
                playerHandInfo,
                pot
        );

        handMenuController.bindHandStatusProperties(
                maxBet,
                betActive,
                disableHandMenu
        );

        betweenHandsMenuController.bindButtonDisabledStates(
                handsPlayed,
                totalHands
        );
    }

    public void setEngine(PokerEngine engine) {
        this.engine = engine;
        gameMenuController.setEngine(this.engine);
        handMenuController.setEngine(this.engine);
        gameBoardController.setEngine(this.engine);
        betweenHandsMenuController.setEngine(this.engine);
    }

    public void updateGameStatus() {
        playerGameInfo.clear();
        playerGameInfo.addAll(engine.getGameStatus());
        handsPlayed.setValue(engine.getHandsPlayed());
        totalHands.setValue(engine.getHandsCount());
        smallBlind.setValue(engine.getSmallBlind());
        bigBlind.setValue(engine.getBigBlind());
        initialSmallBlind.setValue(engine.getInitialSmallBlind());
        initialBigBlind.setValue(engine.getInitialBigBlind());
        buyIn.setValue(engine.getBuyIn());
        handInProgress.setValue(engine.isHandInProgress());
        gameOn.setValue(this.engine.isGameOn());
    }

    public void nextTurn() {
        if (!engine.isRoundInProgress()) {
            engine.nextRound();
        }

        if (!engine.isHandInProgress()) {
            finishHand();
        }

        updateHandStatus();

        Thread th = new Thread(() -> {
            try {
                Platform.runLater(() -> disableHandMenu.setValue(true));

                while (engine.isHandInProgress() && !engine.isHumanTurn()) {
                    Thread.sleep(1000);

                    engine.playComputerTurn();

                    if (!engine.isRoundInProgress()) {
                        engine.nextRound();
                    }

                    if (!engine.isHandInProgress()) {
                        Platform.runLater(this::finishHand);
                    }

                    Platform.runLater(this::updateHandStatus);
                }
            }
            catch (InterruptedException ignored) {
            }
            finally {
                Platform.runLater(() -> disableHandMenu.setValue(false));
            }
        });

        th.start();
    }

    private void finishHand() {
        List<PlayerHandInfo> winners = engine.getWinners();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hand Over!");
        alert.setHeaderText("Winners:");

        if (winners.get(0).getRanking().equals("")) {
            alert.setContentText("All computers (all humans folded)");
        }
        else {
            alert.setContentText(winners
                    .stream()
                    .map(winner -> String.format(
                            "%s (+%d chips) -> %s",
                            winner.getName(),
                            winner.getChipsWon(),
                            winner.getRanking()
                    ))
                    .collect(Collectors.joining("\n")));
        }

        alert.showAndWait();
        resetHandStatus();
        updateGameStatus();
    }

    public void updateHandStatus() {
        resetHandStatus();
        communityCards.addAll(engine.getCommunityCards());
        playerHandInfo.addAll(engine.getHandStatus());
        playerGameInfo.addAll(engine.getGameStatus());
        pot.setValue(engine.getPot());
        maxBet.setValue(engine.getMaxBet());
        betActive.setValue(engine.isBetActive());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        gameMenuController.setStage(this.stage);
        handMenuController.setStage(this.stage);
    }

    public void updateXmlLoaded() {
        xmlLoaded.setValue(this.engine.isXmlLoaded());
        updateGameStatus();
    }

    public void resetHandStatus() {
        communityCards.clear();
        playerHandInfo.clear();
        playerGameInfo.clear();
        pot.setValue(0);
        betActive.setValue(false);
        maxBet.setValue(0);
    }
}