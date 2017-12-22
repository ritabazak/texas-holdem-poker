package internals;

public class BasicGame extends Game {
    public BasicGame(GameConfig config) {
        super(config);

        int buyIn = config.getBuyIn();

        players.add(new GamePlayer(0, "Player 1", Player.PlayerType.HUMAN, buyIn));
        players.add(new GamePlayer(1, "Player 2", Player.PlayerType.COMPUTER, buyIn));
        players.add(new GamePlayer(2, "Player 3", Player.PlayerType.COMPUTER, buyIn));
        players.add(new GamePlayer(3, "Player 4", Player.PlayerType.COMPUTER, buyIn));

        shufflePlayers();
    }
}