package sample;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //import the fxml file
        Pane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Poker");

        primaryStage.setScene(new Scene(root, 1200, 900));
        //applies the css file "poker.css" to the project
        root.getStylesheets().add(this.getClass().getResource("poker.css").toExternalForm());

        GridPane botArea = new GridPane();
        botArea.setAlignment(Pos.TOP_CENTER);
        botArea.setHgap(10);
        root.getChildren().add(botArea);

        /*GridPane humanArea = new GridPane();
        humanArea.setId("humanArea");
        humanArea.setAlignment(Pos.BOTTOM_CENTER);
        humanArea.setHgap(10);
        root.getChildren().add(humanArea);
        */

        GridPane cardArea = new GridPane();
        cardArea.setAlignment(Pos.CENTER);
        cardArea.setHgap(10);
        root.getChildren().add(cardArea);

        Label l = new Label("hello");
        //Controller.humanArea.add(l, 1, 1);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

