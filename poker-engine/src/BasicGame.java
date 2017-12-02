public class BasicGame extends Game {
    public BasicGame(GameConfig config) {
        super(config);

        int buyIn = config.getBuyIn();

        players.add(new GamePlayer(Player.PlayerType.HUMAN, buyIn));
        players.add(new GamePlayer(Player.PlayerType.COMPUTER, buyIn));
        players.add(new GamePlayer(Player.PlayerType.COMPUTER, buyIn));
        players.add(new GamePlayer(Player.PlayerType.COMPUTER, buyIn));

        shufflePlayers();
    }
}