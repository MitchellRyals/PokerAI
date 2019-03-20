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
    final int INITIAL_CASH = 100;
    private static HBox botArea = new HBox();
    private static HBox humanArea = new HBox();
    private static List<Integer> toBeDiscarded = new ArrayList<>();
    private static boolean firstTurn = true;
    private static BorderPane root;
    private static Button discardButton;
    private static Button newGameButton;
    private static Button nextRoundButton;
    private static Button foldButton;
    private static VBox moneyContainer;
    private static VBox playerButtonContainer;
    private static Label botMoney;
    private static Label humanMoney;
    private static int playerCash;
    private static int botCash;

    @Override
    public void start(Stage primaryStage) {
        resetCashValues();

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
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void startGame() {
        /*************THIS FUNCTION IS ALMOST ENTIRELY THE FRONT END**********/
        //this stack pane controls the entire top area
        StackPane botAreaContainer = new StackPane();
        botAreaContainer.setMinHeight(250);
        root.setTop(botAreaContainer);

        newGameButton = new Button("New Game");
        setNewGameEvent();
        newGameButton.setId("newGameButton");
        newGameButton.getStyleClass().add("gameButton");
        newGameButton.setLayoutX(5);
        newGameButton.setLayoutY(5);
        botAreaContainer.getChildren().add(newGameButton);
        botAreaContainer.setAlignment(newGameButton, Pos.TOP_RIGHT);

        botArea.setId("botArea");
        botArea.setMinHeight(250);
        botArea.setAlignment(Pos.TOP_CENTER);
        botAreaContainer.getChildren().add(botArea);
        botAreaContainer.setAlignment(botArea, Pos.TOP_CENTER);

        humanArea.setId("humanArea");
        humanArea.setMinHeight(250);
        humanArea.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(humanArea);

        HBox cardArea = new HBox();
        cardArea.setId("cardArea");
        cardArea.setAlignment(Pos.CENTER);
        root.setCenter(cardArea);

        playerButtonContainer = new VBox();
        playerButtonContainer.setId("playerButtonContainer");
        playerButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        playerButtonContainer.setSpacing(5);
        playerButtonContainer.setPrefWidth(150);
        root.setLeft(playerButtonContainer);

        moneyContainer = new VBox();
        moneyContainer.setId("moneyContainer");
        moneyContainer.setAlignment(Pos.BOTTOM_CENTER);
        root.setRight(moneyContainer);

        botMoney = new Label("$" + Integer.toString(botCash));
        botMoney.getStyleClass().add("cashLabel");
        moneyContainer.getChildren().addAll(botMoney);

        Region moneyContainerSeparator = new Region();
        moneyContainer.setVgrow(moneyContainerSeparator, Priority.ALWAYS);
        moneyContainer.getChildren().addAll(moneyContainerSeparator);

        humanMoney = new Label("$" + Integer.toString(playerCash));
        humanMoney.getStyleClass().add("cashLabel");
        moneyContainer.getChildren().addAll(humanMoney);

        nextRoundButton = new Button("Next Round");
        addNewGameButtonEvent();
        disableButton(nextRoundButton);
        nextRoundButton.setId("nextRoundButton");
        nextRoundButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(nextRoundButton);

        Region buttonContainerSeparator = new Region();
        playerButtonContainer.setVgrow(buttonContainerSeparator, Priority.ALWAYS);
        playerButtonContainer.getChildren().addAll(buttonContainerSeparator);

        foldButton = new Button("Fold");
        foldButton.setId("foldButton");
        foldButton.getStyleClass().add("gameButton");
        foldButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(foldButton);

        discardButton = new Button("Discard");
        discardButton.setId("discardButton");
        discardButton.getStyleClass().add("gameButton");
        discardButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(discardButton);

        /********END FRONT END*****************/
        Game.beginGame();
        playerDiscard();
    }

    private void setNewGameEvent() {
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                disableButton(nextRoundButton);
                firstTurn = true;
                Game.emptyHands();
                resetCashValues();
                startGame();
            }
        });
    }

    private void resetCashValues() {
        //sets both the player's and the bot's cash to the constant at the top of the program.
        //100 at the time of writing.
        playerCash = INITIAL_CASH;
        botCash = INITIAL_CASH;
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
        nextRoundButton.setOnAction(new EventHandler<ActionEvent>() {
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
        b.getStyleClass().remove("disabled");
        b.getStyleClass().add("gameButton");
    }

    private void beginNewGame() {
        disableButton(nextRoundButton);
        firstTurn = true;
        Game.emptyHands();
        startGame();
    }

    public static Button getNextRoundButton() {
        return nextRoundButton;
    }

    public static int getPlayerCash() { return playerCash; }
    public static int getBotCash() { return botCash; }

    public static void setPlayerCash(int cash) { playerCash = cash; }
    public static void setBotCash(int cash) { botCash = cash; }


    //this pseudo class handles the discard button
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
}

