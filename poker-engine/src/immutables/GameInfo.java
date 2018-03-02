package immutables;

import internals.Game;

import java.util.List;

public class GameInfo {
    private final int id;
    private final String title;
    private final String author;
    private final boolean gameOn;
    private final boolean joinable;
    private final boolean handInProgress;
    private final int buyIn;
    private final int handIndex;
    private final int handsCount;
    private final int seats;
    private final int playerCount;
    private final int initialSmallBlind;
    private final int initialBigBlind;
    private final boolean fixedBlinds;
    private final int blindAddition;
    private final int maxTotalRoundsBlinds;
    private final List<PlayerGameInfo> players;
    private final List<ChatMessage> chat;

    public GameInfo(Game game) {
        id = game.getId();
        title = game.getTitle();
        author = game.getAuthor();
        gameOn = game.isGameOn();
        joinable = game.isJoinable();
        handInProgress = game.isHandInProgress();
        buyIn = game.getBuyIn();
        handIndex = game.getHandIndex();
        handsCount = game.getHandsCount();
        seats = game.getSeats();
        playerCount = game.getPlayerCount();
        initialSmallBlind = game.getInitialSmallBlind();
        initialBigBlind = game.getInitialBigBlind();
        fixedBlinds = game.isFixedBlinds();
        blindAddition = game.getBlindAddition();
        maxTotalRoundsBlinds = game.getMaxTotalRoundsBlinds();
        players = game.getGameStatus();
        chat = game.getChat();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public boolean isHandInProgress() {
        return handInProgress;
    }

    public int getBuyIn() {
        return buyIn;
    }

    public int getHandIndex() {
        return handIndex;
    }

    public int getHandsCount() {
        return handsCount;
    }

    public int getSeats() {
        return seats;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getInitialSmallBlind() {
        return initialSmallBlind;
    }

    public int getInitialBigBlind() {
        return initialBigBlind;
    }

    public boolean isFixedBlinds() {
        return fixedBlinds;
    }

    public int getBlindAddition() {
        return blindAddition;
    }

    public int getMaxTotalRoundsBlinds() {
        return maxTotalRoundsBlinds;
    }

    public List<PlayerGameInfo> getPlayers() {
        return players;
    }

    public boolean isJoinable() {
        return joinable;
    }

    public List<ChatMessage> getChat() {
        return chat;
    }
}
