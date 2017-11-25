import java.util.*;

public class Game {
    List<Player> players = new ArrayList<>();
    private int dealer;
    protected int hand = 0;
    protected int pot = 0;
    private int buyIn;
    private Deck deck = new Deck();
    private Date startTime;

    Game(int playerCount, int buyIn) {
        Random rand = new Random();
        dealer = rand.nextInt(playerCount);

        this.buyIn = buyIn;
    }

    public void startTimer() {
        startTime = new Date();
    }

    public long getElapsedTime() {
        return (new Date()).getTime() - startTime.getTime();
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
}
