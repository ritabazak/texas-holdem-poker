import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {
    List<Player> players = new ArrayList<>();
    private int dealer;
    protected int hand = 0;
    protected int hands;
    protected int pot = 0;
    private int buyIn;
    private Deck deck = new Deck();
    private Temporal startTime;

    Game(GameConfig config) {
        Random rand = new Random();
        dealer = rand.nextInt(config.getPlayerCount());

        buyIn = config.getBuyIn();
        hands = config.getHandsCount();
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

    void dealHand() {
        deck = new Deck();

        for (Player player: players) {
            player.dealCards(deck.dealCard(), deck.dealCard());
        }
    }

    protected int getSmall() {
        return (dealer + 1) % players.size();
    }

    protected int getBig() {
        return (dealer + 2) % players.size();
    }

    void shufflePlayers(){
        Collections.shuffle(players);
    }

    public List<PlayerInfo> getGameStatus() {
        return IntStream.range(0, players.size())
                .mapToObj(i ->
                        new PlayerInfo(
                                players.get(i),
                                i == dealer,
                                i == getSmall(),
                                i == getBig(),
                                hands,
                                0
                        )
                ).collect(Collectors.toList());
    }
}
