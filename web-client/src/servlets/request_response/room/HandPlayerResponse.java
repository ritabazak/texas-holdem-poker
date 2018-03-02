package servlets.request_response.room;

import servlets.request_response.lobby.PlayerResponse;

public class HandPlayerResponse extends PlayerResponse {
    private final int bet;
    private final int chips;
    private final String state;
    private final String firstCard;
    private final String secondCard;
    private final boolean current;
    private final boolean folded;
    private final String ranking;
    private final int chipsWon;

    public HandPlayerResponse(int id,
                              String name,
                              String type,
                              int bet,
                              int chips,
                              String state,
                              String firstCard,
                              String secondCard,
                              boolean current,
                              boolean folded,
                              String ranking,
                              int chipsWon) {
        super(id, name, type);
        this.bet = bet;
        this.chips = chips;
        this.state = state;
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        this.current = current;
        this.folded = folded;
        this.ranking = ranking;
        this.chipsWon = chipsWon;
    }
}
