package internals;

import com.rundef.poker.EquityCalculator;
import immutables.Card;
import immutables.PlayerHandInfo;

import java.util.*;
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
    private HandPhase phase = HandPhase.PRE_FLOP;
    private List<HandPlayer> winners = new LinkedList<>();
    private int remainder = 0;
    private boolean roundInProgress = true;

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
    private HandPlayer getCurrentPlayer() {
        return players.get(turn);
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
    public List<PlayerHandInfo> getHandStatus() {
        return IntStream.range(0, players.size())
                .mapToObj(i ->
                        new PlayerHandInfo(
                                players.get(i),
                                i == dealer,
                                i == getSmallIndex(),
                                i == getBigIndex(),
                                roundInProgress && i == turn,
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
    public int getPot() {
        return pot;
    }
    public int getPotRemainder() {
        return remainder;
    }
    public int getCurrentBet() {
        return players.stream().mapToInt(HandPlayer::getBet).max().orElse(0);
    }
    public int getMaxBet() {
        return Math.min(
                players
                        .stream()
                        .mapToInt(player -> player.getChips() + player.getBet())
                        .min().orElse(Integer.MAX_VALUE) - getCurrentBet(),

                players
                        .stream()
                        .mapToInt(HandPlayer::getBet)
                        .sum() + pot
        ) - getCurrentBet();
    }
    public boolean isHumanTurn() {
        return getCurrentPlayer().getType() == Player.PlayerType.HUMAN;
    }
    public boolean isBetActive() {
        return getCurrentBet() != 0;
    }
    public boolean handInProgress() {
        return phase != HandPhase.FINISH;
    }
    public boolean isRoundInProgress() {
        return roundInProgress;
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

            winners = players
                    .stream()
                    .filter(player -> player.getType() == Player.PlayerType.COMPUTER)
                    .filter(player -> !player.isFolded())
                    .collect(toList());

            splitPot();
            return;
        }

        do {
            turn = (turn + 1) % players.size();
        }
        while (getCurrentPlayer().isFolded());

        if (turn == cycleStart) {
            pot += players.stream().mapToInt(HandPlayer::collectBet).sum();

            cycleStart = turn;

            roundInProgress = false;
        }
    }

    public void nextRound() {
        if (players.stream().anyMatch(player -> !player.isFolded() && player.getChips() == 0)) {
            phase = HandPhase.FINISH;
        }
        else {
            phase = phase.next();
        }

        if (phase == HandPhase.FINISH) {
            finish();
        }
        else {
            roundInProgress = true;
        }
    }

    public void fold() {
        getCurrentPlayer().fold();
        pot += players.get(turn).collectBet();
        nextTurn();
    }

    public void placeBet(int amount) {
        if (getCurrentPlayer().getChips() < amount) {
            fold();
            return;
        }

        getCurrentPlayer().placeBet(amount);
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

    private void finish() {
        try {
            EquityCalculator calculator = new EquityCalculator();

            calculator.setBoardFromString(
                    communityCards
                            .stream()
                            .map(Card::toShortString)
                            .collect(joining())
            );

            List<HandPlayer> activePlayers = players
                    .stream()
                    .filter(player -> !player.isFolded())
                    .collect(toList());

            activePlayers
                    .stream()
                    .map(player -> player.getFirstCard().toShortString() + player.getSecondCard().toShortString())
                    .forEach(handStr -> {
                        try { calculator.addHand(com.rundef.poker.Hand.fromString(handStr)); }
                        catch (Exception ignored) {} // Will never happen since we always send 4 characters
                    });

            calculator.calculate();

            final Set<Integer> winnerIndices = new HashSet<>(calculator.getWinningHands());

            IntStream.range(0, activePlayers.size())
                    .forEach(i -> activePlayers.get(i).setResults(
                            winnerIndices.contains(i),
                            calculator.getHandRanking(i)
                    ));

            winners = activePlayers
                    .stream()
                    .filter(HandPlayer::isWinner)
                    .collect(toList());

            splitPot();
        }
        catch (Exception ignored) {} // Will never happen since we always send 10 characters (5 cards)
    }

    private void splitPot() {
        remainder = pot % winners.size();
        winners.forEach(winner -> winner.win(pot / winners.size()));
        pot = remainder;
    }
}