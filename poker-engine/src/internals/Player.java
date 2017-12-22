package internals;

import immutables.PlayerInfo;

public class Player {
    public enum PlayerType {
        HUMAN, COMPUTER;

        public PlayerInfo.PlayerType toPlayerInfo() {
            switch (this) {
                case HUMAN:
                    return PlayerInfo.PlayerType.HUMAN;
                default:
                case COMPUTER:
                    return PlayerInfo.PlayerType.COMPUTER;
            }
        }

        public static PlayerType fromString(String typeStr) {
            if (typeStr.equals("Human")) {
                return HUMAN;
            }

            return COMPUTER;
        }
    }

    protected PlayerType type;
    protected int chips;
    protected int id;
    protected String name;

    protected Player(Player player) {
        this.type = player.getType();
        this.chips = player.getChips();
        this.id = player.getId();
        this.name = player.getName();
    }

    protected Player(int id, String name, Player.PlayerType type, int chips) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.chips = chips;
    }

    public PlayerType getType() {
        return type;
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

    protected void addChips(int chips) {
        this.chips += chips;
    }

    protected void subtractChips(int chips) {
        this.chips -= chips;
    }
}
