package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

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

    private List<Drawable> elements = new ArrayList<>();
    private List<Time> updates = new ArrayList<>();
    private Timer timer;
    private LocalTime time = LocalTime.now();

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
        timer = new Timer(false);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time = time.plusSeconds(1);
                for(Time update : updates){
                    update.update(time);
                }
            }
        }, 0, 1000);
    }

    @FXML
    private void onZoom(){
        scene.setScaleX((slider.getValue()/100)+1);
        scene.setScaleY((slider.getValue()/100)+1);
//        System.out.println(scene.getScaleX());
        parentScene.setPrefSize(1250+slider.getValue()*2, 750+slider.getValue()*2);
        scene.layout();
    }

    @FXML
    private void speedChange(){

    }

    @FXML
    private void onClickReset(){
        parentScene.setPrefSize(1250, 750);
        scene.setScaleX(1);
        scene.setScaleY(1);
        slider.setValue(0);
    }

}
