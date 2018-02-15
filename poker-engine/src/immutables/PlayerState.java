package immutables;

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