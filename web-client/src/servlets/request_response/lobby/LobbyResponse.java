package servlets.request_response.lobby;

import immutables.GameInfo;
import immutables.PlayerInfo;

import java.util.List;
import java.util.stream.Collectors;

public class LobbyResponse {
    private final List<PlayerResponse> players;
    private final List<GameResponse> games;
    private final String username;

    public LobbyResponse(List<PlayerInfo> players, List<GameInfo> games, String username) {
        this.username = username;
        this.games = games.stream()
                .map(g -> new GameResponse(
                        g.getId(),
                        g.getTitle(),
                        g.getAuthor(),
                        g.isGameOn(),
                        g.getHandsCount(),
                        g.getBuyIn(),
                        g.getSeats(),
                        g.getPlayerCount(),
                        g.getInitialSmallBlind(),
                        g.getInitialBigBlind(),
                        g.isFixedBlinds(),
                        g.getBlindAddition(),
                        g.getMaxTotalRoundsBlinds()
                ))
                .collect(Collectors.toList());
        this.players = players.stream()
                .map(p -> new PlayerResponse(p.getId(), p.getName(), p.getType().toString()))
                .collect(Collectors.toList());
    }
}
