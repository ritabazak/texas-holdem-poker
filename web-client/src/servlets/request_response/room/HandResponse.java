package servlets.request_response.room;

import java.util.List;

public class HandResponse {
    private final List<String> communityCards;
    private final int pot;
    private final int smallBlind;
    private final int bigBlind;
    private final List<HandPlayerResponse> players;

    public HandResponse(List<String> communityCards,
                        int pot,
                        int smallBlind,
                        int bigBlind,
                        List<HandPlayerResponse> players) {
        this.communityCards = communityCards;
        this.pot = pot;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.players = players;
    }
}
