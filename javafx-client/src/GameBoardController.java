import immutables.Card;
import immutables.PlayerHandInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class GameBoardController {
    @FXML private PlayerHandController playerTopLeftController;
    @FXML private PlayerHandController playerTopRightController;
    @FXML private PlayerHandController playerLeftController;
    @FXML private PlayerHandController playerRightController;
    @FXML private PlayerHandController playerBottomLeftController;
    @FXML private PlayerHandController playerBottomRightController;
    
    @FXML private CardController tableCenterCard1Controller;
    @FXML private CardController tableCenterCard2Controller;
    @FXML private CardController tableCenterCard3Controller;
    @FXML private CardController tableCenterCard4Controller;
    @FXML private CardController tableCenterCard5Controller;

    @FXML private Pane playerTopLeft;
    @FXML private Pane playerTopRight;
    @FXML private Pane playerLeft;
    @FXML private Pane playerRight;
    @FXML private Pane playerBottomLeft;
    @FXML private Pane playerBottomRight;

    @FXML private GridPane tableCenter;
    @FXML private Label tableCenterPotLabel;

    private IntegerBinding playerCount;

    private PokerEngine engine;

    public void setEngine(PokerEngine engine) {
        this.engine = engine;
    }

    public void bindHandStatusProperties(ObservableList<Card> communityCards, ObservableList<PlayerHandInfo> playerHandInfo, IntegerProperty pot) {
        tableCenterCard1Controller.bindCard(Bindings.valueAt(communityCards, 0));
        tableCenterCard2Controller.bindCard(Bindings.valueAt(communityCards, 1));
        tableCenterCard3Controller.bindCard(Bindings.valueAt(communityCards, 2));
        tableCenterCard4Controller.bindCard(Bindings.valueAt(communityCards, 3));
        tableCenterCard5Controller.bindCard(Bindings.valueAt(communityCards, 4));
        tableCenterPotLabel.textProperty().bind(pot.asString());

        playerCount = Bindings.size(playerHandInfo);

        playerLeft.visibleProperty().bind(playerCount.greaterThanOrEqualTo(3));
        playerRight.visibleProperty().bind(playerCount.greaterThanOrEqualTo(3));
        playerTopLeft.visibleProperty().bind(playerCount.greaterThanOrEqualTo(3));
        tableCenter.visibleProperty().bind(playerCount.greaterThanOrEqualTo(3));

        playerTopRight.visibleProperty().bind(playerCount.greaterThanOrEqualTo(5));
        playerTopRight.managedProperty().bind(playerTopRight.visibleProperty());

        playerBottomLeft.visibleProperty().bind(playerCount.greaterThanOrEqualTo(4));
        playerBottomLeft.managedProperty().bind(playerBottomLeft.visibleProperty());

        playerBottomRight.visibleProperty().bind(playerCount.isEqualTo(6));
        playerBottomRight.managedProperty().bind(playerBottomRight.visibleProperty());

        handlePlayerCountChange(playerCount.get(), playerHandInfo);
        playerCount.addListener((sizeBinding, oldSize, newSize) -> handlePlayerCountChange(newSize.intValue(), playerHandInfo));
    }

    private void handlePlayerCountChange(int count, ObservableList<PlayerHandInfo> playerHandInfo) {
        ObjectBinding<PlayerHandInfo> player1 = Bindings.valueAt(playerHandInfo, 0);
        ObjectBinding<PlayerHandInfo> player2 = Bindings.valueAt(playerHandInfo, 1);
        ObjectBinding<PlayerHandInfo> player3 = Bindings.valueAt(playerHandInfo, 2);
        ObjectBinding<PlayerHandInfo> player4 = Bindings.valueAt(playerHandInfo, 3);
        ObjectBinding<PlayerHandInfo> player5 = Bindings.valueAt(playerHandInfo, 4);
        ObjectBinding<PlayerHandInfo> player6 = Bindings.valueAt(playerHandInfo, 5);

        playerLeftController.bindPlayerProperties(player1);
        playerTopLeftController.bindPlayerProperties(player2);

        if (count == 3) {
            playerRightController.bindPlayerProperties(player3);
            GridPane.setColumnSpan(playerTopLeft, 2);
        }
        else if (count == 4) {
            playerRightController.bindPlayerProperties(player3);
            playerBottomLeftController.bindPlayerProperties(player4);
            GridPane.setColumnSpan(playerTopLeft, 2);
            GridPane.setColumnSpan(playerBottomLeft, 2);
        }
        else if (count == 5) {
            playerTopRightController.bindPlayerProperties(player3);
            playerRightController.bindPlayerProperties(player4);
            playerBottomLeftController.bindPlayerProperties(player5);
            GridPane.setColumnSpan(playerTopLeft, 1);
            GridPane.setColumnSpan(playerBottomLeft, 2);
        }
        else if (count == 6) {
            playerTopRightController.bindPlayerProperties(player3);
            playerRightController.bindPlayerProperties(player4);
            playerBottomRightController.bindPlayerProperties(player5);
            playerBottomLeftController.bindPlayerProperties(player6);
            GridPane.setColumnSpan(playerTopLeft, 1);
            GridPane.setColumnSpan(playerBottomLeft, 1);
        }
    }
}
