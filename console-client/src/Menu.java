import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Menu {
    static int readIntFromRange(int startInclusive, int endInclusive) {
        while (true) {
            int input = readInt();

            if (input >= startInclusive && input <= endInclusive) {
                return input;
            }

            System.out.println(String.format("Please enter a number between %d and %d", startInclusive, endInclusive));
        }
    }

    static int readIntOption(Set<Integer> options) {
        while (true) {
            int option = readInt();

            if (options.contains(option)) {
                return option;
            }

            System.out.println("Please enter a valid number from the menu!");
        }
    }

    static int readInt() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                return sc.nextInt();
            }
            catch (InputMismatchException ignored) {
                sc.next();
                System.out.println("Please enter a number!");
            }
        }
    }

    static String readString() {
        Scanner sc = new Scanner(System.in);

        return sc.nextLine();
    }

    static void readEnter() {
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}
