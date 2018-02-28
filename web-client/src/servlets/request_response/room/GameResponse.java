package servlets.request_response.room;

import java.util.List;

public class GameResponse {
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
    private final List<GamePlayerResponse> players;


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
                 List<GamePlayerResponse> players) {
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
        this.players = players;
    }
}
