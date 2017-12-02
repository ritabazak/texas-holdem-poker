public class PlayerInfo {
    public enum PlayerType {
        HUMAN, COMPUTER;

        public String toShortString() {
             return (this == HUMAN) ? "H" : "C";
        }
    }
    public enum PlayerState {
        NONE, DEALER, SMALL, BIG;

        public String toShortString() {
            switch (this) {
                case DEALER:
                    return "D";
                case BIG:
                    return "B";
                case SMALL:
                    return "S";
                default:
                case NONE:
                    return "";
            }
        }
    }

    private final PlayerType type;
    private final PlayerState state;
    private final int chips;


    public PlayerInfo(Player player,
                      boolean isDealer,
                      boolean isSmall,
                      boolean isBig) {
        this.type = player.getType().toPlayerInfo();
        this.chips = player.getChips();

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
    }

    public PlayerType getType() {
        return type;
    }
    public PlayerState getState() {
        return state;
    }
    public int getChips() {
        return chips;
    }
}
