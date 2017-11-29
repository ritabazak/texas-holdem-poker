public final class PlayerInfo {
    public enum PlayerType {
        HUMAN, COMPUTER
    }

    public enum PlayerState {
        NONE, DEALER, SMALL, BIG
    }

    private final PlayerType type;
    private final PlayerState state;
    private final int chips;
    private final int buyIns;
    private final int handsWon;
    private final int bet;

    public PlayerInfo(Player player,
                      boolean isDealer,
                      boolean isSmall,
                      boolean isBig,
                      int bet) {
        this.type = player.getType().toPlayerInfo();
        this.chips = player.getChips();
        this.buyIns = player.getBuyIns();
        this.handsWon = player.getWins();

        if (isDealer) {
            this.state = PlayerState.DEALER;
        }
        else if (isSmall) {
            this.state = PlayerState.SMALL;
        }
        else if (isBig) {
            this.state = PlayerState.BIG;
        }
        else {
            this.state = PlayerState.NONE;
        }

        this.bet = bet;
    }

    public PlayerType getType() {
        return type;
    }
    public PlayerState getState() {
        return state;
    }
    public int getBet() {
        return bet;
    }
    public int getChips() {
        return chips;
    }
    public int getBuyIns() {
        return buyIns;
    }
    public int getHandsWon() {
        return handsWon;
    }
}
