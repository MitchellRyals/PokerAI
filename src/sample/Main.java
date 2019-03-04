package sample;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//Cards courtesy of the ACBL
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //create the root node
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1200, 900);

        //applies the css file "poker.css" to the project
        scene.getStylesheets().add(this.getClass().getResource("poker.css").toExternalForm());

        Button startButton = new Button("Start Game");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startGame(root);
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

    public void startGame(BorderPane root) {
        HBox botArea = new HBox();
        botArea.setId("botArea");
        botArea.setMinHeight(250);
        botArea.setAlignment(Pos.TOP_CENTER);
        root.setTop(botArea);

        HBox humanArea = new HBox();
        humanArea.setId("humanArea");
        humanArea.setMinHeight(250);
        humanArea.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(humanArea);

        HBox cardArea = new HBox();
        cardArea.setId("cardArea");
        cardArea.setAlignment(Pos.CENTER);
        root.setCenter(cardArea);

        Label botMoney = new Label("$0.00");
        botMoney.setId("botMoney");
        botArea.getChildren().addAll();

        Label humanMoney = new Label("$0.00");
        humanMoney.setId("humanMoney");
        humanArea.getChildren().addAll();

        String path = System.getProperty("user.dir");
        try {
            FileInputStream card1Stream = new FileInputStream(path + "/src/sample/cards/1.png");
            Image card1 = new Image(card1Stream);
            humanArea.getChildren().addAll(new ImageView(card1));
        } catch (Exception e){}

        /********END FRONT END*****************/
        Card[] deck = new Card[52];
        deck = Card.createDeck();

    }
}

