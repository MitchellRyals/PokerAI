package sample;

//TODO: Add bot. Modify Game's finalizeRound().

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.text.TextAlignment;
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
    //Most of these shouldn't have been static but by the time I realized, most of the code was already done and it would require
    //extensive work or an entire rewriting of all the code to fix. It shouldn't cause many (if any at all) issues, but I am still a little disappointed by it
    //maybe some time after the semester is over I'll have enough time to fully fix it properly.
    private final int INITIAL_CASH = 100;
    private static HBox botArea = new HBox();
    private static HBox humanArea = new HBox();
    private static BorderPane root;
    private static Button discardButton;
    private static Button newGameButton;
    private static Button nextRoundButton;
    private static Button foldButton;
    private static Button increasePlayerBet;
    private static Button decreasePlayerBet;
    private static Button callButton;
    private static Button raiseButton;
    private static VBox centerLabelContainer;
    private static VBox moneyContainer;
    private static VBox playerButtonContainer;
    private static Label botMoney;
    private static Label botActionLabel;
    private static Label humanMoney;
    private static Label humanBetLabel;
    private static Label currentActionLabel;
    private static int playerCash;
    private static int botCash;
    private static int playerBet;
    private static boolean firstTurn = true;
    private static List<Integer> toBeDiscarded = new ArrayList<>();

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

        VBox title = new VBox();
        title.setAlignment(Pos.CENTER);

        Image titleImage = new Image("sample/cards/title.png");
        ImageView titleImageView = new ImageView(titleImage);
        titleImageView.setFitHeight(250);
        titleImageView.setFitWidth(400);
        title.getChildren().add(titleImageView);
        title.getChildren().add(startButton);

        root.setCenter(title);

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

        botArea.setId("botArea");
        botArea.setMinHeight(250);
        botArea.setAlignment(Pos.TOP_CENTER);
        botAreaContainer.getChildren().add(botArea);
        botAreaContainer.setAlignment(botArea, Pos.TOP_CENTER);

        newGameButton = new Button("New Game");
        addNewGameEvent();
        newGameButton.setId("newGameButton");
        newGameButton.getStyleClass().add("gameButton");
        botAreaContainer.getChildren().add(newGameButton);
        botAreaContainer.setAlignment(newGameButton, Pos.TOP_RIGHT);

        humanArea.setId("humanArea");
        humanArea.setMinHeight(250);
        humanArea.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(humanArea);

        centerLabelContainer = new VBox();
        centerLabelContainer.setId("centerLabelContainer");
        centerLabelContainer.setAlignment(Pos.CENTER);
        root.setCenter(centerLabelContainer);

        currentActionLabel = new Label();
        currentActionLabel.setId("currentAction");
        currentActionLabel.setTextAlignment(TextAlignment.CENTER);
        centerLabelContainer.getChildren().addAll(currentActionLabel);

        Separator horizontalRule = new Separator();
        horizontalRule.setId("separator");
        centerLabelContainer.getChildren().addAll(horizontalRule);

        botActionLabel = new Label();
        botActionLabel.setId("currentBotAction");
        botActionLabel.setTextAlignment(TextAlignment.CENTER);
        centerLabelContainer.getChildren().addAll(botActionLabel);

        playerButtonContainer = new VBox();
        playerButtonContainer.setId("playerButtonContainer");
        playerButtonContainer.setAlignment(Pos.BOTTOM_CENTER);
        playerButtonContainer.setSpacing(5);
        playerButtonContainer.setPrefWidth(150);
        root.setLeft(playerButtonContainer);

        moneyContainer = new VBox();
        moneyContainer.setId("moneyContainer");
        moneyContainer.setAlignment(Pos.BOTTOM_CENTER);
        moneyContainer.setSpacing(5);
        root.setRight(moneyContainer);

        botMoney = new Label("$" + Integer.toString(botCash));
        botMoney.getStyleClass().add("cashLabel");
        moneyContainer.getChildren().addAll(botMoney);

        Region moneyContainerSeparator = new Region();
        moneyContainer.setVgrow(moneyContainerSeparator, Priority.ALWAYS);
        moneyContainer.getChildren().addAll(moneyContainerSeparator);

        increasePlayerBet = new Button("Bet +10");
        addBetButtonEvent(increasePlayerBet, 10);
        disableButton(increasePlayerBet);
        increasePlayerBet.setId("increasePlayerBet");
        increasePlayerBet.getStyleClass().add("gameButton");
        increasePlayerBet.setMinWidth(playerButtonContainer.getPrefWidth());
        moneyContainer.getChildren().addAll(increasePlayerBet);

        humanBetLabel = new Label("Betting:\n$10");
        humanBetLabel.getStyleClass().add("cashLabel");
        moneyContainer.getChildren().addAll(humanBetLabel);

        decreasePlayerBet = new Button("Bet -10");
        addBetButtonEvent(decreasePlayerBet,-10);
        disableButton(decreasePlayerBet);
        decreasePlayerBet.setId("decreasePlayerBet");
        decreasePlayerBet.getStyleClass().add("gameButton");
        decreasePlayerBet.setMinWidth(playerButtonContainer.getPrefWidth());
        moneyContainer.getChildren().addAll(decreasePlayerBet);

        humanMoney = new Label("$" + Integer.toString(playerCash));
        humanMoney.getStyleClass().add("cashLabel");
        moneyContainer.getChildren().addAll(humanMoney);

        nextRoundButton = new Button("Next Round");
        addNextRoundButtonEvent();
        disableButton(nextRoundButton);
        nextRoundButton.setId("nextRoundButton");
        nextRoundButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(nextRoundButton);

        foldButton = new Button("Fold");
        addFoldButtonEvent();
        disableButton(foldButton);
        foldButton.setId("foldButton");
        foldButton.getStyleClass().add("gameButton");
        foldButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(foldButton);

        Region buttonContainerSeparator = new Region();
        buttonContainerSeparator.setId("separator");
        playerButtonContainer.setVgrow(buttonContainerSeparator, Priority.ALWAYS);
        playerButtonContainer.getChildren().addAll(buttonContainerSeparator);

        raiseButton = new Button("Raise");
        addRaiseButtonEvent();
        disableButton(raiseButton);
        raiseButton.setId("raiseButton");
        raiseButton.getStyleClass().add("gameButton");
        raiseButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(raiseButton);

        callButton = new Button("Call");
        addCallButtonEvent();
        disableButton(callButton);
        callButton.setId("callButton");
        callButton.getStyleClass().add("gameButton");
        callButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(callButton);

        discardButton = new Button("Discard");
        addDiscardButtonEvent();
        discardButton.setId("discardButton");
        discardButton.getStyleClass().add("gameButton");
        discardButton.setMinWidth(playerButtonContainer.getPrefWidth());
        playerButtonContainer.getChildren().addAll(discardButton);

        /********END FRONT END*****************/
        Game.beginGame();
        changeCenterMessage("Choose your cards to discard or choose to fold");
    }

    private void addNewGameEvent() {
        newGameButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                disableButton(nextRoundButton);
                firstTurn = true;
                Game.emptyHands();
                resetCashValues();
                startGame();
            }
        });
    }

    private void addBetButtonEvent(Button button, int betValue) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //only allow this button to do anything after discarding
                if (!firstTurn) {
                    //do not let betting go over the player's cash or be less than 10
                    if (playerBet + betValue <= playerCash && playerBet + betValue > 0) {
                        playerBet += betValue;
                        setPlayerBetLabel(playerBet);
                    }
                }
            }
        });
    }

    private void addFoldButtonEvent() {
        foldButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (!firstTurn) {
                    changeCenterMessage("Folded. Press Next Round to continue.");
                    playerBet = 10;
                    setPlayerBetLabel(playerBet);
                    disableButton(foldButton);
                    disableButton(callButton);
                    disableButton(raiseButton);
                    enableButton(nextRoundButton);
                }
            }
        });
    }

    private void addRaiseButtonEvent() {
        raiseButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (!firstTurn) {
                    if (playerBet > Bot.getBetAmount()) {
                        setPlayerBetLabel(playerBet);
                        changeCenterMessage("Raised for $" + playerBet);
                        disableButton(callButton);
                        disableButton(raiseButton);
                        disableButton(foldButton);
                        Game.finalizeRound();
                    }
                }
            }
        });
    }

    private void addCallButtonEvent() {
        callButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (!firstTurn) {
                    if (playerCash >= Bot.getBetAmount())
                        playerBet = Bot.getBetAmount();
                    else
                        playerBet = playerCash;
                    setPlayerBetLabel(playerBet);
                    changeCenterMessage("Called for $" + playerBet);
                    disableButton(callButton);
                    disableButton(raiseButton);
                    disableButton(foldButton);
                    Game.finalizeRound();
                }
            }
        });
    }

    private void addNextRoundButtonEvent() {
        nextRoundButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                beginNextRound();
            }
        });
    }

    private void addDiscardButtonEvent() {
        discardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                disableButton(discardButton);
                firstTurn = false;
                playerBet = 10;
                enableButton(increasePlayerBet);
                enableButton(decreasePlayerBet);
                enableButton(raiseButton);
                enableButton(callButton);
                enableButton(foldButton);
                Game.discard(toBeDiscarded, true);
                Game.postDiscardRound();
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
        currentActionLabel.setText(message);
    }

    //overloaded method for displaying score values
    public static void changeCenterMessage(String message, int playerScore, int botScore) {
        currentActionLabel.setText(message + "\nPlayer score: " + playerScore + "\nBot score: " + botScore);
    }

    public static void changeBotActionMessage(String message) {
        botActionLabel.setText(message);
    }

    private void beginNextRound() {
        disableButton(nextRoundButton);
        firstTurn = true;
        Game.emptyHands();
        startGame();
    }

    private void isGameOver(boolean moneyZero) {
        if (moneyZero) {
            for (Node b: playerButtonContainer.getChildren()) {
                if (b.getId() != "separator")
                    disableButton((Button) b);
            }
        }
    }

    public static Button getNextRoundButton() { return nextRoundButton; }
    public static Button getNewGameButton() { return newGameButton; }
    public static int getPlayerCash() { return playerCash; }
    public static int getBotCash() { return botCash; }
    public static int getPlayerBet() { return playerBet; }

    public static void setPlayerCash(int cash) {
        playerCash = cash;
        humanMoney.setText("$" + Integer.toString(playerCash));
    }
    public static void setBotCash(int cash) {
        botCash = cash;
        botMoney.setText("$" + Integer.toString(botCash));
    }
    public static void setPlayerBetLabel(int betAmount) {
        humanBetLabel.setText("Betting:\n$" + Integer.toString(betAmount));
    }


    //this pseudo class handles the discarding of cards when the discard button is clicked
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
