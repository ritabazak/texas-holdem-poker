package servlets.request_response.room;

import immutables.Card;
import immutables.GameInfo;
import immutables.HandInfo;

import java.util.List;
import java.util.stream.Collectors;

public class RoomResponse {
    private final GameResponse game;
    private final HandResponse hand;

    public RoomResponse(GameInfo game, HandInfo hand) {
        this.game = new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getAuthor(),
                game.isGameOn(),
                game.getHandsCount(),
                game.getBuyIn(),
                game.getSeats(),
                game.getPlayerCount(),
                game.getInitialSmallBlind(),
                game.getInitialBigBlind(),
                game.getPlayers().stream().map(p -> new GamePlayerResponse(
                        p.getId(),
                        p.getName(),
                        p.getType().toString(),
                        p.getBuyIns(),
                        p.getHandsWon(),
                        p.getChips(),
                        p.getState().toString()
                )).collect(Collectors.toList())
        );

        this.hand = new HandResponse(
                hand.getCommunityCards().stream().map(Card::toShortString).collect(Collectors.toList()),
                hand.getPot(),
                hand.getSmallBlind(),
                hand.getBigBlind(),
                hand.getPlayers().stream().map(p -> new HandPlayerResponse(
                        p.getId(),
                        p.getName(),
                        p.getType().toString(),
                        p.getBet(),
                        p.getChips(),
                        p.getState().toString(),
                        p.getFirstCard().toShortString(),
                        p.getSecondCard().toShortString(),
                        p.isCurrent(),
                        p.isFolded()
                )).collect(Collectors.toList())
        );
    }
}
