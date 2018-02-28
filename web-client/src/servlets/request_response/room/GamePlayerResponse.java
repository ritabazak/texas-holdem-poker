package servlets.request_response.room;

import servlets.request_response.lobby.PlayerResponse;

public class GamePlayerResponse extends PlayerResponse {
    private final int buyIns;
    private final int handsWon;
    private final int chips;
    private final String state;

    public GamePlayerResponse(int id,
                              String name,
                              String type,
                              int buyIns,
                              int handsWon,
                              int chips,
                              String state) {
        super(id, name, type);
        this.buyIns = buyIns;
        this.handsWon = handsWon;
        this.chips = chips;
        this.state = state;
    }
}
