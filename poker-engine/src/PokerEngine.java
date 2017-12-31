import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import exceptions.BadFileExtensionException;
import exceptions.DuplicatePlayerIdException;
import exceptions.InvalidBlindsException;
import exceptions.InvalidHandsCountException;
import immutables.PlayerGameInfo;
import immutables.PlayerHandInfo;
import internals.BasicGame;
import immutables.Card;
import internals.Game;
import internals.GameConfig;
import internals.MultiplayerGame;

public class PokerEngine {
    private boolean gameOn = false;
    private GameConfig gameConfig;
    private Game game;

    public boolean isXmlLoaded() {
        return gameConfig != null;
    }
    public boolean isGameOn() {
        return gameOn || game.isHandInProgress();
    }
    List<PlayerGameInfo> getGameStatus() {
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

    public void loadConfigFile(String xmlFilePath)
            throws FileNotFoundException,
            BadFileExtensionException,
            InvalidHandsCountException,
            InvalidBlindsException,
            DuplicatePlayerIdException {

        File f = new File(xmlFilePath);

        if (!Files.exists(f.toPath())) {
            throw new FileNotFoundException();
        }

        if (!f.getPath().toLowerCase().endsWith(".xml")) {
            throw new BadFileExtensionException();
        }

        GameConfig temp = new GameConfig(f);

        if (temp.getHandsCount() % temp.getPlayerCount() != 0) {
            throw new InvalidHandsCountException();
        }

        if (temp.getSmallBlind() > temp.getBigBlind()) {
            throw new InvalidBlindsException();
        }

        if (temp.getConfigPlayers() != null) {
            Set<Integer> ids = temp
                    .getConfigPlayers()
                    .stream()
                    .map(GameConfig.ConfigPlayer::getId)
                    .collect(Collectors.toSet());

            if (ids.size() < temp.getPlayerCount()) {
                throw new DuplicatePlayerIdException();
            }
        }

        gameConfig = temp;

        initGame();
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
            gameOn = false;
        }
    }
    public void addBuyIn(int playerIndex) {
        game.addBuyIn(playerIndex);
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
}