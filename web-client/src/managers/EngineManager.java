package managers;

import javax.servlet.ServletContext;
import engine.PokerEngine;
import exceptions.*;
import immutables.GameInfo;
import immutables.HandInfo;
import immutables.PlayerInfo;
import internals.Player;

import java.util.List;

public class EngineManager {
    private PokerEngine engine;

    public static EngineManager getEngineFromContext(ServletContext context) {
        if (context.getAttribute("ENGINE_MANAGER") == null) {
            context.setAttribute("ENGINE_MANAGER", new EngineManager());

        }

        return (EngineManager) context.getAttribute("ENGINE_MANAGER");
    }

    private EngineManager() {
        engine = new PokerEngine();
    }

    public synchronized void addPlayer(String username, String type) throws UsernameAlreadyTakenException {
        engine.addPlayer(username, Player.PlayerType.fromString(type));
    }

    public synchronized List<PlayerInfo> getPlayers() {
        return engine.getPlayers();
    }

    public synchronized List<GameInfo> getGames() {
        return engine.getGames();
    }

    public synchronized void addGame(String xmlContent, String username)
            throws InvalidBlindsException,
            InvalidHandsCountException,
            DuplicatePlayerIdException,
            DuplicateGameTitleException,
            UnsupportedGameTypeException {
        engine.addGame(xmlContent, username);
    }

    public synchronized void removePlayer(String username) {
        engine.removePlayer(username);
    }

    public synchronized void joinGame(int gameId, String username) throws GameFullException, GameAlreadyInProgressException {
        engine.joinGame(gameId, username);
    }

    public synchronized GameInfo getGameInfo(int gameId) {
        return engine.getGameInfo(gameId);
    }

    public synchronized HandInfo getHandInfo(int gameId, String username) {
        return engine.getHandInfo(gameId, username);
    }

    public synchronized void setPlayerReady(int gameId, String username, boolean ready) {
        engine.setPlayerReady(gameId, username, ready);
    }

    public synchronized void buyIn(int gameId, String username) {
        engine.addBuyIn(gameId, username);
    }

    public synchronized void leaveGame(int gameId, String username) {
        engine.retirePlayer(gameId, username);
    }

    public synchronized void fold(int gameId) {
        engine.fold(gameId);
    }

    public synchronized void check(int gameId) {
        engine.check(gameId);
    }

    public synchronized void call(int gameId) {
        engine.call(gameId);
    }

    public synchronized void bet(int gameId, int amount) {
        engine.placeBet(gameId, amount);
    }

    public synchronized void raise(int gameId, int amount) {
        engine.raise(gameId, amount);
    }

    public synchronized void addMessage(int gameId, String username, String message) {
        engine.addMessage(gameId, username, message);
    }
}