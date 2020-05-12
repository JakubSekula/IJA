package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.exit;

public class Bus implements Drawable, Time{
    List<Street> route = new ArrayList<Street>();
    private Shape bus;
    HashMap<String, Street> streets = new HashMap<String, Street>();
    String id, name;
    List<List<String>> plannedStops = new ArrayList<List<String>>();
    List<String> stopsOnRoute = new ArrayList<String>();
    int currentStops = 0;
    String start;
    float posX, posY;
    Street current;
    float toStop = 0;
    int now = 0;
    int currenti = 0;
    float step;
    boolean recountStep = false;
    boolean countDistance = true;
    float travelledDistance = 0;
    boolean hasStop = true;
    float rest = 0;
    boolean stopped = false;
    boolean changeDir = true;
    boolean useRest = false;
    boolean atEnd = false;
    int timerTime = 0;
    boolean getDelay = true;
    boolean stationary = true;
    boolean round = false;

    private void changeTimer(){
        timerTime++;
        if( timerTime == 3600 ){
            timerTime = 0;
        }
    }

    private void shouldStop(){
        if( current.getStop() != null ){
            if( current.getStop().getId().equals( plannedStops.get( now + 1 ).get( 0 ) ) ){
                hasStop = true;
            }
        } else {
            hasStop = false;
        }
    }

    private void switchStreet(){
        current.changeable = true;
        if( currenti == route.size() - 1 ){
            currenti = -1;
            now = 0;
        }
        getDelay = true;
        currenti++;
        current = route.get( currenti );
        travelledDistance  = 0;
        stopped = false;
    }

    private void distanceBetweenStops(){
        float distance = 0;
        for( int i = currenti; i < route.size(); i++ ){
            if( route.get( i ).getStop() != null ){
                if( route.get( i ).getStop() != null && route.get( i ).getStop().getId().equals( plannedStops.get( now ).get( 0 ) ) ){
                    distance = distance + ( route.get( i ).streetLength / 2 );
                } else if ( route.get( i ).getStop() != null && route.get( i ).getStop().getId().equals( plannedStops.get( now + 1 ).get( 0 ) ) ) {
                    distance = distance + ( route.get( i ).streetLength / 2 );
                    break;
                } else {
                    distance = distance + ( route.get( i ).streetLength );
                }
            } else {
                distance = distance + ( route.get( i ).streetLength );
            }
        }
        toStop = distance;
    }

    private void changePos( float stepX, float stepY ){
        this.posX = this.posX + stepX;
        this.posY = this.posY + stepY;
        travelledDistance = travelledDistance + step;
    }

    private void countStep(){
        step = toStop / ( timeToNextStop() - 3 );
        recountStep = false;
    }

    private void setNull(){
        toStop = 0;
        now = 0;
        currenti = 0;
        step = 0;
        recountStep = false;
        countDistance = true;
        travelledDistance = 0;
        hasStop = true;
        rest = 0;
        stopped = false;
        changeDir = true;
        useRest = false;
        atEnd = false;
    }

    private void countAdditions( float sx, float sy, float ex, float ey ){

        if( countDistance ){
            distanceBetweenStops();
            countStep();
            countDistance = false;
        }

        if( recountStep ){
            countStep();
        }

        shouldStop();

        float XDiff = Math.abs( sx - ex );
        float YDiff = Math.abs( sy - ey );

        float hypotenuse = ( float ) Math.sqrt( Math.pow( XDiff, 2 ) + Math.pow( YDiff, 2 ));

        float stepX = 0;
        float stepY = 0;

        if( useRest ){
            step = step + rest;
            recountStep = true;
            useRest = false;
            travelledDistance = 0;
            rest = 0;
        }

        if (travelledDistance + step > hypotenuse) {
            rest = step;
            step = hypotenuse - travelledDistance;
            rest = rest - step;
            recountStep = true;
            useRest = true;
            if( atEnd == false ){
                switchStreet();
            }
        }

        if( hasStop ){
            if( currenti == 0 ){
                stepX =  XDiff / ( hypotenuse / ( step ) );
                stepY =  YDiff / ( hypotenuse / ( step ) );
                if( atEnd ){
                    if( travelledDistance + step >= hypotenuse ){
                        this.posX = current.getMiddle().getX();
                        this.posY = current.getMiddle().getY();
                        stationary = true;
                        round = true;
                        setNull();
                        return;
                    }
                }
            } else {
                if( travelledDistance + step >= hypotenuse / 2 &&  stopped == false ){
                    travelledDistance = hypotenuse / 2;
                    this.posX = current.getMiddle().getX();
                    this.posY = current.getMiddle().getY();
                    if( atEnd ){
                        return;
                    }
                    now++;
                    stationary = true;
                    stopped = true;
                    changeDir = false;
                    countDistance = true;
                } else {
                    stepX = XDiff / (hypotenuse / ( step ) );
                    stepY = YDiff / (hypotenuse / ( step ) );
                }
            }
        } else {
            stepX =  XDiff / ( hypotenuse / ( step ) );
            stepY =  YDiff / ( hypotenuse / ( step ) );
        }

        if( changeDir ) {
            if (sx < ex && sy < ey) {
                changePos(stepX, stepY);
            } else if (sx < ex && sy > ey) {
                changePos(stepX, -1 * stepY);
            } else if (sx > ex && sy < ey) {
                changePos(-1 * stepX, stepY);
            } else {
                changePos(-1 * stepX, -1 * stepY);
            }
        } else {
            changeDir = true;
        }

    }

    private int timeToNextStop(){
        int correction = 0;
        if( now == -1 ){
            correction = 1;
        }
        int time1 = countDeparture( plannedStops.get( now + correction ).get( 1 ) );
        int time2 = countDeparture( plannedStops.get( now + 1 + correction ).get( 1 ) );
        if( time1 > time2 ){
            int over = 3600 - time1;
            return over + time2;
        }
        return time2 - time1;
    }

    private int countDeparture( String time ){

        if( time.length() != 5 ){
            exit(20);
        }

        String[] parts = time.split( ":" );

        if( parts.length != 2 ){
            exit( 20 );
        }

        int minutes = Integer.parseInt( parts[ 0 ] );
        minutes = 60 * minutes;
        int sec = Integer.parseInt( parts[ 1 ] );

        minutes = minutes + sec;

        return minutes;
    }

    private void nextPos(){
        if( now != plannedStops.size() - 1 ) {
            int correction = 0;
            if( now == -1 ) {
                correction = 1;
            }

            if( timerTime == countDeparture( plannedStops.get( 0 ).get( 1 ) ) ){
                round = false;
            }

            if ( round == true || countDeparture(plannedStops.get(now + correction).get(1)) > timerTime && stationary ) {
                return;
            } else {
                stationary = false;
            }

        }

        if( currenti == route.size() - 1 ){
            currenti = 0;
            current = route.get( currenti );
            atEnd = true;
            now = -1;
        }

        if( getDelay ){
            current.changeable = false;
            System.out.println( current.color );
            getDelay = false;
        }

        if( currenti == 0 ){
            if( route.get( currenti ).Direction( route.get( currenti + 1 ) ) ){
                countAdditions( current.getMiddle().getX(), current.getMiddle().getY(), current.getStreetEnd().getX(), current.getStreetEnd().getY() );
            } else {
                countAdditions( current.getMiddle().getX(), current.getMiddle().getY(), current.getStreetStart().getX(), current.getStreetStart().getY() );
            }
        } else {
            if( route.get( currenti ).Direction( route.get( currenti + 1 ) ) ){
                countAdditions( current.getStreetStart().getX(), current.getStreetStart().getY(), current.getStreetEnd().getX(), current.getStreetEnd().getY() );
            } else {
                countAdditions( current.getStreetEnd().getX(), current.getStreetEnd().getY(), current.getStreetStart().getX(), current.getStreetStart().getY() );
            }
        }
        bus.setTranslateX( posX );
        bus.setTranslateY( posY );

    }

    @Override
    public Shape getGUI() {
        return bus;
    }

    @Override
    public void update(LocalTime time) {
        changeTimer();
        nextPos();
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

        posX = route.get( 0 ).getMiddle().getX();
        posY = route.get( 0 ).getMiddle().getY();

        bus = new Circle( 0, 0, 3, Color.RED );

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
        this.currenti = 0;
    }


}
