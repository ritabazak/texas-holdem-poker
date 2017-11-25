public class BasicGame extends Game {
    public BasicGame(int buyIn) {
        super(4, buyIn);

        players.add(new Player(Player.Type.HUMAN, buyIn));
        players.add(new Player(Player.Type.COMPUTER, buyIn));
        players.add(new Player(Player.Type.COMPUTER, buyIn));
        players.add(new Player(Player.Type.COMPUTER, buyIn));

        shufflePlayers();
        dealHand();
    }
}