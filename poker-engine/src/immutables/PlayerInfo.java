package immutables;

import internals.Player;

public class PlayerInfo {
    public enum PlayerType {
        HUMAN, COMPUTER;

        public String toShortString() {
             return (this == HUMAN) ? "H" : "C";
        }
    }
    public enum PlayerState {
        NONE, DEALER, SMALL, BIG, BIG_AND_DEALER;

        public String toShortString() {
            switch (this) {
                case DEALER:
                    return "D";
                case BIG:
                    return "B";
                case SMALL:
                    return "S";
                case BIG_AND_DEALER:
                    return "B+D";
                default:
                case NONE:
                    return "";
            }
        }
    }

    private final PlayerType type;
    private final PlayerState state;
    private final int chips;
    private final String name;
    private final int id;

    public PlayerInfo(Player player,
                      boolean isDealer,
                      boolean isSmall,
                      boolean isBig) {
        type = player.getType().toPlayerInfo();
        chips = player.getChips();
        name = player.getName();
        id = player.getId();

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

    public PlayerType getType() {
        return type;
    }
    public PlayerState getState() {
        return state;
    }
    public int getChips() {
        return chips;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", name, id);
    }
}
