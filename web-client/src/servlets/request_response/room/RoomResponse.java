package servlets.request_response.room;

import immutables.Card;
import immutables.GameInfo;
import immutables.HandInfo;

import java.util.List;
import java.util.stream.Collectors;

public class RoomResponse {
    private final GameResponse game;
    private final HandResponse hand;
    private final String username;

    public RoomResponse(GameInfo game, HandInfo hand, String username) {
        this.game = new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getAuthor(),
                game.isJoinable(),
                game.isGameOn(),
                game.isHandInProgress(),
                game.getHandIndex(),
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
                        p.getState().toString(),
                        p.isReady()
                )).collect(Collectors.toList()),
                game.getChat().stream().map(c -> new ChatMessageResponse(
                        c.getAuthor(),
                        c.getMessage()
                )).collect(Collectors.toList())
        );

        this.hand = hand == null ? null :
                new HandResponse(
                        hand.getCommunityCards().stream().map(Card::toShortString).collect(Collectors.toList()),
                        hand.getPot(),
                        hand.getSmallBlind(),
                        hand.getBigBlind(),
                        hand.isBetActive(),
                        hand.getMaxBet(),
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
                                p.isFolded(),
                                p.getRanking(),
                                p.getChipsWon()
                        )).collect(Collectors.toList()),
                        hand.getWinners().stream().map(p -> new HandPlayerResponse(
                                p.getId(),
                                p.getName(),
                                p.getType().toString(),
                                p.getBet(),
                                p.getChips(),
                                p.getState().toString(),
                                p.getFirstCard().toShortString(),
                                p.getSecondCard().toShortString(),
                                p.isCurrent(),
                                p.isFolded(),
                                p.getRanking(),
                                p.getChipsWon()
                        )).collect(Collectors.toList())
                );

        this.username = username;
    }
}