package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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


    public void setElements(List<Drawable> elements){
        this.elements = elements;
        for (Drawable drawable : elements){
            scene.getChildren().addAll(drawable.getGUI());
            if(drawable instanceof Time){
                updates.add((Time) drawable);
            }
        }
    }

    public void setElementsScene2(List<Shape> elements){
        for (Shape shape : elements){
            scene2.getChildren().addAll(shape);
        }
    }

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

    @FXML
    private void onZoom(){
        scene.setScaleX((slider.getValue()/10)+1);
        scene.setScaleY((slider.getValue()/10)+1);
        scroll.setHvalue(0.1+slider.getValue()/120);
        scroll.setVvalue(0.1+slider.getValue()/120);
        scene.layout();
    }

    @FXML
    private void speedChange(){
        period = (long) (1000 - speeder.getValue());
        this.startTime();
    }

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
    }

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

    @FXML
    private void onClickScene(){
        Bus.clearPicked();
        scene2.getChildren().clear();
    }

}

