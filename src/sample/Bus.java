package sample;

import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.exit;

public class Bus implements Drawable, Time{
    private Shape bus;
    HashMap<String, Street> streets = new HashMap<String, Street>();
    String id, name;
    List<List<String>> plannedStops = new ArrayList<List<String>>();
    List<Street> route = new ArrayList<Street>();
    List<String> stopsOnRoute = new ArrayList<String>();
    int currentStops = 0;
    String start;
    float posX, posY;
    Street current;
    int currenti;

    @Override
    public Shape getGUI() {
        return bus;
    }

    @Override
    public void update(LocalTime time) {

    }

    void getStreets(HashMap<String, Street> str){
        this.streets = str;
    }

    void nameIt(String str){
        this.name = str;
    }

    void getStops(){
        for(int i = 0; i < route.size(); ++i){
            if(route.get(i).getStop() != null){
                stopsOnRoute.add(route.get(i).getStop().getId());
            }
        }

        // -1 protoze zastavka na konci je stejna jako na zacatku kvuli dojeti na zastavku
        for( int i = currentStops; i < stopsOnRoute.size() - 1; i++ ){
            if( !stopsOnRoute.get(i).equals(plannedStops.get(i).get(0))){
                System.out.println("Error: Zastavky na ceste se neshoduji se zadanymi");
                exit(60);
            }
        }
    }

    void setStart(String time){
        this.start = time;
    }

    void setRout(String streetId){
        int size = route.size();
        if( size == 0 ){
            // prvni vzdy pushuji
            route.add( streets.get(streetId) );
        } else if( route.get(size - 1).equals( streets.get(streetId) ) ){
            // jestlize na sebe navazuji
            route.add( streets.get(streetId) );
        } else {
            exit( 16 );
        }

        this.posX = route.get(0).getStreetStart().getX();
        this.posY = route.get(0).getStreetStart().getY();
        this.current = route.get(0);
        this.currenti = 1;
    }


}
