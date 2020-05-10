package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Projekt IJA");
        Scene scene = new Scene(root, 1300, 800);
        primaryStage.setScene(scene);
        primaryStage.show();

        ReadCSV map = new ReadCSV("newyork.csv", "Map");
        HashMap<String, Street> mapHash = map.getMapHash();
        HashMap<String, Stop> stopsHash = map.getStopsHash();

        Controller controller = loader.getController();
        controller.setElements(new ArrayList(mapHash.values()));
        controller.setElements(new ArrayList(stopsHash.values()));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
