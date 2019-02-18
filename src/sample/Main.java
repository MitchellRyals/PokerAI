package sample;

import javafx.application.Application;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //import the fxml file
        VBox root = new VBox();
        //root.setAlignment(Pos.CENTER);

        primaryStage.setScene(new Scene(root, 1200, 900));
        //applies the css file "poker.css" to the project
        root.getStylesheets().add(this.getClass().getResource("poker.css").toExternalForm());

        BorderPane botAreaPane = new BorderPane();
        HBox botArea = new HBox();
        botAreaPane.setTop(botArea);

        BorderPane humanAreaPane = new BorderPane();
        HBox humanArea = new HBox();
        humanAreaPane.setBottom(humanArea);

        BorderPane cardAreaPane = new BorderPane();
        HBox cardArea = new HBox();
        cardAreaPane.setCenter(cardArea);

        Label l = new Label("hello");
        humanArea.getChildren().addAll(l);

        primaryStage.setTitle("Poker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

