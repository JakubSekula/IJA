package sample;

import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.HashMap;

public class Bus implements Drawable, Time{
    private Shape bus;
    HashMap<String, Street> streets = new HashMap<String, Street>();
    String id, name;

    @Override
    public Shape getGUI() {
        return bus;
    }

    @Override
    public void update(LocalTime time) {

    }





}
