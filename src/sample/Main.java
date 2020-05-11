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

        HashMap<String, Street> phony = new HashMap<>();
        HashMap<String, Line> phonyl = new HashMap<>();

        ReadCSV map = new ReadCSV("newyork.csv", "Map", phony, phonyl);

        HashMap<String, Street> mapHash = map.getMapHash();
        HashMap<String, Stop> stopsHash = map.getStopsHash();

        ReadCSV lined = new ReadCSV("Bus.csv", "Bus", mapHash, phonyl);
        HashMap<String, Line> lines = lined.getLineHash();

        ReadCSV bus = new ReadCSV("Bus.csv", "Bus", mapHash, lines);
        HashMap<String, HashMap<String, Bus>> busses = bus.getBusHash();

        Controller controller = loader.getController();
        controller.setElements(new ArrayList(mapHash.values()));
        controller.setElements(new ArrayList(stopsHash.values()));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
