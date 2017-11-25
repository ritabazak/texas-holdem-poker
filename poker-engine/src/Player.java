import java.util.ArrayList;
import java.util.List;


public class Player {
    public enum PlayerType {
        HUMAN, COMPUTER;

        public PlayerInfo.PlayerType toPlayerInfo() {
            switch (this) {
                case HUMAN:
                    return PlayerInfo.PlayerType.HUMAN;
                default:
                case COMPUTER:
                    return PlayerInfo.PlayerType.COMPUTER;
            }
        }
    }

    private PlayerType type;
    private int chips;
    private int wins = 0;
    private int buyIns = 1;
    private List<Card> cards = new ArrayList<>();

    public Player(Player.PlayerType type, int buyIn) {
        this.type = type;
        this.chips = buyIn;
    }

    public PlayerType getType() {
        return type;
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int buyIn) {
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

    public void dealCards(Card cardA, Card cardB) {
        cards.clear();
        cards.add(cardA);
        cards.add(cardB);
    }
}
