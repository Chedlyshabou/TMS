package tms.tms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class Dashboard extends Application {

    Stage window;

    @Override
    public void start(Stage stage) throws IOException, SQLException {

        System.out.println("Loading main menu window");

        window = stage;

        // start off with main menu
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Parent root = loader.load();

        DashboardController myController = loader.getController();
        window.setTitle("Dashboard");

        window.setScene(new Scene(root, 1540, 840));
        myController.addEventHandlers();
        System.out.println("reached");
        myController.HideUserVBox();
        myController.populateDashboard();
        window.getScene().getStylesheets().add(Dashboard.class.getResource("java-keywords.css").toExternalForm());
        window.getScene().getStylesheets().add(Dashboard.class.getResource("images/rich-text.css").toExternalForm());
        window.setMaximized(true);
        window.show();
    }

    public void setScene(Scene scene) {
        window.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}