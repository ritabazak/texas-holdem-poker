package immutables;

import java.util.List;

public class HandReplayData {
    private final List<PlayerHandInfo> players;
    private final List<Card> communityCards;
    private final int pot;

    public HandReplayData(List<PlayerHandInfo> players,
                          List<Card> communityCards,
                          int pot) {
        this.players = players;
        this.communityCards = communityCards;
        this.pot = pot;
    }

    public List<PlayerHandInfo> getPlayers() {
        return players;
    }
    public List<Card> getCommunityCards() {
        return communityCards;
    }
    public int getPot() {
        return pot;
    }
}
