package engine;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import exceptions.*;
import immutables.*;
import internals.*;

public class PokerEngine {
    private Game game;
    private List<Game> games = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private int playerId = 0;
    private int gameId = 0;

    private Game getGame(int id) {
        return games.stream().filter(g -> g.getId() == id).findFirst().orElse(null);
    }
    private Player getPlayer(String username) {
        return players.stream().filter(p -> p.getName().equals(username)).findFirst().orElse(null);
    }
    public void addGame(String xmlContent, String author) throws InvalidBlindsException, InvalidHandsCountException, DuplicatePlayerIdException, DuplicateGameTitleException {
        GameConfig config = new GameConfig(xmlContent);
        config = verifyGameConfig(config);
        games.add(new Game(gameId++, config, author));
    }
    public List<PlayerGameInfo> getGameStatus() {
        return game.getGameStatus();
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
    public int getPlayerIndexById(int id) {
        return game.getPlayerIndexById(id);
    }
    public boolean canStartHand() { return game.canStartHand(); }

    private GameConfig verifyGameConfig(GameConfig config)
            throws InvalidHandsCountException,
            InvalidBlindsException,
            DuplicatePlayerIdException, DuplicateGameTitleException {

        //TODO: add tests for Project3
        //TODO: verify gameConfig.getGameType() == DYNAMIC_MULTIPLAYER
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
         if (games.stream().anyMatch(g -> g.getTitle().equals(config.getTitle()))) {
            throw new DuplicateGameTitleException();
         }
        return config;
    }

    /*public void startHand() {
        if (game.startHand() == getHandsCount()) {
            endGame();
        }
    }*/
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
        //gameOn = false;
        //initGame();
    }


    private boolean isUsernameTaken(String username) {
        return players.stream().anyMatch(p -> p.getName().equals(username));
    }
    public void addPlayer(String username, Player.PlayerType type) throws UsernameAlreadyTakenException {
        if (isUsernameTaken(username)) {
            throw new UsernameAlreadyTakenException();
        }
        players.add(new Player(playerId++, username, type));
    }

    public List<PlayerInfo> getPlayers() {
        return players.stream().map(PlayerInfo::new).collect(Collectors.toList());
    }

    public List<GameInfo> getGames() {
        return games.stream().map(GameInfo::new).collect(Collectors.toList());
    }

    public void removePlayer(String name) {
        players.stream().filter(p -> p.getName().equals(name)).findFirst().ifPresent(players::remove);
    }

    public void joinGame(int id, String username) throws GameFullException, GameAlreadyInProgressException {
        getGame(id).addPlayer(getPlayer(username));
    }
}