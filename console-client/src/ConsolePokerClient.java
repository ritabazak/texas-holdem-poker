import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ConsolePokerClient {
    private PokerEngine engine;

    private ConsolePokerClient() {
        engine = new PokerEngine();
    }

    private void launch() {
        //TODO: Remove this
        try {
            engine.loadConfigFile("./ex1-basic.xml");
        }
        catch (FileNotFoundException | BadFileExtensionException | InvalidHandsCountException | InvalidBlindsException e) {
            e.printStackTrace();
        }
        engine.startGame();

        while (true) {
            showMenu();
        }
    }

    private void showMenu() {
        Menus menu = new Menus(engine.isXmlLoaded(), engine.isGameOn());

        int option = menu.show();

        switch (option) {
            case 1:
                loadXmlFile();
                break;
            case 2:
                startGame();
                break;
            case 3:
                displayGameStatus();
                break;
            case 4:
                playHand();
                break;
            case 5:
                displayGameStatistics();
                break;
            case 6:
                buyIn();
                break;
            case 7:
                endGame();
                break;
        }
    }

    private void loadXmlFile() {
        System.out.println("Please enter a path to the XML file:");

        while (true) {
            try {
                engine.loadConfigFile(Menus.readString());
                break;
            }
            catch (FileNotFoundException e) {
                System.out.println("Please enter a file that exists!");
            }
            catch (BadFileExtensionException e) {
                System.out.println("Please enter a path to an XML file!");
            }
            catch (InvalidBlindsException e) {
                System.out.println("Big blind must be larger than the small blind!\nPlease enter a valid XML file!");
            }
            catch (InvalidHandsCountException e) {
                System.out.println("There must be enough hands for all players to play an equal amount of hands!\nPlease enter a valid XML file!");
            }
        }

        System.out.println("XML Loaded!");
        displayGameStatus();
    }

    private void startGame() {
        engine.startGame();

        System.out.println("Game started!");
    }

    private void displayGameStatus() {
        List<PlayerInfo> info = engine.getGameStatus();

        List<List<String>> playerLines = info.stream()
                .map(player -> {
                    String type = player.getType() == PlayerInfo.PlayerType.HUMAN ? "H" : "C";
                    String state =
                            (player.getState() == PlayerInfo.PlayerState.DEALER) ? "D" :
                                    (player.getState() == PlayerInfo.PlayerState.BIG) ? "B (" + engine.getBigBlind() + ")" :
                                            (player.getState() == PlayerInfo.PlayerState.SMALL) ? "S (" + engine.getSmallBlind() + ")" :
                                                    "";
                    String wins = String.format("%d/%d", player.getHandsWon(), engine.getHandsCount());

                    return Arrays.asList(
                            "• • • • • • • • • • •",
                            String.format("• Type: %1$-12s•", type),
                            String.format("• State: %1$-11s•", state),
                            String.format("• Chips: %1$-11s•", player.getChips()),
                            String.format("• BuyIns: %1$-10s•", player.getBuyIns()),
                            String.format("• Wins: %1$-12s•", wins),
                            "• • • • • • • • • • •"
                    );
                })
                .collect(toList());

        List<String> firstLines = IntStream.range(0, 7)
                .mapToObj(i -> playerLines.get(0).get(i) + "      " + playerLines.get(1).get(i))
                .collect(toList());

        List<String> secondLines = IntStream.range(0, 7)
                .mapToObj(i -> playerLines.get(2).get(i) + "      " + playerLines.get(3).get(i))
                .collect(toList());

        System.out.println("");

        for (String line: firstLines) {
            System.out.println(line);
        }

        System.out.println("\n\n");

        for (String line: secondLines) {
            System.out.println(line);
        }

        System.out.println("");
    }

    private void playHand() {

        /*

        * * * * * * * * * *      • • • • • • • • • •
        * Type: H         *      • Type: C         •
        * State: D        *      • State: S        •
        * Chips: 100      *      • Chips: 100      •
        * Cards: 7C JH    *      • Cards: ??       •
        * Bet:            *      • Bet:            •
        * * * * * * * * * *      • • • • • • • • • •



        • • • • • • • • • •      • • • • • • • • • •
        • Type: C         •      • Type: C         •
        • State:          •      • State: B        •
        • Chips: 100      •      • Chips: 100      •
        • Cards: ??       •      • Cards: ??       •
        • Bet:            •      • Bet:            •
        • • • • • • • • • •      • • • • • • • • • •

         */
    }

    private void displayGameStatistics() {
        Duration elapsed = engine.getElapsedTime();

        System.out.println(String.format("Time elapsed: %02d:%02d", elapsed.getSeconds() / 60, elapsed.getSeconds() % 60));
        System.out.println(String.format("Hands played: %d/%d", engine.getHandsPlayed(), engine.getHandsCount()));
        System.out.println(String.format("Max pot: %d", engine.getMaxPot()));

        displayGameStatus();
    }

    private void buyIn() {
        engine.addBuyIn(engine.getHumanIndices().get(0));

        System.out.println("Chips were added");
    }

    private void endGame() {

    }

    public static void main(String[] args) {
        (new ConsolePokerClient()).launch();
    }
}