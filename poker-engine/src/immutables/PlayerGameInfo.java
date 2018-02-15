package immutables;

import internals.GamePlayer;

public class PlayerGameInfo extends PlayerInfo {
    private final int buyIns;
    private final int handsWon;
    private final int chips;
    private final PlayerState state;

    public PlayerGameInfo(GamePlayer player,
                          boolean isDealer,
                          boolean isSmall,
                          boolean isBig) {
        super(player);
        this.buyIns = player.getBuyIns();
        this.handsWon = player.getWins();
        this.chips = player.getChips();

        if (isDealer && isBig) {
            state = PlayerState.BIG_AND_DEALER;
        }
        else if (isDealer) {
            state = PlayerState.DEALER;
        }
        else if (isSmall) {
            state = PlayerState.SMALL;
        }
        else if (isBig) {
            state = PlayerState.BIG;
        }
        else {
            state = PlayerState.NONE;
        }
    }

    public PlayerState getState() {
        return state;
    }
    public int getBuyIns() {
        return buyIns;
    }
    public int getHandsWon() {
        return handsWon;
    }
    public int getChips() {
        return chips;
    }
}
