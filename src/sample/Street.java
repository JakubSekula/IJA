package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

public class Street implements Drawable {
    private Shape street;
    String id, name;
    Coordinate middle;
    float streetLength = 0;
    Stop stop = null;
    List<Coordinate> coords = new ArrayList<Coordinate>();
    public int color = 1;   //1-green, 2-orange, 3-red
    public boolean changeable = true;
    public static HashMap<String, List<Street>> Detour = new HashMap<>();
    public static String usingKey = null;
    public static boolean changingLink = false;
    public static List<Street> alternateRoute = new ArrayList<>(); //!< Vektor ciest v obchadzke, prva cesta je uzavreta a ostatne su obchadzka


    private Color green = Color.rgb(99, 214, 104);
    private Color orange = Color.rgb(255,151,77);
    private Color red = Color.rgb(242,60,50);
    private  Color blue = Color.rgb(0,170,240);
    private  Color grey = Color.rgb(170,170,170);

    public Street(String str, String name, Coordinate c0, Coordinate c1){
        id = str;
        this.name = name;
        coords.add(c0);
        coords.add(c1);
        street = new Line(c0.getX(), c0.getY(), c1.getX(), c1.getY());
        street.setStrokeWidth(1.5);
        street.setStroke(green);

        countStreetLength();

        street.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event){
                if(event.getButton() == MouseButton.PRIMARY && changeable ) {
                    if (street.getStroke() == green) {
                        street.setStroke(orange);
                        color = 2;
                    } else if (street.getStroke() == orange) {
                        street.setStroke(red);
                        color = 3;
                    } else if (street.getStroke() == red) {
                        street.setStroke(green);
                        color = 1;
                    }
                }
                else if(changeable){
                    if(street.getStroke() == blue || (street.getStroke() == grey && alternateRoute.size() == 1)){
                        setBackColor();
                    }
                    else if(street.getStroke() != grey){
                        closeStreet();
                    }
                }
            }
        });
    }

    public static void clearAtlernateRoute(){
        while(alternateRoute.size() != 0){
            alternateRoute.get(0).setBackColor();
        }
    }

    private void closeStreet() {
        if(changingLink){
            // System.out.println( this.getId() );
            if(alternateRoute.size() == 0){
                usingKey = this.getId();
                List<Street> test2 = new ArrayList<>();
                Detour.put( usingKey, test2 );
                street.setStroke(grey);
            }
            else{
                street.setStroke(blue);
            }
            List<Street> test = Detour.get( usingKey );
            test.add( this );
            Detour.put( usingKey, test );
            alternateRoute.add(this);
        }
    }

    private void setBackColor() {
        alternateRoute.remove(this);
        if(this.color == 1) this.street.setStroke(green);
        else if(this.color == 2) this.street.setStroke(orange);
        else if(this.color == 3) this.street.setStroke(red);
    }

    private void countStreetLength(){
        float XDiff = Math.abs( coords.get( 0 ).getX() - coords.get( 1 ).getX() );
        float YDiff = Math.abs( coords.get( 0 ).getY() - coords.get( 1 ).getY() );

        float hypotenuse = ( float ) Math.sqrt( Math.pow( XDiff, 2 ) + Math.pow( YDiff, 2 ));

        streetLength = hypotenuse;

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
        if( this.stop != null ){
            return stop;
        } else {
            return null;
        }
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
        float startX = c0.getX();
        float startY = c0.getY();
        float endX = c1.getX();
        float endY = c1.getY();

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
        this.middle = new Coordinate( (float) midX, (float) midY );
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

    public boolean Direction( Street s2 ){
        if( ( ( this.getStreetStart().getX() == s2.getStreetStart().getX() ) && ( this.getStreetStart().getY() == s2.getStreetStart().getY() ) ) ||
            ( ( this.getStreetStart().getX() == s2.getStreetEnd().getX() ) && ( this.getStreetStart().getY() == s2.getStreetEnd().getY() ) ) ){
            // ma se poslat konec, start
            return false;
        } else {
            // ma se poslat start, konec
            return true;
        }
    }

    @Override
    public Shape getGUI() {
        return street;
    }
}
