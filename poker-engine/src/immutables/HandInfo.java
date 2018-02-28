package immutables;

import internals.Hand;

import java.util.List;

public class HandInfo {
    private final List<Card> communityCards;
    private final int pot;
    private final int smallBlind;
    private final int bigBlind;
    private final List<PlayerHandInfo> players;

    public HandInfo(Hand hand) {
        communityCards = hand.getCommunityCards(false);
        pot = hand.getPot();
        smallBlind = hand.getSmallBlind();
        bigBlind = hand.getBigBlind();
        players = hand.getHandStatus(false);
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

    public List<PlayerHandInfo> getPlayers() {
        return players;
    }
}
