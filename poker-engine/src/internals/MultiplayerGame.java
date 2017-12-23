package internals;

import java.util.stream.Collectors;

public class MultiplayerGame extends Game {
    public MultiplayerGame(GameConfig config) {
        super(config);

        players.addAll(
                config
                        .getConfigPlayers()
                        .stream()
                        .map(player -> new GamePlayer(
                                player.getId(),
                                player.getName(),
                                player.getType(),
                                config.getBuyIn()
                        ))
                        .collect(Collectors.toList())
        );

        shufflePlayers();
    }
}