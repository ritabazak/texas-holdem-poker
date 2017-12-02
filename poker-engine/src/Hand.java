import com.rundef.poker.EquityCalculator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Hand {
    public enum HandPhase{
        PRE_FLOP(0), FLOP(1), TURN(2), RIVER(3), FINISH(4);

        private int phase;

        HandPhase(int phase){
            this.phase = phase;
        }

        public boolean isAfter(HandPhase other){
            return phase >= other.phase;
        }

        public HandPhase next() {
            switch (this) {
                case PRE_FLOP:
                    return FLOP;
                case FLOP:
                    return TURN;
                case TURN:
                    return RIVER;
                case RIVER:
                case FINISH:
                default:
                    return FINISH;
            }
        }
    }

    private final List<HandPlayer> players;
    private final Deck deck = new Deck();
    private List<Card> communityCards;
    private int pot;
    private int turn;
    private int cycleStart;
    private int dealer;
    private final int smallBlind;
    private final int bigBlind;
    private HandPhase phase = HandPhase.PRE_FLOP;
    private List<HandPlayer> winners = new LinkedList<>();
    private int remainder = 0;

    public Hand(List<GamePlayer> players, int dealer, int smallBlind, int bigBlind, int pot){
        communityCards = Arrays.asList(
                deck.dealCard(),
                deck.dealCard(),
                deck.dealCard(),
                deck.dealCard(),
                deck.dealCard()
        );
        this.players = players.stream()
                .map(player -> new HandPlayer(player, deck.dealCard(), deck.dealCard()))
                .collect(toList());
        this.pot = pot;
        this.dealer = dealer;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;

        turn = getSmallIndex();
        placeBet(smallBlind);
        placeBet(bigBlind);
        cycleStart = turn;
    }

    private int getSmallIndex() {
        return (dealer + 1) % players.size();
    }
    private int getBigIndex() {
        return (dealer + 2) % players.size();
    }

    public List<Card> getCommunityCards() {
        return IntStream.range(0, communityCards.size())
                .mapToObj(i ->
                        (
                                phase.isAfter(HandPhase.RIVER) ||
                                (i < 4 && phase.isAfter(HandPhase.TURN)) ||
                                (i < 3 && phase.isAfter(HandPhase.FLOP)) ?
                                    communityCards.get(i) : Card.noneCard
                        )
                ).collect(toList());
    }

    public int getPot() {
        return pot;
    }

    public List<PlayerHandInfo> getHandStatus() {
        return IntStream.range(0, players.size())
                .mapToObj(i ->
                        new PlayerHandInfo(
                                players.get(i),
                                i == dealer,
                                i == getSmallIndex(),
                                i == getBigIndex(),
                                i == turn,
                                phase == HandPhase.FINISH || (players.get(i).getType() == Player.PlayerType.HUMAN && i == turn)
                        )
                ).collect(toList());
    }

    public List<PlayerHandInfo> getWinners() {
        return IntStream.range(0, players.size())
                .filter(i -> winners.contains(players.get(i)))
                .mapToObj(i ->
                        new PlayerHandInfo(
                                players.get(i),
                                i == dealer,
                                i == getSmallIndex(),
                                i == getBigIndex(),
                                i == turn,
                                true
                        )
                ).collect(toList());
    }

    public int getPotRemainder() {
        return remainder;
    }

    public boolean handInProgress() {
        return phase != HandPhase.FINISH;
    }

    public boolean isHumanTurn() {
        return players.get(turn).getType() == Player.PlayerType.HUMAN;
    }

    public int getCurrentBet() {
        return players.stream().mapToInt(HandPlayer::getBet).max().orElse(0);
    }

    public boolean isBetActive() {
        return getCurrentBet() != 0;
    }

    public int getMaxBet() {
        return players.stream()
                .mapToInt(player -> player.getChips() + player.getBet())
                .min().orElse(Integer.MAX_VALUE) - getCurrentBet();
    }

    public void playComputerTurn() {
        if (getCurrentBet() > 0 ) {
            call();
        }
        else {
            check();
        }
    }

    private void nextTurn() {
        if (players.stream().allMatch(player -> player.getType() == Player.PlayerType.COMPUTER || player.isFolded())) {
            phase = HandPhase.FINISH;
            splitPotToComputers();
        }
        else {
            do {
                turn = (turn + 1) % players.size();
            }
            while (players.get(turn).isFolded());

            if (turn == cycleStart) {
                pot += players.stream().mapToInt(HandPlayer::collectBet).sum();
                cycleStart = turn;

                if (players.stream().anyMatch(player -> !player.isFolded() && player.getChips() == 0)) {
                    phase = HandPhase.FINISH;
                }
                else {
                    phase = phase.next();
                }
            }

            if (phase == HandPhase.FINISH) {
                finish();
            }
        }
    }

    public void fold() {
        players.get(turn).fold();
        pot += players.get(turn).collectBet();
        nextTurn();
    }

    public void placeBet(int amount) {
        players.get(turn).placeBet(amount);
        cycleStart = turn;
        nextTurn();
    }

    public void raise(int raiseAmount) {
        placeBet(getCurrentBet() + raiseAmount);
    }

    public void call() {
        players.get(turn).placeBet(getCurrentBet());
        nextTurn();
    }

    public void check() {
        nextTurn();
    }

    private void splitPotToComputers() {
        pot += players.stream().mapToInt(HandPlayer::collectBet).sum();

        winners = players.stream()
                .filter(player -> player.getType() == Player.PlayerType.COMPUTER && !player.isFolded())
                .collect(toList());

        remainder = pot % winners.size();

        winners.forEach(player -> player.addChips(pot / winners.size()));
        pot = remainder;
    }

    private void finish() {
        EquityCalculator calculator = new EquityCalculator();
        try {
            calculator.setBoardFromString(communityCards.stream().map(Card::toShortString).collect(joining()));

            List<HandPlayer> activePlayers = players.stream().filter(player -> !player.isFolded()).collect(toList());

            activePlayers.stream()
                    .map(player -> player.getFirstCard().toShortString() + player.getSecondCard().toShortString())
                    .forEach(hand -> {
                        try { calculator.addHand(com.rundef.poker.Hand.fromString(hand)); }
                        catch (Exception ignored) {} // Will never happen since we always send 4 characters
                    });

            calculator.calculate();

            IntStream.range(0, activePlayers.size())
                    .forEach(i -> activePlayers.get(i).setResults(calculator.getHandEquity(i), calculator.getHandRanking(i)));

            int maxEquity = activePlayers.stream().mapToInt(HandPlayer::getEquity).max().orElse(0);

            winners = activePlayers.stream().filter(player -> player.getEquity() == maxEquity).collect(toList());

            remainder = pot % winners.size();
            winners.forEach(winner -> winner.addChips(pot / winners.size()));
            pot = remainder;
        }
        catch (Exception e) {} // Will never happen since we always send 10 characters (5 cards)
    }
}