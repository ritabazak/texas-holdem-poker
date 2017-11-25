import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.List;

public class PokerEngine {
    private boolean gameOn = false;
    private GameConfig gameConfig;
    private Game game;

    public void loadConfigFile(String xmlFilePath)
            throws FileNotFoundException, BadFileExtensionException, InvalidHandsCountException, InvalidBlindsException {
        File f = new File(xmlFilePath);

        if (!Files.exists(f.toPath())) {
            throw new FileNotFoundException();
        }

        if (!f.getPath().toLowerCase().endsWith(".xml")) {
            throw new BadFileExtensionException();
        }

        GameConfig temp = new GameConfig(f);

        if (temp.getHandsCount() % temp.getPlayerCount() != 0) {
            throw new InvalidHandsCountException();
        }

        if (temp.getSmallBlind() > temp.getBigBlind()) {
            throw new InvalidBlindsException();
        }

        gameConfig = temp;

        switch (gameConfig.getGameType()) {
            case BASIC:
                game = new BasicGame(gameConfig);
        }
    }

    public boolean isXmlLoaded() {
        return gameConfig != null;
    }

    public boolean isGameOn() {
        return game != null;
    }

    public void startGame() {
        gameOn = true;
        game.startTimer();
    }

    List<PlayerInfo> getGameStatus() {
        return game.getGameStatus();
    }
}