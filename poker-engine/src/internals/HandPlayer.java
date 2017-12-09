package internals;

import com.rundef.poker.HandRanking;
import immutables.Card;

public class HandPlayer extends Player {
    private final Card firstCard;
    private final Card secondCard;
    private int bet;
    private boolean folded = false;
    private final GamePlayer originalPlayer;
    private boolean winner;
    private String ranking = "";

    public HandPlayer(GamePlayer gamePlayer, Card firstCard, Card secondCard) {
        super(gamePlayer.type, gamePlayer.chips);

        originalPlayer = gamePlayer;
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
    public boolean isFolded() {
        return folded;
    }

    public void fold() {
        folded = true;
    }

    public void placeBet(int amount) {
        subtractChips(amount - bet);
        originalPlayer.subtractChips(amount - bet);
        bet = amount;
    }

    public int collectBet() {
        int temp = bet;
        bet = 0;
        return temp;
    }

    @Override
    public void addChips(int chips) {
        super.addChips(chips);
        originalPlayer.addChips(chips);
        originalPlayer.addWin();
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