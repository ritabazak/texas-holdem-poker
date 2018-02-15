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
            if (typeStr.equalsIgnoreCase("Human")) {
                return HUMAN;
            }

            return COMPUTER;
        }
    }

    protected PlayerType type;
    protected int id;
    protected String name;

    protected Player(Player player) {
        this.type = player.getType();
        this.id = player.getId();
        this.name = player.getName();
    }

    public Player(int id, String name, Player.PlayerType type) {
        this.id = id;
        this.name = name;
        this.type = type;
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
}
