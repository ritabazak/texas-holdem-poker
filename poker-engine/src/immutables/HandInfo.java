package immutables;

import internals.Hand;

import java.util.List;

public class HandInfo {
    private final List<Card> communityCards;
    private final int pot;
    private final int smallBlind;
    private final int bigBlind;
    private final boolean betActive;
    private final int maxBet;
    private final List<PlayerHandInfo> players;
    private final List<PlayerHandInfo> winners;

    public HandInfo(Hand hand, String username) {
        communityCards = hand.getCommunityCards(false);
        pot = hand.getPot();
        smallBlind = hand.getSmallBlind();
        bigBlind = hand.getBigBlind();
        betActive = hand.isBetActive();
        maxBet = hand.getMaxBet();
        players = hand.getHandStatus(false, username);
        winners = hand.getWinners();
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getPot() {
        return pot;
    }

    public int getSmallBlind() {
        return smallBlind;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public boolean isBetActive() {
        return betActive;
    }

    public int getMaxBet() {
        return maxBet;
    }

    public List<PlayerHandInfo> getPlayers() {
        return players;
    }

    public List<PlayerHandInfo> getWinners() {
        return winners;
    }
}
