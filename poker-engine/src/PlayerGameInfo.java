public class PlayerGameInfo extends PlayerInfo {
    private final int buyIns;
    private final int handsWon;

    public PlayerGameInfo(GamePlayer player,
                          boolean isDealer,
                          boolean isSmall,
                          boolean isBig) {
        super(player, isDealer, isSmall, isBig);
        this.buyIns = player.getBuyIns();
        this.handsWon = player.getWins();
    }

    public int getBuyIns() {
        return buyIns;
    }
    public int getHandsWon() {
        return handsWon;
    }
}
