package internals;

import exceptions.GameAlreadyInProgressException;
import exceptions.GameFullException;
import immutables.Card;
import immutables.HandReplayData;
import immutables.PlayerGameInfo;
import immutables.PlayerHandInfo;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
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
    private int dealer;
    private int handIndex = 0;
    private Temporal startTime;
    private int smallBlind;
    private int bigBlind;
    private Hand hand;

    public Game(int id, GameConfig config, String author) {
        this.id = id;
        this.config = config;
        this.author = author;
        Random rand = new Random();
        dealer = rand.nextInt(config.getPlayerCount());

        title = config.getTitle();
        seats = config.getPlayerCount();
        smallBlind = config.getSmallBlind();
        bigBlind = config.getBigBlind();
    }

    public int getId() {
        return id;
    }

    public void startTimer() {
        startTime = LocalTime.now();
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

    public Duration getElapsedTime() {
        if (startTime == null) {
            return Duration.of(0, ChronoUnit.SECONDS);
        }

        return Duration.between(startTime, LocalTime.now());
    }
    protected int getSmallIndex() {
        return hand == null? (dealer + 1) % players.size(): hand.getSmallIndex();
    }
    protected int getBigIndex() {
        return hand == null? (dealer + 2) % players.size(): hand.getBigIndex();
    }
    public int getHandsPlayed() {
        return handIndex;
    }
    public int getMaxPot() {
        return players.stream()
                .mapToInt(GamePlayer::getBuyIns)
                .sum() * config.getBuyIn();
    }
    public List<PlayerHandInfo> getHandStatus(){
        return hand.getHandStatus(false);
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
    public List<Integer> getHumanIndices() {
        return IntStream.range(0, players.size())
                .filter(i -> players.get(i).getType() == Player.PlayerType.HUMAN)
                .boxed()
                .collect(toList());
    }

    public void addBuyIn(int playerIndex) {
        players.get(playerIndex).addBuyIn(config.getBuyIn());
    }
    public boolean retirePlayer(int playerIndex) {
        players.remove(playerIndex);

        return players.size() > 1 &&
                players.stream().anyMatch(player -> player.getType() == Player.PlayerType.HUMAN);
    }

    void shufflePlayers(){
        Collections.shuffle(players);
    }

    public void startGame() {
        gameOn = true;
        startTimer();
    }

    public int startHand() {
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

        return ++handIndex;
    }

    public List<HandReplayData> getReplay() {
        return hand.getReplayData();
    }
    public List<Card> getCommunityCards() {
        return hand.getCommunityCards(false);
    }
    public int getPot() {
        return hand.getPot();
    }
    public boolean isHandInProgress() {
        return hand != null && hand.handInProgress();
    }
    public boolean isHumanTurn() {
        return hand.isHumanTurn();
    }
    public int getMaxBet() {
        return hand.getMaxBet();
    }
    public List<PlayerHandInfo> getWinners() {
        return hand.getWinners();
    }
    public boolean isBetActive() {
        return hand.isBetActive();
    }
    public boolean isRoundInProgress() {
        return hand.isRoundInProgress();
    }
    public int getSmallBlind() {
        return hand != null? hand.getSmallBlind(): smallBlind;
    }
    public int getBigBlind() {
        return hand != null? hand.getBigBlind(): bigBlind;
    }
    public int getPlayerIndexById(int id) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId() == id) {
                return i;
            }
        }

        return -1;
    }
    public boolean canStartHand() {
        return players.stream().filter(player -> player.getChips() >= bigBlind).count() >= 2;
    }

    public void nextRound() {
        hand.nextRound();
    }
    public void playComputerTurn() {
        hand.playComputerTurn();
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
    }
}