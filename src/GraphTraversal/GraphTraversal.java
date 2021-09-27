package GraphTraversal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class GraphTraversal extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage = FXMLLoader.load(getClass().getResource("GraphTraversal.fxml"));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
