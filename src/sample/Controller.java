/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

/**
 * Hlavni trida pro aplikaci
 * @file Controller.java.
 * @author Jakub Sekula (xsekul01)
 * @author Ondrej Potúček (xpotuc06)
 *
 */

package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.*;

/**
 * Hlavni Trida pro grafiku
 */
public class Controller {

    public int timerTime = 0;

    @FXML
    private Pane scene;
    @FXML
    private Pane scene2;
    @FXML
    private Slider slider;
    @FXML
    private Pane parentScene;
    @FXML
    private Slider speeder;
    @FXML
    private TextField timeText;
    @FXML
    private ScrollPane scroll;
    @FXML
    private ToggleButton changeRoute;

    private List<Drawable> elements = new ArrayList<>();
    private List<Time> updates = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.of(12, 00, 00);
    long period = 1000;


    /**
     * Metóda pre vykreslenie elementov na mapu.
     * @param elements List objektov na vykreslenie
     */
    public void setElements(List<Drawable> elements){
        this.elements = elements;
        for (Drawable drawable : elements){
            scene.getChildren().addAll(drawable.getGUI());
            if(drawable instanceof Time){
                updates.add((Time) drawable);
            }
        }
    }

    /**
     * Metóda pre vykreslenie elementov na spodnú plochu.
     * @param elements List objektov na vykreslenie
     */
    public void setElementsScene2(List<Shape> elements){
        for (Shape shape : elements){
            scene2.getChildren().addAll(shape);
        }
    }

    /**
     * Metoda nastartuje hodiny
     */
    public void startTime(){
        if(timer != null) timer.cancel();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        time = time.plusSeconds(1);
                        timerTime++;
                        String timeToWrite = time.toString();
                        if(timeToWrite.length() > 8) timeToWrite = timeToWrite.substring(0,8);
                        if( timeToWrite.length() == 5 ){
                            timeToWrite = timeToWrite + ":00";
                        }
                        timeText.setText(timeToWrite);
                        for(Time update : updates){
                            update.update(time);
                        }
                        if(Bus.busLink != null){
                            setElementsScene2(Bus.busLink);
                            Bus.busLink = null;
                        }
                    }
                });
            }
        }, 0, period);
    }

    /**
     * Metoda priblizuje scenu
     */
    @FXML
    private void onZoom(){
        scene.setScaleX((slider.getValue()/10)+1);
        scene.setScaleY((slider.getValue()/10)+1);
        scroll.setHvalue(0.1+slider.getValue()/120);
        scroll.setVvalue(0.1+slider.getValue()/120);
        scene.layout();
    }

    /**
     * Metoda urychluje beh programu
     */
    @FXML
    private void speedChange(){
        period = (long) (1000 - speeder.getValue());
        this.startTime();
    }

    /**
     * Metoda zresetuje simulaci
     */
    @FXML
    private void onClickReset(){
        scene.setScaleX(1);
        scene.setScaleY(1);
        slider.setValue(0);
        speeder.setValue(0);
        time = LocalTime.of(12, 00, 00);
        this.speedChange();
        scroll.setHvalue(0.1);
        scroll.setVvalue(0.1);
        Street.clearAtlernateRoute();
        changeRoute.setText("Zmena trasy");
        changeRoute.setSelected(false);
        Street.changingLink = false;
        scene2.getChildren().clear();
        scene.getChildren().clear();

        HashMap<String, Street> phony = new HashMap<>();
        HashMap<String, sample.Line> phonyl = new HashMap<>();

        ReadCSV map = new ReadCSV("newyork.csv", "Map", phony, phonyl);

        HashMap<String, Street> mapHash = map.getMapHash();
        HashMap<String, Stop> stopsHash = map.getStopsHash();


        ReadCSV lined = new ReadCSV("link.csv", "Line", mapHash, phonyl);
        HashMap<String, Line> lines = lined.getLineHash();

        ReadCSV bus = new ReadCSV("Bus.csv", "Bus", mapHash, lines);
        HashMap<String, HashMap<String, Bus>> busses = bus.getBusHash();

        setElements(new ArrayList(mapHash.values()));
        setElements(new ArrayList(stopsHash.values()));

        Set<String> test = busses.keySet();

        for(String key : test){
            setElements( new ArrayList( busses.get(key).values() ) );
        }
        startTime();

    }

    /**
     * Metoda zmeni napis na tlacitku
     */
    @FXML
    private void onClickChange(){
        if(changeRoute.isSelected()){
            changeRoute.setText("Hotovo");
            Street.changingLink = true;
        }
        else{
            changeRoute.setText("Zmena trasy");
            Street.changingLink = false;
            Street.alternateRoute.clear();
            Street.usingKey = null;
        }
    }

    /**
     * Metoda zrusi zbarveni dane linky
     */
    @FXML
    private void onClickScene(){
        Bus.clearPicked();
        scene2.getChildren().clear();
    }

}

