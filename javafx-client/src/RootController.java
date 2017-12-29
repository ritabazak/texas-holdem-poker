import immutables.Card;
import immutables.PlayerGameInfo;
import immutables.PlayerHandInfo;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RootController {
    @FXML private GameBoardController gameBoardController;
    @FXML private GameMenuController gameMenuController;
    @FXML private HandMenuController handMenuController;
    @FXML private GameInfoPaneController gameInfoPaneController;

    @FXML private Pane gameInfoPane;
    @FXML private Pane gameMenu;
    @FXML private Pane handMenu;

    private PokerEngine engine;
    private Stage stage;

    private BooleanProperty isGameOn = new SimpleBooleanProperty(false);
    private BooleanProperty xmlLoaded = new SimpleBooleanProperty(false);
    private ObservableList<PlayerGameInfo> playerGameInfo = FXCollections.observableList(new ArrayList<>());
    private IntegerProperty handsPlayed = new SimpleIntegerProperty(0);
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

    @FXML private void initialize() {
        handMenu.visibleProperty().bind(isGameOn);
        gameMenu.visibleProperty().bind(isGameOn.not());

        gameInfoPane.visibleProperty().bind(xmlLoaded);
        gameInfoPane.managedProperty().bind(xmlLoaded);

        gameMenuController.setParentController(this);
        handMenuController.setParentController(this);
        gameMenuController.bindXmlLoaded(xmlLoaded);

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
                betActive
        );
    }

    public void setEngine(PokerEngine engine) {
        this.engine = engine;
        updateGameOn();
        gameMenuController.setEngine(this.engine);
        handMenuController.setEngine(this.engine);
        gameBoardController.setEngine(this.engine);
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
    }

    public void nextTurn() {
        if (!engine.isHandInProgress()) {
            finishHand();
        }

        if (!engine.isRoundInProgress()) {
            engine.nextRound();
        }

        updateHandStatus();

        Thread th = new Thread(() -> {
            try {
                while (!engine.isHumanTurn()) {
                    Thread.sleep(1000);

                    engine.playComputerTurn();

                    if (!engine.isHandInProgress()) {
                        finishHand();
                    }

                    if (!engine.isRoundInProgress()) {
                        engine.nextRound();
                    }
                    Platform.runLater(this::updateHandStatus);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        th.start();
    }

    private void finishHand() {
        Alert a = new Alert(Alert.AlertType.INFORMATION, "Egg");
        a.showAndWait();

        updateGameStatus();
        communityCards.clear();
        playerHandInfo.clear();
        pot.setValue(0);
        maxBet.setValue(0);
        betActive.setValue(false);
    }

    public void updateHandStatus() {
        communityCards.clear();
        communityCards.addAll(engine.getCommunityCards());
        playerHandInfo.clear();
        playerHandInfo.addAll(engine.getHandStatus());
        pot.setValue(engine.getPot());
        maxBet.setValue(engine.getMaxBet());
        betActive.setValue(engine.isBetActive());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        gameMenuController.setStage(this.stage);
        handMenuController.setStage(this.stage);
    }

    public void updateGameOn() {
        isGameOn.setValue(this.engine.isGameOn());
    }

    public void updateXmlLoaded() {
        xmlLoaded.setValue(this.engine.isXmlLoaded());
        updateGameStatus();
    }
}