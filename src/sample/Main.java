package sample;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.List;
import java.util.ArrayList;

public class Main extends Application {
    private static HBox botArea = new HBox();
    private static HBox humanArea = new HBox();
    private static List<Integer> toBeDiscarded = new ArrayList<>();
    private static boolean firstTurn = true;
    private static BorderPane root;
    private static Button discardButton;
    private static Button newGameButton;


    @Override
    public void start(Stage primaryStage) {
        //create the root node
        root = new BorderPane();
        Scene scene = new Scene(root, 1200, 900);

        //applies the css file "poker.css" to the project
        scene.getStylesheets().add(this.getClass().getResource("poker.css").toExternalForm());

        Button startButton = new Button("Start Game");
        startButton.getStyleClass().add("gameButton");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame();
            }
        });
        root.setCenter(startButton);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Poker");
        primaryStage.show();
        //************END FRONT END************
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startGame() {
        botArea.setId("botArea");
        botArea.setMinHeight(250);
        botArea.setAlignment(Pos.TOP_CENTER);
        root.setTop(botArea);

        humanArea.setId("humanArea");
        humanArea.setMinHeight(250);
        humanArea.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(humanArea);

        HBox cardArea = new HBox();
        cardArea.setId("cardArea");
        cardArea.setAlignment(Pos.CENTER);
        root.setCenter(cardArea);

        Label botMoney = new Label("$0.00");
        botArea.getChildren().addAll();

        Label humanMoney = new Label("$0.00");
        humanArea.getChildren().addAll();

        discardButton = new Button("Discard");
        discardButton.setId("discardButton");
        root.setLeft(discardButton);
        root.setAlignment(discardButton, Pos.BOTTOM_CENTER);

        newGameButton = new Button("New Game");
        addNewGameButtonEvent();
        disableButton(newGameButton);
        newGameButton.setId("newGameButton");
        newGameButton.getStyleClass().add("disabled");
        root.setRight(newGameButton);
        root.setAlignment(newGameButton, Pos.BOTTOM_CENTER);

        /********END FRONT END*****************/
        Game.beginGame();
        playerDiscard();
    }

    public static void updateHumanHand(List<Card> hand) {
        humanArea.getChildren().clear();
        int i = 0;

        for (Card card : hand) {
            try {
                String path = card.getImagePath();
                Image image = new Image(path, 120, 190, false, false);
                //makes a button for each of the player's cards and sets the background image as the card itself.
                Button cardButton = new Button("", new ImageView(image));
                cardButton.setId(i + "playerCard");
                if (firstTurn) {
                    cardButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new getButtonId());
                }
                humanArea.getChildren().addAll(cardButton);
                i++;
            }
            catch (Exception e) {e.printStackTrace();}
        }
    }

    public static void updateBotHand(List<Card> hand) {
        botArea.getChildren().clear();
        int i = 0;

        for (Card card : hand) {
            try {
                String path = card.getImagePath();
                Image image = new Image(path, 120, 190, false, false);
                //makes a button for each of the bot's cards and sets the background image as the card itself.
                Button cardButton = new Button("", new ImageView(image));
                cardButton.setId(i + "botCard");
                botArea.getChildren().addAll(cardButton);
                i++;
            }
            catch (Exception e) {e.printStackTrace();}
        }
    }

    private static class getButtonId implements EventHandler<Event>{
        @Override
        public void handle(Event evt) {
            Integer cardId = Integer.parseInt(((Control)evt.getSource()).getId().substring(0, 1));

            if (toBeDiscarded.contains(cardId)) {
                toBeDiscarded.remove(cardId);
                ((Control) evt.getSource()).getStyleClass().remove("addBorder");
            }
            else {
                toBeDiscarded.add(cardId);
                //((Control) evt.getSource()).getStyleClass().clear();
                ((Control) evt.getSource()).getStyleClass().add("addBorder");
            }
        }
    }

    public static void changeCenterMessage(String message) {
        Label currentAction = new Label(message);
        currentAction.setId("currentAction");
        root.setCenter(currentAction);
    }

    //overloaded method for debugging score values
    public static void changeCenterMessage(String message, int playerScore, int botScore) {
        Label currentAction = new Label(message + "\nPlayer score: " + playerScore + "\nBot score: " + botScore);
        currentAction.setId("currentAction");
        root.setCenter(currentAction);
    }

    public void playerDiscard() {
        changeCenterMessage("Choose your cards to discard or choose to fold");
        discardButton.getStyleClass().add("gameButton");
        discardButton.getStyleClass().remove("discard");
        discardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                disableButton(discardButton);
                firstTurn = false;
                Game.discard(toBeDiscarded);
                List<Card> deck = Card.getDeck();
                Game.dealHuman(deck);
            }
        });

    }

    public void addNewGameButtonEvent() {
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                beginNewGame();
            }
        });
    }

    public static void disableButton(Button b) {
        b.setDisable(true);
        b.getStyleClass().remove("gameButton");
        b.getStyleClass().add("disabled");
    }

    public static void enableButton(Button b) {
        b.setDisable(false);
        b.getStyleClass().add("gameButton");
        b.getStyleClass().remove("disabled");
    }

    public static Button getNewGameButton() {
        return newGameButton;
    }

    private void beginNewGame() {
        disableButton(newGameButton);
        firstTurn = true;
        Game.emptyHands();
        startGame();
    }
}

