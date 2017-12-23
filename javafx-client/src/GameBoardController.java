import immutables.Card;
import immutables.PlayerGameInfo;
import immutables.PlayerHandInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import xml_game_config.Player;

public class GameBoardController {

    @FXML private GridPane playerTopLeft;
    @FXML private GridPane playerTopRight;
    @FXML private GridPane playerLeft;
    @FXML private GridPane playerRight;
    @FXML private GridPane playerBottomLeft;
    @FXML private GridPane playerBottomRight;
    @FXML private GridPane tableCenter;

    @FXML private Label playerTopLeftNameLabel;
    @FXML private Label playerTopLeftTypeLabel;
    @FXML private ImageView playerTopLeftCardOneImage;
    @FXML private ImageView playerTopLeftCardTwoImage;
    @FXML private Label playerTopLeftBetLabel;

    @FXML private Label playerTopRightNameLabel;
    @FXML private Label playerTopRightTypeLabel;
    @FXML private ImageView playerTopRightCardOneImage;
    @FXML private ImageView playerTopRightCardTwoImage;
    @FXML private Label playerTopRightBetLabel;

    @FXML private Label playerLeftNameLabel;
    @FXML private Label playerLeftTypeLabel;
    @FXML private ImageView playerLeftCardOneImage;
    @FXML private ImageView playerLeftCardTwoImage;
    @FXML private Label playerLeftBetLabel;

    @FXML private Label playerRightNameLabel;
    @FXML private Label playerRightTypeLabel;
    @FXML private ImageView playerRightCardOneImage;
    @FXML private ImageView playerRightCardTwoImage;
    @FXML private Label playerRightBetLabel;

    @FXML private Label playerBottomLeftNameLabel;
    @FXML private Label playerBottomLeftTypeLabel;
    @FXML private ImageView playerBottomLeftCardOneImage;
    @FXML private ImageView playerBottomLeftCardTwoImage;
    @FXML private Label playerBottomLeftBetLabel;

    @FXML private Label playerBottomRightNameLabel;
    @FXML private Label playerBottomRightTypeLabel;
    @FXML private ImageView playerBottomRightCardOneImage;
    @FXML private ImageView playerBottomRightCardTwoImage;
    @FXML private Label playerBottomRightBetLabel;

    @FXML private ImageView tableCenterCardOneImage;
    @FXML private ImageView tableCenterCardTwoImage;
    @FXML private ImageView tableCenterCardThreeImage;
    @FXML private ImageView tableCenterCardFourImage;
    @FXML private ImageView tableCenterCardFiveImage;
    @FXML private Label tableCenterPotLabel;


    private PokerEngine engine;


    public void setEngine(PokerEngine engine) {
        this.engine = engine;
    }

    public void bindXmlLoaded(BooleanProperty xmlLoaded) {
        playerTopLeft.visibleProperty().bind(xmlLoaded);
        playerTopRight.visibleProperty().bind(xmlLoaded);
        playerLeft.visibleProperty().bind(xmlLoaded);
        playerRight.visibleProperty().bind(xmlLoaded);
        playerBottomLeft.visibleProperty().bind(xmlLoaded);
        playerBottomRight.visibleProperty().bind(xmlLoaded);
        tableCenter.visibleProperty().bind(xmlLoaded);

    }

    public void bindHandStatusProperties(ObservableList<Card> communityCards, ObservableList<PlayerHandInfo> playerHandInfo, IntegerProperty pot) {
        playerTopRight.visibleProperty().bind(Bindings.size(playerHandInfo).greaterThanOrEqualTo(5));
        playerBottomLeft.visibleProperty().bind(Bindings.size(playerHandInfo).greaterThanOrEqualTo(4));
        playerBottomRight.visibleProperty().bind(Bindings.size(playerHandInfo).isEqualTo(6));

        playerLeftNameLabel.setText(playerHandInfo.get(0).getName());
        ObjectBinding<PlayerHandInfo> info0 = Bindings.valueAt(playerHandInfo, 0);
        Bindings.

        playerLeftTypeLabel;
        playerLeftCardOneImage;
        playerLeftCardTwoImage;
        playerLeftBetLabel;

        playerTopLeftNameLabel;
        playerTopLeftTypeLabel;
        playerTopLeftCardOneImage;
        playerTopLeftCardTwoImage;
        playerTopLeftBetLabel;
        playerTopRightNameLabel;
        playerTopRightTypeLabel;
        playerTopRightCardOneImage;
        playerTopRightCardTwoImage;
        playerTopRightBetLabel;
        playerRightNameLabel;
        playerRightTypeLabel;
        playerRightCardOneImage;
        playerRightCardTwoImage;
        playerRightBetLabel;
        playerBottomLeftNameLabel;
        playerBottomLeftTypeLabel;
        playerBottomLeftCardOneImage;
        playerBottomLeftCardTwoImage;
        playerBottomLeftBetLabel;
        playerBottomRightNameLabel;
        playerBottomRightTypeLabel;
        playerBottomRightCardOneImage;
        playerBottomRightCardTwoImage;
        playerBottomRightBetLabel;
        tableCenterCardOneImage;
        tableCenterCardTwoImage;
        tableCenterCardThreeImage;
        tableCenterCardFourImage;
        tableCenterCardFiveImage;
        tableCenterPotLabel;

    }
}
