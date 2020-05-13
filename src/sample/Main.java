/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

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


        ReadCSV lined = new ReadCSV("link.csv", "Line", mapHash, phonyl);
        HashMap<String, Line> lines = lined.getLineHash();

        ReadCSV bus = new ReadCSV("Bus.csv", "Bus", mapHash, lines);
        HashMap<String, HashMap<String, Bus>> busses = bus.getBusHash();

        Controller controller = loader.getController();
        controller.setElements(new ArrayList(mapHash.values()));
        controller.setElements(new ArrayList(stopsHash.values()));

        Set<String> test = busses.keySet();

        for(String key : test){
            controller.setElements( new ArrayList( busses.get(key).values() ) );
        }
        controller.startTime();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
