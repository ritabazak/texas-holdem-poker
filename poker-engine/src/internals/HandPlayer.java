package internals;

import com.rundef.poker.HandRanking;
import immutables.Card;

public class HandPlayer extends Player {
    private final Card firstCard;
    private final Card secondCard;
    private final GamePlayer originalPlayer;
    private int bet;
    private boolean folded = false;
    private boolean winner;
    private String ranking = "";
    private int chipsWon = 0;

    public HandPlayer(GamePlayer gamePlayer, Card firstCard, Card secondCard) {
        super(gamePlayer);

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

    @Override
    protected void addChips(int chips) {
        super.addChips(chips);
        originalPlayer.addChips(chips);
    }

    @Override
    protected void subtractChips(int chips) {
        super.subtractChips(chips);
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