package servlets.request_response.lobby;

class GameResponse {
    private final int id;
    private final String title;
    private final String author;
    private final boolean gameOn;
    private final int handsCount;
    private final int buyIn;
    private final int seats;
    private final int playerCount;
    private final int initialSmallBlind;
    private final int initialBigBlind;
    private final boolean fixedBlinds;
    private final int blindAddition;
    private final int maxTotalRoundsBlinds;

    GameResponse(int id,
                 String title,
                 String author,
                 boolean gameOn,
                 int handsCount,
                 int buyIn,
                 int seats,
                 int playerCount,
                 int initialSmallBlind,
                 int initialBigBlind,
                 boolean fixedBlinds,
                 int blindAddition,
                 int maxTotalRoundsBlinds) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.gameOn = gameOn;
        this.handsCount = handsCount;
        this.buyIn = buyIn;
        this.seats = seats;
        this.playerCount = playerCount;
        this.initialSmallBlind = initialSmallBlind;
        this.initialBigBlind = initialBigBlind;
        this.fixedBlinds = fixedBlinds;
        this.blindAddition = blindAddition;
        this.maxTotalRoundsBlinds = maxTotalRoundsBlinds;
    }
}
