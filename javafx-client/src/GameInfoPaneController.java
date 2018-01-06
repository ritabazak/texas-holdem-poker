import immutables.PlayerGameInfo;
import immutables.PlayerHandInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class GameInfoPaneController {
    @FXML private Label handsLabel;
    @FXML private Label buyInLabel;
    @FXML private Label smallBlindLabel;
    @FXML private Label bigBlindLabel;
    @FXML private TableView<PlayerGameInfo> playerTableView;
    @FXML private TableColumn<PlayerGameInfo, String> typeColumn;
    @FXML private TableColumn<PlayerGameInfo, String> nameColumn;
    @FXML private TableColumn<PlayerGameInfo, Integer> chipsColumn;
    @FXML private TableColumn<PlayerGameInfo, Integer> buyInsColumn;
    @FXML private TableColumn<PlayerGameInfo, Integer> winsColumn;

    public void bindGameStatusProperties(ObservableList<PlayerGameInfo> playerGameInfo,
                                         IntegerProperty handsPlayed,
                                         IntegerProperty totalHands,
                                         IntegerProperty smallBlind,
                                         IntegerProperty bigBlind,
                                         IntegerProperty initialSmallBlind,
                                         IntegerProperty initialBigBlind,
                                         IntegerProperty buyIn) {
        handsLabel.textProperty().bind(
                Bindings.format("%d / %d", handsPlayed, totalHands)
        );

        buyInLabel.textProperty().bind(buyIn.asString());
        smallBlindLabel.textProperty().bind(
                Bindings.format(
                        "%d (%d + %d)",
                        smallBlind,
                        initialSmallBlind,
                        Bindings.subtract(smallBlind, initialSmallBlind)
                )
        );
        bigBlindLabel.textProperty().bind(
                Bindings.format(
                        "%d (%d + %d)",
                        bigBlind,
                        initialBigBlind,
                        Bindings.subtract(bigBlind, initialBigBlind)
                )
        );

        playerTableView.setItems(playerGameInfo);
        typeColumn.setCellValueFactory(player -> new SimpleStringProperty(player.getValue().getType().toShortString()));
        nameColumn.setCellValueFactory(player -> new SimpleStringProperty(player.getValue().getName()));
        chipsColumn.setCellValueFactory(player -> new SimpleIntegerProperty(player.getValue().getChips()).asObject());
        buyInsColumn.setCellValueFactory(player -> new SimpleIntegerProperty(player.getValue().getBuyIns()).asObject());
        winsColumn.setCellValueFactory(player -> new SimpleIntegerProperty(player.getValue().getHandsWon()).asObject());

        playerTableView.getSortOrder().add(chipsColumn);
    }

    public void resortTable() {
        List<TableColumn<PlayerGameInfo, ?>> sortOrder = new ArrayList<>(playerTableView.getSortOrder());
        playerTableView.getSortOrder().clear();
        playerTableView.getSortOrder().addAll(sortOrder);
    }
}
