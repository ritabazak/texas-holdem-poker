import immutables.Card;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardController {
    @FXML private ImageView cardImage;

    public void bindCard(ObjectBinding<Card> cardProp, BooleanBinding forceShowCard) {
        ObjectBinding<Card> safeCard = Bindings.when(
                cardProp.isNotNull()
                        .and(
                                forceShowCard
                                        .or(cardImage.hoverProperty())
                        )
        )
                .then(cardProp)
                .otherwise(Card.noneCard);

        cardImage.imageProperty().bind(Bindings.createObjectBinding(() -> getCardImage(safeCard.get()), safeCard));
    }

    private static Image getCardImage(Card card) {
        if (card == Card.noneCard) {
            return new Image(CardController.class.getResource("images/cards/blank.png").toExternalForm());
        }

        return new Image(CardController.class.getResource("images/cards/" + card.toShortString() + ".png").toExternalForm());
    }
}
