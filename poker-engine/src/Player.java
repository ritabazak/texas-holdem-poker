import java.util.ArrayList;
import java.util.List;


public class Player {
    public enum Type {
        HUMAN, COMPUTER
    }

    private Type type;
    private int chips;
    private int wins = 0;
    private int buyIns = 1;
    private List<Card> cards = new ArrayList<>();

    public Player(Player.Type type, int buyIn) {
        this.type = type;
        this.chips = buyIn;
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int buyIn) {
        chips += buyIn;
        buyIns++;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        wins++;
    }

    public void dealCards(Card cardA, Card cardB) {
        cards.clear();
        cards.add(cardA);
        cards.add(cardB);
    }
}
