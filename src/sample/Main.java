package sample;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //create the root node
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1200, 900);

        //applies the css file "poker.css" to the project
        scene.getStylesheets().add(this.getClass().getResource("poker.css").toExternalForm());

        AnchorPane botArea = new AnchorPane();
        botArea.setMinHeight(250);
        root.setTop(botArea);

        AnchorPane humanArea = new AnchorPane();
        humanArea.setId("humanArea");
        humanArea.setMinHeight(250);
        root.setBottom(humanArea);

        AnchorPane cardArea = new AnchorPane();
        cardArea.setId("cardArea");
        root.setCenter(cardArea);

        Label botMoney = new Label("$0.00");
        botMoney.setId("botMoney");
        botArea.setLeftAnchor(botMoney, 10.0);
        botArea.getChildren().addAll(botMoney);

        Label humanMoney = new Label("$0.00");
        humanMoney.setId("humanMoney");
        humanArea.setLeftAnchor(humanMoney, 10.0);
        humanArea.getChildren().addAll(humanMoney);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Poker");
        primaryStage.show();

        //************END FRONT END************
    }

    public static void main(String[] args) {
        launch(args);
    }
}

