package immutables;

import internals.Player;

public class PlayerInfo {
    public enum PlayerType {
        HUMAN, COMPUTER;

        public String toShortString() {
             return (this == HUMAN) ? "H" : "C";
        }

    }

    private final PlayerType type;
    private final String name;
    private final int id;

    public PlayerInfo(Player player) {
        type = player.getType().toPlayerInfo();
        name = player.getName();
        id = player.getId();
    }

    public PlayerType getType() {
        return type;
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
