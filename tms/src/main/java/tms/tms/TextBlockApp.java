package tms.tms;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TextBlockApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the list of items
        ObservableList<String> items = FXCollections.observableArrayList(
                "Item 1", "Item 2", "Item 3", "Item 4", "Item 5"
        );

        // Create the ListView control
        ListView<String> listView = new ListView<>(items);

        // Create a layout and add the ListView to it
        VBox root = new VBox(listView);

        // Create the scene and add the layout to it
        Scene scene = new Scene(root, 300, 250);

        // Set the title and show the stage
        primaryStage.setTitle("My List View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}