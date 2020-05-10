package sample;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

public class Street implements Drawable {
    private List<Shape> street = new ArrayList<>();
    String id, name;
    List<Coordinate> coords = new ArrayList<Coordinate>();
//    List<Stop> stops = new ArrayList<Stop>();

    public Street(String str, String name, Coordinate c0, Coordinate c1){
        id = str;
        name = name;
        coords.add(c0);
        coords.add(c1);
        street.add(new Line(c0.getX(), c0.getY(), c1.getX(), c1.getY()));
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public List<Coordinate> getCoordinates(){
        return coords;
    }

//    public List<Stop> getStops(){
//        return stops;
//    }

//    public boolean addStop(Stop stop){
//        try{
//            if(inStreet(stop)){
//                stops.add(stop);
//                stop.setStreet(this);
//            }
//            else return false;
//
//        }
//        catch (Exception e){
//            return false;
//        }
//        return true;
//    }

    public Coordinate begin(){
        return coords.get(0);
    }

    public Coordinate end(){
        return coords.get(coords.size()-1);
    }

    public boolean follows​(Street s){
        if(s.begin().equals(this.begin())){
            return true;
        }
        else if(s.end().equals(this.begin())){
            return true;
        }
        else if(s.begin().equals(this.end())){
            return true;
        }
        else if(s.end().equals(this.end())){
            return true;
        }
        else return false;
    }

    @Override
    public List<Shape> getGUI() {
        return street;
    }
}
