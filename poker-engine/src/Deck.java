import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Deck {
    private List<Card> deck = new LinkedList<>();

    public Deck() {
        for (Card.Suit suit: Card.Suit.values()) {
            for (Card.Rank rank: Card.Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
    }

    public Card dealCard() {
        Random rand = new Random();
        return deck.remove(rand.nextInt(deck.size()));
    }

}
