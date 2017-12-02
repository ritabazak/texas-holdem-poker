import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public abstract class Game {
    List<GamePlayer> players = new ArrayList<>();
    private int dealer;
    protected int handIndex = 0;
    protected int handsCount;
    private int buyIn;
    private Temporal startTime;
    private int smallBlind;
    private int bigBlind;
    private Hand hand;

    Game(GameConfig config) {
        Random rand = new Random();
        dealer = rand.nextInt(config.getPlayerCount());

        buyIn = config.getBuyIn();
        handsCount = config.getHandsCount();

        smallBlind = config.getSmallBlind();
        bigBlind = config.getBigBlind();
    }

    public void startTimer() {
        startTime = LocalTime.now();
    }

    public Duration getElapsedTime() {
        if (startTime == null) {
            return Duration.of(0, ChronoUnit.SECONDS);
        }

        return Duration.between(startTime, LocalTime.now());
    }
    protected int getSmallIndex() {
        return (dealer + 1) % players.size();
    }
    protected int getBigIndex() {
        return (dealer + 2) % players.size();
    }
    public int getSmallBlind() {
        return smallBlind;
    }
    public int getBigBlind() {
        return bigBlind;
    }
    public int getHandsCount() {
        return handsCount;
    }
    public int getHandsPlayed() {
        return handIndex;
    }
    public int getMaxPot() {
        return players.stream()
                .mapToInt(GamePlayer::getBuyIns)
                .sum() * buyIn;
    }
    public List<PlayerHandInfo> getHandStatus(){
        return hand.getHandStatus();
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
        players.get(playerIndex).addBuyIn(buyIn);
    }

    void shufflePlayers(){
        Collections.shuffle(players);
    }

    public int startHand() {
        hand = new Hand(players, dealer, smallBlind, bigBlind, handIndex == 0 ? 0 : hand.getPotRemainder());
        dealer = getSmallIndex();
        return ++handIndex;
    }

    public List<Card> getCommunityCards() {
        return hand.getCommunityCards();
    }

    public int getPot() {
        return hand.getPot();
    }

    public boolean handInProgress(){
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
}