package sample;

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

    @FXML
    private Pane scene;
    @FXML
    private Pane scene2;
    @FXML
    private Slider slider;
    @FXML
    private Pane parentScene;
    @FXML
    private ScrollBar speeder;
    @FXML
    private TextField timeText;

    private List<Drawable> elements = new ArrayList<>();
    private List<Time> updates = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.of(12, 0, 0);

    public void setElements(List<Drawable> elements){
        this.elements = elements;
        for (Drawable drawable : elements){
            scene.getChildren().addAll(drawable.getGUI());
            if(drawable instanceof Time){
                updates.add((Time) drawable);
            }
        }
    }

    public void startTime(){
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                System.out.println( time );
                timeText.setText(time.toString().substring(0,8));
                for(Time update : updates){
                    update.update(time);
                }
            }
        }, 0, 1000);
    }

    @FXML
    private void onZoom(){
        scene.setScaleX((slider.getValue()/10)+1);
        scene.setScaleY((slider.getValue()/10)+1);
        scene.layout();
    }

    @FXML
    private void speedChange(){
        System.out.println("ahpj");
    }

    @FXML
    private void onClickReset(){
        scene.setScaleX(1);
        scene.setScaleY(1);
        slider.setValue(0);
        speeder.setValue(0);
    }

}
