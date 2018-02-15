package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import exceptions.*;
import immutables.*;
import internals.*;
import javafx.concurrent.Task;

public class PokerEngine {
    private boolean gameOn = false;
    private GameConfig gameConfig;
    private Game game;
    private List<Player> players = new LinkedList<>();
    private int id = 0;

    public boolean isXmlLoaded() {
        return gameConfig != null;
    }
    public boolean isGameOn() {
        return gameOn || game.isHandInProgress();
    }
    public List<PlayerGameInfo> getGameStatus() {
        return game.getGameStatus();
    }
    public int getHandsCount() {
        return gameConfig.getHandsCount();
    }
    public Duration getElapsedTime() {
        return game.getElapsedTime();
    }
    public int getHandsPlayed() {
        return game.getHandsPlayed();
    }
    public int getMaxPot() {
        return game.getMaxPot();
    }
    public List<Integer> getHumanIndices() {
        return game.getHumanIndices();
    }
    public List<HandReplayData> getReplay() {
        return game.getReplay();
    }
    public List<PlayerHandInfo> getHandStatus() {
        return game.getHandStatus();
    }
    public List<Card> getCommunityCards() {
        return game.getCommunityCards();
    }
    public int getPot() {
        return game.getPot();
    }
    public boolean isHandInProgress() {
        return game.isHandInProgress();
    }
    public boolean isHumanTurn() {
        return game.isHumanTurn();
    }
    public int getMaxBet() {
        return game.getMaxBet();
    }
    public List<PlayerHandInfo> getWinners() {
        return game.getWinners();
    }
    public boolean isBetActive() {
        return game.isBetActive();
    }
    public boolean isRoundInProgress() {
        return game.isRoundInProgress();
    }
    public int getSmallBlind() {
        return game.getSmallBlind();
    }
    public int getBigBlind() {
        return game.getBigBlind();
    }
    public int getBuyIn() {
        return gameConfig.getBuyIn();
    }
    public int getInitialSmallBlind() {
        return gameConfig.getSmallBlind();
    }
    public int getInitialBigBlind() {
        return gameConfig.getBigBlind();
    }
    public int getPlayerIndexById(int id) {
        return game.getPlayerIndexById(id);
    }
    public boolean canStartHand() { return game.canStartHand(); }

    private File checkFileStep1(String xmlFilePath)
            throws FileNotFoundException,
            BadFileExtensionException {

        File file = new File(xmlFilePath);

        if (!Files.exists(file.toPath())) {
            throw new FileNotFoundException();
        }

        if (!file.getPath().toLowerCase().endsWith(".xml")) {
            throw new BadFileExtensionException();
        }

        return file;
    }

    private GameConfig checkFileStep2(GameConfig config)
            throws InvalidHandsCountException,
            InvalidBlindsException,
            DuplicatePlayerIdException {

        if (config.getHandsCount() % config.getPlayerCount() != 0) {
            throw new InvalidHandsCountException();
        }

        if (config.getSmallBlind() > config.getBigBlind()) {
            throw new InvalidBlindsException();
        }

        if (config.getConfigPlayers() != null) {
            Set<Integer> ids = config
                    .getConfigPlayers()
                    .stream()
                    .map(GameConfig.ConfigPlayer::getId)
                    .collect(Collectors.toSet());

            if (ids.size() < config.getPlayerCount()) {
                throw new DuplicatePlayerIdException();
            }
        }

        return config;
    }

    public void loadConfigFile(String xmlFilePath)
            throws FileNotFoundException,
            BadFileExtensionException,
            InvalidHandsCountException,
            InvalidBlindsException,
            DuplicatePlayerIdException {

        File file = checkFileStep1(xmlFilePath);

        gameConfig = checkFileStep2(new GameConfig(file));

        initGame();
    }

    public Task<Void> loadConfigFileTask(String xmlFilePath) {
        return new Task<Void>() {
            @Override
            protected Void call()
                    throws FileNotFoundException,
                    BadFileExtensionException,
                    InvalidHandsCountException,
                    InvalidBlindsException,
                    DuplicatePlayerIdException {
                try {
                    updateProgress(0, 3);

                    File file = checkFileStep1(xmlFilePath);

                    for (int i = 10; i > 0; i--) {
                        Thread.sleep(50);
                        updateProgress(1.0/i, 3);
                    }

                    gameConfig = checkFileStep2(new GameConfig(file));

                    for (int i = 10; i > 0; i--) {
                        Thread.sleep(50);
                        updateProgress(1 + 1.0/i, 3);
                    }

                    initGame();

                    for (int i = 10; i > 0; i--) {
                        Thread.sleep(50);
                        updateProgress(2 + 1.0/i, 3);
                    }
                }
                catch (InterruptedException ignored) {
                }

                return null;
            }
        };
    }

    private void initGame() {
        switch (gameConfig.getGameType()) {
            case BASIC:
                game = new BasicGame(gameConfig);
                break;
            case MULTIPLAYER:
                game = new MultiplayerGame(gameConfig);
                break;
            case DYNAMIC_MULTIPLAYER:
                //TODO: Implement this
                break;
        }
    }
    public void startGame() {
        gameOn = true;
        game.startTimer();
    }
    public void startHand() {
        if (game.startHand() == getHandsCount()) {
            endGame();
        }
    }
    public void addBuyIn(int playerIndex) {
        game.addBuyIn(playerIndex);
    }
    public void retirePlayer(int playerIndex) {
        if (!game.retirePlayer(playerIndex)) {
            endGame();
        }
    }
    public void fold() {
        game.fold();
    }
    public void raise(int raiseAmount) {
        game.raise(raiseAmount);
    }
    public void placeBet(int bet) {
        game.placeBet(bet);
    }
    public void call() {
        game.call();
    }
    public void check() {
        game.check();
    }
    public void playComputerTurn() {
        game.playComputerTurn();
    }
    public void nextRound() {
        game.nextRound();
    }
    public void endGame() {
        gameOn = false;
        initGame();
    }


    private boolean isUserTaken(String name) {
        return players.stream().anyMatch(p -> p.getName().equals(name));
    }
    public void addUser(String name, Player.PlayerType type) throws UserNameAlreadyTakenException {
        if (isUserTaken(name)) {
            throw new UserNameAlreadyTakenException();
        }
        players.add(new Player(id++, name, type));
    }

    public List<PlayerInfo> getPlayers() {
        return players.stream().map(PlayerInfo::new).collect(Collectors.toList());
    }

}