import xml_game_config.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class GameConfig {
    public enum GameType {
        BASIC, MULTIPLAYER, DYNAMIC_MULTIPLAYER
    }

    private GameType gameType;
    private int bigBlind;
    private int smallBlind;
    private int buyIn;
    private int handsCount;

    public GameConfig(String xmlFilePath) {
        try {
            JAXBContext jaxb = JAXBContext.newInstance(GameDescriptor.class);
            Unmarshaller un = jaxb.createUnmarshaller();

            File f = new File(xmlFilePath);
            GameDescriptor descriptor = (GameDescriptor)un.unmarshal(f);

            parseDescriptor(descriptor);
        }
        catch (JAXBException e) {
            System.out.println(e.toString());
        }
    }

    private void parseDescriptor(GameDescriptor descriptor) {
        parseGameType(descriptor.getGameType());
        parsePlayers(descriptor.getPlayers());
        parseDynamicPlayers(descriptor.getDynamicPlayers());
        parseStructure(descriptor.getStructure());
    }

    private void parsePlayers(Players players) {
        //TODO: Parse this
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
        //TODO: Parse blinds.getAdditions()
        //TODO: Parse blinds.getMaxTotalRounds()
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
