import immutables.Card;
import immutables.HandReplayData;
import immutables.PlayerGameInfo;
import immutables.PlayerHandInfo;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
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
    @FXML private ReplayMenuController replayMenuController;
    @FXML private GameInfoPaneController gameInfoPaneController;

    @FXML private Pane gameInfoPane;
    @FXML private Pane gameMenu;
    @FXML private Pane handMenu;
    @FXML private Pane betweenHandsMenu;
    @FXML private Pane replayMenu;

    private PokerEngine engine;
    private Stage stage;

    private BooleanProperty gameOn = new SimpleBooleanProperty(false);
    private BooleanProperty xmlLoaded = new SimpleBooleanProperty(false);
    private BooleanProperty handInProgress = new SimpleBooleanProperty(false);
    private BooleanProperty replayMode = new SimpleBooleanProperty(false);
    private ObservableList<PlayerGameInfo> playerGameInfo = FXCollections.observableList(new ArrayList<>());
    private IntegerProperty handsPlayed = new SimpleIntegerProperty(-1);
    private IntegerProperty totalHands = new SimpleIntegerProperty(0);
    private IntegerProperty smallBlind = new SimpleIntegerProperty(0);
    private IntegerProperty bigBlind = new SimpleIntegerProperty(0);
    private IntegerProperty initialSmallBlind = new SimpleIntegerProperty(0);
    private IntegerProperty initialBigBlind = new SimpleIntegerProperty(0);
    private IntegerProperty buyIn = new SimpleIntegerProperty(0);
    private BooleanProperty canStartHand = new SimpleBooleanProperty(true);

    private ObservableList<Card> communityCards = FXCollections.observableList(new ArrayList<>());
    private ObservableList<PlayerHandInfo> playerHandInfo = FXCollections.observableList(new ArrayList<>());
    private IntegerProperty pot = new SimpleIntegerProperty(0);
    private BooleanProperty betActive = new SimpleBooleanProperty(false);
    private IntegerProperty maxBet = new SimpleIntegerProperty(0);

    private ObservableList<HandReplayData> replayData = FXCollections.observableList(new ArrayList<>());
    private IntegerProperty replayStepNumber = new SimpleIntegerProperty(0);
    private ObjectBinding<HandReplayData> replayStep;

    private BooleanProperty disableHandMenu = new SimpleBooleanProperty(false);

    @FXML private void initialize() {
        gameMenu.visibleProperty().bind(
                gameOn.not()
                        .and(replayMode.not())
                        .and(handsPlayed.isNotEqualTo(totalHands))
        );

        handMenu.visibleProperty().bind(handInProgress);

        replayMenu.visibleProperty().bind(replayMode);

        betweenHandsMenu.visibleProperty().bind(
                handMenu.visibleProperty().not()
                        .and(gameMenu.visibleProperty().not())
                        .and(replayMenu.visibleProperty().not())
        );

        gameInfoPane.visibleProperty().bind(xmlLoaded);
        gameInfoPane.managedProperty().bind(xmlLoaded);

        gameMenuController.setParentController(this);
        gameMenuController.bindXmlLoaded(xmlLoaded);
        handMenuController.setParentController(this);
        betweenHandsMenuController.setParentController(this);
        betweenHandsMenuController.setPlayerGameInfoProperty(playerGameInfo);
        replayMenuController.setParentController(this);

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
                handInProgress,
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
                totalHands,
                canStartHand
        );

        replayMenuController.bindButtonDisabledStates(
                Bindings.size(replayData),
                replayStepNumber
        );
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        gameMenuController.setStage(this.stage);
        handMenuController.setStage(this.stage);
    }
    public void setEngine(PokerEngine engine) {
        this.engine = engine;
        gameMenuController.setEngine(this.engine);
        handMenuController.setEngine(this.engine);
        gameBoardController.setEngine(this.engine);
        betweenHandsMenuController.setEngine(this.engine);
    }
    public void nextTurn() {
        if (!engine.isRoundInProgress()) {
            engine.nextRound();
        }

        if (!engine.isHandInProgress()) {
            finishHand();
        }

        updateHandStatus();

        disableHandMenu.setValue(true);

        Thread th = new Thread(() -> {
            try {

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

        if (winners.size() == 0) {
            alert.setContentText("No winners.");
        }
        else if (winners.get(0).getRanking().equals("")) {
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
    public void updateHandStatus() {
        resetHandStatus();
        communityCards.addAll(engine.getCommunityCards());
        playerHandInfo.addAll(engine.getHandStatus());
        playerGameInfo.addAll(engine.getGameStatus());
        gameInfoPaneController.resortTable();
        pot.setValue(engine.getPot());
        maxBet.setValue(engine.getMaxBet());
        betActive.setValue(engine.isBetActive());
    }
    public void updateGameStatus() {
        playerGameInfo.clear();
        playerGameInfo.addAll(engine.getGameStatus());
        gameInfoPaneController.resortTable();
        handsPlayed.setValue(engine.getHandsPlayed());
        totalHands.setValue(engine.getHandsCount());
        smallBlind.setValue(engine.getSmallBlind());
        bigBlind.setValue(engine.getBigBlind());
        initialSmallBlind.setValue(engine.getInitialSmallBlind());
        initialBigBlind.setValue(engine.getInitialBigBlind());
        buyIn.setValue(engine.getBuyIn());
        handInProgress.setValue(engine.isHandInProgress());
        gameOn.setValue(engine.isGameOn());
        canStartHand.setValue(engine.canStartHand());
    }

    public void startReplayMode() {
        ObservableList<PlayerHandInfo> replayPlayers = FXCollections.observableList(new ArrayList<>());
        ObservableList<Card> replayCommunityCards = FXCollections.observableList(new ArrayList<>());

        replayStep = Bindings.valueAt(replayData, replayStepNumber);

        replayStep.addListener((observer, oldStep, newStep) -> {
            replayPlayers.clear();
            replayCommunityCards.clear();

            if (newStep != null) {
                replayPlayers.addAll(newStep.getPlayers());
                replayCommunityCards.addAll(newStep.getCommunityCards());
            }
        });

        replayMode.setValue(true);
        replayData.addAll(engine.getReplay());

        IntegerProperty replayPot = new SimpleIntegerProperty(replayStep.get().getPot());
        replayPot.bind(
                Bindings.createIntegerBinding(
                        () -> replayStep.get() == null? 0: replayStep.get().getPot(),
                        replayStep
                )
        );

        gameBoardController.bindHandStatusProperties(
                new SimpleBooleanProperty(false),
                replayCommunityCards,
                replayPlayers,
                replayPot
        );
    }
    public void endReplayMode() {
        replayStep.dispose();
        replayStep = null;
        replayMode.setValue(false);
        replayStepNumber.setValue(0);
        replayData.clear();

        gameBoardController.bindHandStatusProperties(
                handInProgress,
                communityCards,
                playerHandInfo,
                pot
        );

        updateHandStatus();
    }

    public void prevReplayStep() {
        if (replayStepNumber.get() > 0) {
            replayStepNumber.setValue(replayStepNumber.get() - 1);
        }
    }
    public void nextReplayStep() {
        if (replayStepNumber.get() < replayData.size() - 1) {
            replayStepNumber.setValue(replayStepNumber.get() + 1);
        }
    }

    public boolean isGameOn() {
        return gameOn.get();
    }
}