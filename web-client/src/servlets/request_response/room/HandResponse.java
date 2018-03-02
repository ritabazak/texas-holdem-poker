package servlets.request_response.room;

import java.util.List;

public class HandResponse {
    private final List<String> communityCards;
    private final int pot;
    private final int smallBlind;
    private final int bigBlind;
    private final boolean betActive;
    private final int maxBet;
    private final List<HandPlayerResponse> players;
    private final List<HandPlayerResponse> winners;

    public HandResponse(List<String> communityCards,
                        int pot,
                        int smallBlind,
                        int bigBlind,
                        boolean betActive,
                        int maxBet,
                        List<HandPlayerResponse> players,
                        List<HandPlayerResponse> winners) {
        this.communityCards = communityCards;
        this.pot = pot;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.betActive = betActive;
        this.maxBet = maxBet;
        this.players = players;
        this.winners = winners;
    }
}
