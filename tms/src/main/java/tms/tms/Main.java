package tms.tms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    Stage window;

    @Override
    public void start(Stage stage) throws IOException {

        System.out.println("Loading main menu window");

        window = stage;

        // start off with main menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();

        MainController myController = loader.getController();
        window.setTitle("Event Management System");
        window.setScene(new Scene(root, 1000, 800));
        myController.changeTab();
        myController.loadFilters();
        myController.setData();
        window.show();
    }
    public void setScene(Scene scene) {
        window.setScene(scene);
    }

    public static void setMySQL() {
        Globals.setDb_name("tms");
        Globals.setDb_username("root");
        Globals.setDb_pass("");
    }

    public static void main(String[] args) {
        setMySQL();
        launch(args);
    }
}