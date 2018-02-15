package internals;

import com.rundef.poker.HandRanking;
import immutables.Card;

public class HandPlayer extends Player {
    private final Card firstCard;
    private final Card secondCard;
    private final GamePlayer originalPlayer;
    private int bet;
    private int chips;
    private boolean folded = false;
    private boolean winner;
    private String ranking = "";
    private int chipsWon = 0;

    public HandPlayer(GamePlayer gamePlayer, Card firstCard, Card secondCard) {
        super(gamePlayer);

        originalPlayer = gamePlayer;
        chips = originalPlayer.getChips();
        this.firstCard = firstCard;
        this.secondCard = secondCard;
    }

    public Card getFirstCard() {
        return firstCard;
    }
    public Card getSecondCard() {
        return secondCard;
    }
    public int getBet() {
        return bet;
    }
    public int getChips() {
        return chips;
    }
    public boolean isFolded() {
        return folded;
    }
    public int getChipsWon() { return chipsWon; }

    public void fold() {
        folded = true;
    }

    public void placeBet(int amount) {
        subtractChips(amount - bet);
        bet = amount;
    }

    public int collectBet() {
        int temp = bet;
        bet = 0;
        return temp;
    }

    public void win(int winnings) {
        addChips(winnings);
        this.chipsWon = winnings;
        originalPlayer.addWin();
    }

    private void addChips(int chips) {
        this.chips += chips;
        originalPlayer.addChips(chips);
    }

    private void subtractChips(int chips) {
        this.chips -= chips;
        originalPlayer.subtractChips(chips);
    }

    public void setResults(boolean winner, HandRanking ranking) {
        this.winner = winner;
        this.ranking = ranking.toString();
    }

    public boolean isWinner() {
        return winner;
    }
    public String getRanking() {
        return ranking;
    }
}