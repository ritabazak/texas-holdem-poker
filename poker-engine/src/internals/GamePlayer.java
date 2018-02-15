package internals;

public class GamePlayer extends Player {
    private int buyIns = 1;
    private int wins = 0;
    private int chips;

    public GamePlayer(int id, String name, Player.PlayerType type, int buyIn) {
        super(id, name, type);
        chips = buyIn;
    }

    public void addBuyIn(int buyIn) {
        chips += buyIn;
        buyIns++;
    }

    public int getChips() {
        return chips;
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

    public void addChips(int chips) {
        this.chips += chips;
    }

    public void subtractChips(int chips) {
        this.chips -= chips;
    }
}