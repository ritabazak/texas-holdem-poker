import java.util.*;

public class MainMenu extends Menu {

    public static int show(boolean xmlLoaded, boolean gameOn) {
        if (!gameOn) {
            return mainMenu(xmlLoaded);
        }
        return gameMenu();
    }

    private static int mainMenu(boolean xmlLoaded) {
        Set<Integer> legalOptions = new HashSet<>();

        System.out.println("1. Load XML file");
        legalOptions.add(1);

        if (xmlLoaded) {
            System.out.println("2. Start game");
            legalOptions.add(2);
        }

        System.out.println("8. Exit");
        legalOptions.add(8);

        return readIntOption(legalOptions);
    }

    private static int gameMenu() {
        Set<Integer> legalOptions = new HashSet<>(Arrays.asList(3, 4, 5, 6, 7, 8));

        System.out.println("3. Display game status");
        System.out.println("4. Play hand");
        System.out.println("5. Display game statistics");
        System.out.println("6. Buy-in");
        System.out.println("7. End game");
        System.out.println("8. Exit");

        return readIntOption(legalOptions);
    }
}
