public class ConsolePokerClient {
    private PokerEngine engine = new PokerEngine();
    private boolean xmlLoaded = false;
    private boolean gameOn = false;
    private MainMenu mainMenu = new MainMenu(xmlLoaded, gameOn);

    private ConsolePokerClient() {

    }

    private void launch() {

    }

    public static void main(String[] args) {
        (new ConsolePokerClient()).launch();
    }
}
