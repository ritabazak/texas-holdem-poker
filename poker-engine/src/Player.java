import java.util.ArrayList;
import java.util.List;


public class Player {

    public void subtractChips(int chips) {
        this.chips -= chips;
    }

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
    }

    protected PlayerType type;
    protected int chips;

    protected Player(Player.PlayerType type, int chips) {
        this.type = type;
        this.chips = chips;
    }

    public PlayerType getType() {
        return type;
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int chips) {
        this.chips += chips;
    }
}
