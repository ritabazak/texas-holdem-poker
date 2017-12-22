package internals;

import sun.security.krb5.Config;
import xml_game_config.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class GameConfig {
    public enum GameType {
        BASIC, MULTIPLAYER, DYNAMIC_MULTIPLAYER
    }

    public class ConfigPlayer {
        private final int id;
        private final String name;
        private final Player.PlayerType type;

        ConfigPlayer(int id, String name, Player.PlayerType type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Player.PlayerType getType() {
            return type;
        }
    }

    private GameType gameType;
    private boolean fixedBlinds;
    private int bigBlind;
    private int smallBlind;
    private int blindAddition;
    private int maxTotalRounds;
    private int buyIn;
    private int handsCount;
    private List<ConfigPlayer> configPlayers;

    public GameConfig(File f) {
        try {
            JAXBContext jaxb = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller un = jaxb.createUnmarshaller();

            GameDescriptor descriptor = (GameDescriptor)un.unmarshal(f);

            parseDescriptor(descriptor);
        }
        catch (JAXBException e) {
            System.out.println(e.toString());
        }
    }

    public GameType getGameType() {
        return gameType;
    }
    public boolean isFixedBlinds() {
        return fixedBlinds;
    }
    public int getBigBlind() {
        return bigBlind;
    }
    public int getSmallBlind() {
        return smallBlind;
    }
    public int getBlindAddition() {
        return blindAddition;
    }
    public int getMaxTotalRounds() {
        return maxTotalRounds;
    }
    public int getBuyIn() {
        return buyIn;
    }
    public int getHandsCount() {
        return handsCount;
    }
    public int getPlayerCount() {
        if (gameType == GameType.BASIC) {
            return 4;
        }
        return configPlayers.size();
    }
    public List<ConfigPlayer> getConfigPlayers() {
        return configPlayers;
    }

    private void parseDescriptor(GameDescriptor descriptor) {
        parseGameType(descriptor.getGameType());
        parsePlayers(descriptor.getPlayers());
        parseDynamicPlayers(descriptor.getDynamicPlayers());
        parseStructure(descriptor.getStructure());
    }

    private void parsePlayers(Players players) {
        configPlayers = players
                .getPlayer()
                .stream()
                .map(player -> new ConfigPlayer(
                        player.getId(),
                        player.getName(),
                        Player.PlayerType.fromString(player.getType())
                ))
                .collect(Collectors.toList());
    }

    private void parseDynamicPlayers(DynamicPlayers dynPlayers) {
        //TODO: Parse this
    }

    private void parseStructure(Structure structure) {
        parseBlinds(structure.getBlindes());
        handsCount = structure.getHandsCount();
        buyIn = structure.getBuy();
    }

    private void parseBlinds(Blindes blinds) {
        smallBlind = blinds.getSmall();
        bigBlind = blinds.getBig();
        fixedBlinds = blinds.isFixed();

        if (!fixedBlinds) {
            blindAddition = blinds.getAdditions();
            maxTotalRounds = blinds.getMaxTotalRounds();
        }
    }

    private void parseGameType(String gameTypeStr) {
        switch (gameTypeStr) {
            case "Multiplayer":
                gameType = GameType.MULTIPLAYER;
                break;
            case "DynamicMultiplayer":
                gameType = GameType.DYNAMIC_MULTIPLAYER;
                break;
            default:
            case "Basic":
                gameType = GameType.BASIC;
        }
    }
}
