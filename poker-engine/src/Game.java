import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public abstract class Game {
    List<Player> players = new ArrayList<>();
    private int dealer;
    protected int hand = 0;
    protected int handsCount;
    private int buyIn;
    private Deck deck = new Deck();
    private Temporal startTime;
    private int smallBlind;
    private int bigBlind;

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

    void dealHand() {
        deck = new Deck();

        for (Player player: players) {
            player.dealCards(deck.dealCard(), deck.dealCard());
        }
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
        return hand;
    }
    public int getMaxPot() {
        return players.stream()
                .mapToInt(Player::getBuyIns)
                .sum() * buyIn;
    }
    public List<PlayerInfo> getGameStatus() {
        return IntStream.range(0, players.size())
                .mapToObj(i ->
                        new PlayerInfo(
                                players.get(i),
                                i == dealer,
                                i == getSmallIndex(),
                                i == getBigIndex(),
                                0
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
        players.get(playerIndex).addChips(buyIn);
    }

    void shufflePlayers(){
        Collections.shuffle(players);
    }

}