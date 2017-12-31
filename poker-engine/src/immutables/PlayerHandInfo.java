package immutables;

import internals.HandPlayer;

public class PlayerHandInfo extends PlayerInfo {
    private final int bet;
    private final Card firstCard;
    private final Card secondCard;
    private final boolean current;
    private final String ranking;
    private final boolean folded;
    private final int chipsWon;

    public PlayerHandInfo(HandPlayer player,
                          boolean isDealer,
                          boolean isSmall,
                          boolean isBig,
                          boolean isCurrent,
                          boolean showCards) {
        super(player, isDealer, isSmall, isBig);

        this.current = isCurrent;
        this.bet = player.getBet();
        this.ranking = player.getRanking();
        this.folded = player.isFolded();
        this.chipsWon = player.getChipsWon();

        if (showCards) {
            this.firstCard = player.getFirstCard();
            this.secondCard = player.getSecondCard();
        }
        else {
            this.firstCard = this.secondCard = Card.noneCard;
        }
    }

    public int getBet() {
        return bet;
    }
    public Card getFirstCard() {
        return firstCard;
    }
    public Card getSecondCard() {
        return secondCard;
    }
    public boolean isCurrent() {
        return current;
    }
    public String getRanking() {
        return ranking;
    }
    public boolean isFolded() {
        return folded;
    }
    public int getChipsWon() { return chipsWon; }
}