package internals;

public class GamePlayer extends Player {
    private int buyIns = 1;
    private int wins = 0;

    public GamePlayer(int id, String name, Player.PlayerType type, int buyIn) {
        super(id, name, type, buyIn);
    }

    public void addBuyIn(int buyIn) {
        chips += buyIn;
        buyIns++;
    }

    public int getBuyIns() {
        return buyIns;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        wins++;
    }
}