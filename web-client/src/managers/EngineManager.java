package managers;

import javax.servlet.ServletContext;
import engine.PokerEngine;
import exceptions.*;
import immutables.GameInfo;
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

    public void addGame(String xmlContent, String username) throws InvalidBlindsException, InvalidHandsCountException, DuplicatePlayerIdException, DuplicateGameTitleException {
        engine.addGame(xmlContent, username);
    }

    public void removePlayer(String username) {
        engine.removePlayer(username);
    }

    public void joinGame(int id, String username) throws GameFullException, GameAlreadyInProgressException {
        engine.joinGame(id, username);
    }
}