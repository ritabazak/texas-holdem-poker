public class HandMenu extends Menu {
    public static char show(boolean betActive, boolean canRaise) {
        System.out.println("Please choose the letter for your move:");

        String options;

        if (betActive) {
            if (canRaise) {
                System.out.println("(F)old | (C)all | (R)aise");
                options = "FCR";
            }
            else {
                System.out.println("(F)old | (C)all");
                options = "FC";
            }
        }
        else {
            System.out.println("(F)old | (B)et | Chec(K)");
            options = "FBK";
        }

        while (true) {
            String input = readString();

            if (input.length() == 1 && options.contains(input)) {
                return input.charAt(0);
            }

            System.out.println("Please enter a valid letter for your move!");
        }
    }

    public static int readBet(int maxBet) {
        System.out.print(String.format("Please place a bet (%d-%d): ", 1, maxBet));
        return readIntFromRange(1, maxBet);
    }

    public static int readRaise(int maxRaise) {
        System.out.print(String.format("Enter the raise amount (%d-%d): ", 1, maxRaise));
        return readIntFromRange(1, maxRaise);
    }
}
