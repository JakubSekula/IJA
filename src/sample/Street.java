package sample;

import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Street implements Drawable {
    private Shape street;
    String id, name;
    Coordinate middle;
    Stop stop;
    List<Coordinate> coords = new ArrayList<Coordinate>();

    public Street(String str, String name, Coordinate c0, Coordinate c1){
        id = str;
        this.name = name;
        coords.add(c0);
        coords.add(c1);
        street = new Line(c0.getX(), c0.getY(), c1.getX(), c1.getY());
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

    Stop getStop(){
        return stop;
    }

    public boolean addStop(Stop stop){
        try{
            if(stop != null) {
//                stop.position = getMiddle();
                this.stop = stop;
                stop.setStreet(this);
            }
            else return false;

        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public Coordinate begin(){
        return coords.get(0);
    }

    public Coordinate end(){
        return coords.get(coords.size()-1);
    }

    public boolean follows(Street s){
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

    void countMiddle(Coordinate c0, Coordinate c1){
        int startX = c0.getX();
        int startY = c0.getY();
        int endX = c1.getX();
        int endY = c1.getY();

        float midX = 0;
        float midY = 0;

        float XDifference = endX - startX;
        float YDifference = endY - startY;

        midX = abs( XDifference ) / 2;

        midY = abs( YDifference ) / 2;

        if( startX > endX ){
            midX = endX + midX;
        } else {
            midX = startX + midX;
        }

        if( startY > endY ){
            midY = endY + midY;
        } else {
            midY = startY + midY;
        }
        this.middle = new Coordinate( (int) midX, (int) midY );
    }

    public Coordinate getMiddle(){
        countMiddle(this.begin(), this.end());
        return middle;
    }

    public Coordinate getStreetStart(){
        return coords.get(0);
    }

    public Coordinate getStreetEnd(){
        return coords.get(coords.size()-1);
    }

    public boolean equals(Street street){
        if( ( this.getStreetEnd().getX() == street.getStreetStart().getX() && this.getStreetEnd().getY() == street.getStreetStart().getY() ) ||
        ( this.getStreetEnd().getX() == street.getStreetEnd().getX() && this.getStreetEnd().getY() == street.getStreetEnd().getY() )){
            return true;
        } else if( ( this.getStreetStart().getX() == street.getStreetEnd().getX() && this.getStreetStart().getY() == street.getStreetEnd().getY() ) ||
        ( this.getStreetStart().getX() == street.getStreetStart().getX() && this.getStreetStart().getY() == street.getStreetStart().getY() ) ) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Shape getGUI() {
        return street;
    }
}
