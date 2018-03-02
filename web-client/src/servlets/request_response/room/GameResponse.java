package servlets.request_response.room;

import java.util.List;

public class GameResponse {
    private final int id;
    private final String title;
    private final String author;
    private final boolean joinable;
    private final boolean gameOn;
    private final boolean handInProgress;
    private final int handIndex;
    private final int handsCount;
    private final int buyIn;
    private final int seats;
    private final int playerCount;
    private final int initialSmallBlind;
    private final int initialBigBlind;
    private final List<GamePlayerResponse> players;
    private final List<ChatMessageResponse> chat;

    GameResponse(int id,
                 String title,
                 String author,
                 boolean joinable,
                 boolean gameOn,
                 boolean handInProgress,
                 int handIndex,
                 int handsCount,
                 int buyIn,
                 int seats,
                 int playerCount,
                 int initialSmallBlind,
                 int initialBigBlind,
                 List<GamePlayerResponse> players,
                 List<ChatMessageResponse> chat) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.joinable = joinable;
        this.gameOn = gameOn;
        this.handInProgress = handInProgress;
        this.handIndex = handIndex;
        this.handsCount = handsCount;
        this.buyIn = buyIn;
        this.seats = seats;
        this.playerCount = playerCount;
        this.initialSmallBlind = initialSmallBlind;
        this.initialBigBlind = initialBigBlind;
        this.players = players;
        this.chat = chat;
    }
}
