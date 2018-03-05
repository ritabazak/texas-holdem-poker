package engine;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import exceptions.*;
import immutables.*;
import internals.*;

public class PokerEngine {
    private List<Game> games = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private int playerId = 0;
    private int gameId = 0;

    private Game getGame(int gameId) {
        return games.stream().filter(g -> g.getId() == gameId).findFirst().orElse(null);
    }

    private Player getPlayer(String username) {
        return players.stream().filter(p -> p.getName().equals(username)).findFirst().orElse(null);
    }

    public void addGame(String xmlContent, String author)
            throws InvalidBlindsException,
            InvalidHandsCountException,
            DuplicatePlayerIdException,
            DuplicateGameTitleException,
            UnsupportedGameTypeException {

        GameConfig config = new GameConfig(xmlContent);
        config = verifyGameConfig(config);
        games.add(new Game(gameId++, config, author));
    }

    private GameConfig verifyGameConfig(GameConfig config)
            throws InvalidHandsCountException,
            InvalidBlindsException,
            DuplicatePlayerIdException,
            DuplicateGameTitleException,
            UnsupportedGameTypeException {

        if (config.getGameType() != GameConfig.GameType.DYNAMIC_MULTIPLAYER) {
            throw new UnsupportedGameTypeException();
        }

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

    public void addBuyIn(int gameId, String username) {
        getGame(gameId).addBuyIn(username);
    }

    public void retirePlayer(int gameId, String username) {
        Game g = getGame(gameId);

        g.retirePlayer(username);

        if (g.getPlayerCount() == 0) {
            int i = games.indexOf(g);
            games.set(i, new Game(g.getId(), g.getConfig(), g.getAuthor()));
        }
    }

    public void fold(int gameId) {
        getGame(gameId).fold();
    }

    public void raise(int gameId, int raiseAmount) {
        getGame(gameId).raise(raiseAmount);
    }

    public void placeBet(int gameId, int bet) {
        getGame(gameId).placeBet(bet);
    }

    public void call(int gameId) {
        getGame(gameId).call();
    }

    public void check(int gameId) {
        getGame(gameId).check();
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
        return games.stream()
                .map(Game::getId)
                .map(this::getGameInfo)
                .collect(Collectors.toList());
    }

    public void removePlayer(String name) {
        players.stream().filter(p -> p.getName().equals(name)).findFirst().ifPresent(players::remove);
    }

    public void joinGame(int gameId, String username) throws GameFullException, GameAlreadyInProgressException {
        getGame(gameId).addPlayer(getPlayer(username));
    }

    public GameInfo getGameInfo(int gameId) {
        return new GameInfo(getGame(gameId));
    }

    public HandInfo getHandInfo(int gameId, String username) {
        return getGame(gameId).getHandInfo(username);
    }

    public void setPlayerReady(int gameId, String username, boolean ready) {
        getGame(gameId).setPlayerReady(username, ready);
    }

    public void addMessage(int gameId, String username, String message) {
        getGame(gameId).addMessage(username, message);
    }
}