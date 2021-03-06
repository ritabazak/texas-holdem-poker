package internals;

import exceptions.GameAlreadyInProgressException;
import exceptions.GameFullException;
import immutables.*;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Game {
    private int id;
    private String title;
    private String author;
    private boolean gameOn = false;
    private GameConfig config;
    private List<GamePlayer> players = new ArrayList<>();
    private int seats;
    private int dealer = 0;
    private int handIndex = 0;
    private int smallBlind;
    private int bigBlind;
    private Hand hand;
    private List<ChatMessage> chat = new LinkedList<>();

    public Game(int id, GameConfig config, String author) {
        this.id = id;
        this.config = config;
        this.author = author;

        title = config.getTitle();
        seats = config.getPlayerCount();
        smallBlind = config.getSmallBlind();
        bigBlind = config.getBigBlind();
    }

    public int getId() {
        return id;
    }

    public boolean isGameOn() {
        return gameOn || isHandInProgress();
    }

    public int getBuyIn() {
        return config.getBuyIn();
    }

    public int getInitialSmallBlind() {
        return config.getSmallBlind();
    }

    public int getInitialBigBlind() {
        return config.getBigBlind();
    }

    public int getHandsCount() {
        return config.getHandsCount();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getSeats() {
        return seats;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public boolean isFixedBlinds() {
        return config.isFixedBlinds();
    }

    public int getBlindAddition() {
        return isFixedBlinds()? 0: config.getBlindAddition();
    }

    public int getMaxTotalRoundsBlinds() {
        return isFixedBlinds()? 0: config.getMaxTotalRounds();
    }

    protected int getSmallIndex() {
        return hand == null? (dealer + 1) % players.size(): hand.getSmallIndex();
    }

    protected int getBigIndex() {
        return hand == null? (dealer + 2) % players.size(): hand.getBigIndex();
    }

    public List<PlayerGameInfo> getGameStatus() {
        return IntStream.range(0, players.size())
                .mapToObj(i ->
                        new PlayerGameInfo(
                                players.get(i),
                                i == dealer,
                                i == getSmallIndex(),
                                i == getBigIndex()
                        )
                ).collect(toList());
    }

    public void addBuyIn(String username) {
        getPlayer(username).addBuyIn(config.getBuyIn());
    }

    public void retirePlayer(String username) {
        players.remove(getPlayer(username));

        if (players.size() <= 1 ||
                players.stream().noneMatch(player -> player.type == Player.PlayerType.HUMAN)) {
            gameOn = false;
        }
    }

    public void startHand() {
        hand = new Hand(
                players,
                dealer,
                smallBlind,
                bigBlind,
                handIndex == 0 ? 0 : hand.getPotRemainder()
        );

        dealer = getSmallIndex();

        if (!config.isFixedBlinds() && (handIndex+1) % players.size() == 0) {
            int maxBig = Math.min(
                    config.getHandsCount() / config.getPlayerCount() * config.getBlindAddition(),
                    config.getMaxTotalRounds() * config.getBlindAddition()
            ) + config.getBigBlind();

            if (bigBlind + config.getBlindAddition() <= maxBig) {
                smallBlind += config.getBlindAddition();
                bigBlind += config.getBlindAddition();
            }
        }

        if (++handIndex == getHandsCount()) {
            gameOn = false;
        }
    }

    public boolean isHandInProgress() {
        return hand != null && hand.handInProgress();
    }

    public void fold() {
        hand.fold();
    }

    public void placeBet(int bet) {
        hand.placeBet(bet);
    }

    public void raise(int raiseAmount) {
        hand.raise(raiseAmount);
    }

    public void call() {
        hand.call();
    }

    public void check() {
        hand.check();
    }

    public void addPlayer(Player player) throws GameAlreadyInProgressException, GameFullException {
        if(isGameOn()) {
            throw new GameAlreadyInProgressException();
        }
        if (players.size() == getSeats()) {
            throw new GameFullException();
        }
        players.add(new GamePlayer(player, getBuyIn()));
        if (players.size() == getSeats() && players.stream().anyMatch(p -> p.type == Player.PlayerType.HUMAN)) {
            gameOn = true;
        }
    }

    public HandInfo getHandInfo(String username) {
        return hand != null? new HandInfo(hand, username): null;
    }

    public int getHandIndex() {
        return handIndex;
    }

    private GamePlayer getPlayer(String username) {
        return players.stream().filter(p -> p.name.equals(username)).findFirst().orElse(null);
    }

    public void setPlayerReady(String username, boolean ready) {
        getPlayer(username).setReady(ready);
        if (players.stream().allMatch(GamePlayer::isReady)) {
            startHand();
            players.forEach(p -> p.setReady(false));
        }
    }

    public GameConfig getConfig() {
        return config;
    }

    public boolean isJoinable() {
        return handIndex == 0 && !gameOn;
    }

    public void addMessage(String author, String message) {
        chat.add(new ChatMessage(author, message));
    }

    public List<ChatMessage> getChat() {
        return chat;
    }
}