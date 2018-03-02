package immutables;

public class Card {
    public enum Rank {
        DEUCE('2'), THREE('3'), FOUR('4'), FIVE('5'), SIX('6'), SEVEN('7'), EIGHT('8'),
        NINE('9'), TEN('T'), JACK('J'), QUEEN('Q'), KING('K'), ACE('A'), NONE('?');

        private final char c;
        public char toChar() {
            return c;
        }

        Rank(char c) {
            this.c = c;
        }
    }

    public enum Suit {
        CLUBS('C'), DIAMONDS('D'), HEARTS('H'), SPADES('S'), NONE('?');

        private final char c;
        public char toChar() {
            return c;
        }

        Suit(char c) {
            this.c = c;
        }
    }

    public static final Card noneCard = new Card(Rank.NONE, Suit.NONE);

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String toShortString() {
        return "" + rank.toChar() + suit.toChar();
    }
}
