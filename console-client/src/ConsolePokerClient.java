import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import exceptions.BadFileExtensionException;
import exceptions.InvalidBlindsException;
import exceptions.InvalidHandsCountException;
import immutables.PlayerHandInfo;
import immutables.PlayerInfo;
import immutables.Card;

public class ConsolePokerClient {
    private PokerEngine engine;
    private static final int BOX_WIDTH = 15;

    private ConsolePokerClient() {
        engine = new PokerEngine();
    }

    private void launch() {
        while (showMenu());
    }

    private boolean showMenu() {
        switch (MainMenu.show(engine.isXmlLoaded(), engine.isGameOn())) {
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
            case 8:
                exitGame();
                return false;
        }
        return true;
    }

    private void loadXmlFile() {
        System.out.println("Please enter a path to the XML file:");

        while (true) {
            try {
                engine.loadConfigFile(Menu.readString());
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

        System.out.println("internals.Game started!");
    }

    private void displayGameStatus() {
        List<List<String>> playerBoxes = engine.getGameStatus().stream()
                .map(player -> {
                    Map<String, String> infoFields = new LinkedHashMap<>();

                    infoFields.put("Type", player.getType().toShortString());
                    infoFields.put("State", player.getState().toShortString());
                    infoFields.put("Chips", Integer.toString(player.getChips()));
                    infoFields.put("BuyIns", Integer.toString(player.getBuyIns()));
                    infoFields.put("Wins", String.format("%d/%d", player.getHandsWon(), engine.getHandsCount()));

                    return createBox(infoFields, '.');
                })
                .collect(toList());

        System.out.println("");

        concatBoxes(playerBoxes.get(0), playerBoxes.get(1))
                .forEach(System.out::println);

        System.out.println("\n\n");

        concatBoxes(playerBoxes.get(3), playerBoxes.get(2))
                .forEach(System.out::println);

        System.out.println("");
    }

    private void displayHandStatus(){
        List<List<String>> playerBoxes = engine.getHandStatus().stream()
                .map(player -> {
                    Map<String, String> infoFields = new LinkedHashMap<>();

                    infoFields.put("Type", player.getType().toShortString());
                    infoFields.put("State", player.getState().toShortString());
                    infoFields.put("Chips", Integer.toString(player.getChips()));
                    if (player.isFolded()) {
                        infoFields.put("Cards", "FOLD");
                    }
                    else {
                        infoFields.put("Cards", player.getFirstCard().toShortString() + " " + player.getSecondCard().toShortString());
                    }
                    infoFields.put("Bet", Integer.toString(player.getBet()));

                    return createBox(infoFields, player.isCurrent() ? '#' : '.');
                })
                .collect(toList());

        System.out.println("");

        concatBoxes(playerBoxes.get(0), playerBoxes.get(1))
                .forEach(System.out::println);

        System.out.println("");

        System.out.print(engine.getCommunityCards()
                .stream()
                .map(Card::toShortString)
                .collect(joining(" | ")));

        System.out.println(String.format("%25s", "Pot: " + engine.getPot()));

        System.out.println("");

        if (playerBoxes.size() > 2) {
            if (playerBoxes.size() == 3) {
                playerBoxes.get(2)
                        .forEach(line ->
                                System.out.println(
                                        String.format("%" + ((BOX_WIDTH + 4 + 9) / 2) + "s", "") + line
                                )
                        );
            }
            else {
                concatBoxes(playerBoxes.get(3), playerBoxes.get(2))
                        .forEach(System.out::println);
            }
        }

        System.out.println("");
    }

    private List<String> createBox(Map<String, String> infoBox, char border) {
        List<String> rows = infoBox.keySet()
                .stream()
                .map(key -> String.format("%s %s: %-" + (BOX_WIDTH - key.length() - 2) + "s %s", border, key, infoBox.get(key), border))
                .collect(toList());

        String emptyRow = IntStream.range(0, BOX_WIDTH + 4)
                .mapToObj(i -> i % 2 != 0 ? " " : Character.toString(border))
                .collect(joining(""));

        rows.add(0, emptyRow);
        rows.add(emptyRow);

        return rows;
    }

    private List<String> concatBoxes(List<String> box1, List<String> box2) {
        return IntStream.range(0, box1.size())
                .mapToObj(i -> box1.get(i) + "         " + box2.get(i))
                .collect(toList());
    }

    private void playHand() {
        engine.startHand();

        while (engine.handInProgress()) {
            if (engine.isHumanTurn()) {
                displayHandStatus();

                switch (HandMenu.show(engine.isBetActive(), engine.getMaxBet() > 0)) {
                    case 'F':
                        engine.fold();
                        break;
                    case 'B':
                        engine.placeBet(HandMenu.readBet(engine.getMaxBet()));
                        break;
                    case 'C':
                        engine.call();
                        break;
                    case 'K':
                        engine.check();
                        break;
                    case 'R':
                        engine.raise(HandMenu.readRaise(engine.getMaxBet()));
                        break;
                }
            }
            else {
                engine.playComputerTurn();
            }
        }

        displayHandSummary();

        if (!engine.isGameOn()) {
            endGame();
        }
    }

    private void displayHandSummary() {
        displayHandStatus();

        List<PlayerHandInfo> winners = engine.getWinners();

        if (winners.get(0).getRanking().equals("")) {
            System.out.println("All human players folded! Computers win!");
        }
        else {
            System.out.println("Winning hands:");

            winners.forEach(winner -> {
                System.out.print(winner.getType() == PlayerInfo.PlayerType.HUMAN ? "(Human) " : "(Computer) ");
                System.out.print(winner.getFirstCard().toShortString() + " " + winner.getSecondCard().toShortString());
                System.out.println(" -> " + winner.getRanking());
            });
        }

        System.out.println("");
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
        displayGameStatistics();
        engine.endGame();
    }

    private void exitGame() {
        System.out.println("Bye bye!");
    }

    public static void main(String[] args) {
        (new ConsolePokerClient()).launch();
    }
}