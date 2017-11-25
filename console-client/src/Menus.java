import java.util.*;

public class Menus {
    private boolean xmlLoaded;
    private boolean gameOn;

    public Menus(boolean xmlLoaded, boolean gameOn) {
        this.xmlLoaded = xmlLoaded;
        this.gameOn = gameOn;
    }

    public int show() {
        if (!gameOn) {
            return mainMenu();
        }
        return gameMenu();
    }

    private int mainMenu() {
        Set<Integer> legalOptions = new HashSet<>();

        System.out.println("1. Load XML file");
        legalOptions.add(1);

        if (xmlLoaded) {
            System.out.println("2. Start game");
            legalOptions.add(2);
        }

        return readIntInput(legalOptions);
    }

    private int gameMenu() {
        Set<Integer> legalOptions = new HashSet<>(Arrays.asList(3, 4, 5, 6, 7));

        System.out.println("3. Display game status");
        System.out.println("4. Play hand");
        System.out.println("5. Display game statistics");
        System.out.println("6. Buy-in");
        System.out.println("7. End game");

        return readIntInput(legalOptions);
    }

    private int readIntInput(Set<Integer> options) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                int option = sc.nextInt();

                if (options.contains(option)) {
                    return option;
                }
            }
            catch (InputMismatchException ignored) {}

            System.out.println("Please enter a valid number from the menu!");
        }
    }

    public static String readString() {
        Scanner sc = new Scanner(System.in);

        return sc.nextLine();
    }
}
