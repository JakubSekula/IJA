/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

/**
 * Drzi informace o autobusech
 * @file Bus.java.
 * @author Jakub Sekula (xsekul01)
 * @author Ondrej Potúček (xpotuc06)
 *
 */

package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.exit;

/**
 *
 */
public class Bus implements Drawable, Time{
    List<Street> route = new ArrayList<Street>();
    static List<Street> blueRoute = new ArrayList<>();
    private Shape bus;
    HashMap<String, Street> streets = new HashMap<>();
    String name;
    List<List<String>> plannedStops = new ArrayList<List<String>>();
    List<String> stopsOnRoute = new ArrayList<>();
    public static List<Shape> busLink;
    int currentStops = 0;
    String start;
    float posX, posY;
    Street current;
    float toStop = 0;
    int now = 0;
    int currenti = 0;
    private int timeFromStop = 0;
    float step;
    boolean recountStep = false;
    boolean countDistance = true;
    float travelledDistance = 0;
    boolean hasStop = true;
    float rest = 0;
    boolean stopped = false;
    boolean changeDir = true;
    boolean deleteTime = false;
    boolean useRest = false;
    boolean atEnd = false;
    int timerTime = 0;
    int timeFromnow = 0;
    int currDelay = 0;
    int Delay = 0;
    boolean getDelay = true;
    private boolean recount = false;
    boolean stationary = true;
    boolean round = false;
    static boolean busClicked = false;

    /**
     * Metoda meni interni hondotu citace pri prekroceni hodiny na 0
     *
     */
    private void changeTimer(){
        timerTime++;
        if( timerTime == 3600 ){
            timerTime = 0;
        }

    }

    /**
     * Metoda zmeni aktualni cestu na objizdnou trasu
     *
     */
    private void changeRoute(){
        int Rindex = route.indexOf( route.get( currenti ).Detour.get(route.get( currenti ).getId() ).get( 0 ) );

        ArrayList<Street> tmpList = new ArrayList<>();

        for( int i = 1; i < current.Detour.get(route.get( currenti ).getId() ).size(); i++ ){
            tmpList.add( current.Detour.get(route.get( currenti ).getId() ).get( i ) );
        }

        Street tmp =  route.get( Rindex );

        for( int i = 0; i < plannedStops.size(); i++ ){
            if( tmp.getId().equals( plannedStops.get( now + 1 ).get( 0 ) ) ){
                plannedStops.remove( now + 1 );
                //now--;
            }
        }

        route.remove( Rindex );

        for( int i = tmpList.size() - 1; i >= 0; i-- ){
            route.add( Rindex, tmpList.get( i ) );
        }

        current = route.get( currenti );
        recount = true;

    }

    /**
     * Metoda zjisti, jestli by mel bus zastavit
     *
     */
    private void shouldStop(){
        if( current.getStop() != null ){
            if( current.getStop().getId().equals( plannedStops.get( now + 1 ).get( 0 ) ) ){
                hasStop = true;
            }
        } else {
            hasStop = false;
        }
    }

    /**
     * Metoda prepne ulice na jejich krizeni
     *
     */
    private void switchStreet(){
        current.changeable = true;
        if( currenti == route.size() - 1 ){
            currenti = -1;
            now = 0;
        }
        getDelay = true;
        currDelay = 0;
        currenti++;
        current = route.get( currenti );
        if( route.get( currenti ).Detour.containsKey( route.get( currenti ).getId() ) ){
            changeRoute();
        }
        travelledDistance  = 0;
        stopped = false;
    }

    /**
     * Metoda spocita vzdalenost mezi zastavkami
     *
     */
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

    /**
     * Metoda nastavi krok pro bus
     *
     * @param  stepX krok v ose X
     * @param  stepY krok v ose Y
     */
    private void changePos( float stepX, float stepY ){
        this.posX = this.posX + stepX;
        this.posY = this.posY + stepY;
        travelledDistance = travelledDistance + step;
    }

    /**
     * Metoda spocita delku kroku na ulici
     *
     */
    private void countStep(){
        if( timeFromnow != 0 ) {
            step = toStop / ( timeToNextStop() - timeFromnow  - 3 + currDelay );
        } else {
            step = toStop / ( timeToNextStop() - 3 + currDelay );
        }
        currDelay = 0;
        recountStep = false;
    }

    /**
     * Metoda inicializuje bus
     *
     */
    private void setNull(){
        toStop = 0;
        Delay = 0;
        now = 0;
        currenti = 0;
        current.changeable = true;
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

    /**
     * Metoda spocita stepX a stepY
     *
     * @param sx zacatek ulice v X
     * @param sy zacatek ulice v Y
     * @param ex konec ulice v X
     * @param ey konec ulice v Y
     */
    private void countAdditions( float sx, float sy, float ex, float ey ){

        if( countDistance || recount ){
            if( recount ){
                timeFromnow = timeFromStop;
            }
            if( deleteTime ){
                timeFromnow = 0;
                deleteTime  = false;
            }
            distanceBetweenStops();
            countStep();
            recount = false;
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
            if(!atEnd){
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
                        deleteTime = true;
                        timeFromStop = 0;
                        stationary = true;
                        round = true;
                        setNull();
                        return;
                    }
                }
            } else {
                if( travelledDistance + step >= hypotenuse / 2 && !stopped){
                    travelledDistance = hypotenuse / 2;
                    timeFromStop = 0;
                    this.posX = current.getMiddle().getX();
                    this.posY = current.getMiddle().getY();
                    deleteTime = true;
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

    /**
     * Metoda pocita cas prijezdu do dalsi zastavky
     *
     */
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

    /**
     * Metoda pocita cas prijezdu do dalsi zastavky
     *
     * @param time udaj s casem odjezdu busu ze zastavky
     */
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

    /**
     * Metoda spocita vola dalsi metody pro vypocet kroku
     *
     */
    private void nextPos(){
        if( now != plannedStops.size() - 1 ) {
            int correction = 0;
            if( now == -1 ) {
                correction = 1;
            }

            timeFromStop++;

            if( timerTime == countDeparture( plannedStops.get( 0 ).get( 1 ) ) ){
                round = false;
            }
            if ( round || countDeparture(plannedStops.get(now + correction).get(1)) + Delay > timerTime && stationary ) {
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
            if( current.color == 3 ){
                Delay = Delay + 15;
                currDelay = currDelay + 15;
            } else if( current.color == 2 ){
                Delay = Delay + 5;
                currDelay = currDelay + 5;
            } else {
                currDelay = currDelay + 0;
                Delay = Delay + 0;
            }
            current.changeable = false;
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

    /**
     * Metoda nacte ulice
     *
     * @param str hash s informacemi o ulicich
     */
    void getStreets(HashMap<String, Street> str){
        this.streets = str;
    }

    /**
     * Metoda nazve autobus
     *
     * @param str nazev busu
     */
    void nameIt(String str){
        this.name = str;
    }

    /**
     * Metoda ziska zastavky
     */
    void getStops(){
        for(int i = 0; i < route.size(); ++i){
            if(route.get(i).getStop() != null){
                stopsOnRoute.add(route.get(i).getStop().getId());
            }
        }

        posX = route.get( 0 ).getMiddle().getX();
        posY = route.get( 0 ).getMiddle().getY();

        bus = new Circle( 0, 0, 3, Color.RED );
        onBusClick();

        // -1 protoze zastavka na konci je stejna jako na zacatku kvuli dojeti na zastavku
        for( int i = currentStops; i < stopsOnRoute.size() - 1; i++ ){
            if( !stopsOnRoute.get(i).equals(plannedStops.get(i).get(0))){
                System.out.println("Error: Zastavky na ceste se neshoduji se zadanymi");
                exit(60);
            }
        }
    }

    /**
     * Metoda pri kliknuti na bus da prikaz k vykresleni trasy busu
     */
    private void onBusClick(){
        bus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                busClicked = true;
                busLink = new ArrayList<>();
                busLink.add(new Line(310, 100, 940, 100));
                int cnt = 0;
                //zastavky na line ne scene2
                for (int i = 310; i <= 940; i += 70){
                    busLink.add(new Circle(i, 100, 5, Color.BLUE));
                    busLink.add(new Text(i-20, 130, plannedStops.get(cnt).get(0)));
                    busLink.add(new Text(i-20, 80, plannedStops.get(cnt++).get(1)));
                }
                //autobus na linke na scene2
                if(stationary){//je na zastavke
                    busLink.add(new Circle(310+70*now, 100, 5, Color.RED));
                }
                else{
                    busLink.add(new Circle(310+70*now+35, 100, 5, Color.RED));
                }
                //zvyraznenie cesty na mape
//                for(Street str : route){
                for(int i = 0; i < route.size(); ++i){
                    route.get(i).getGUI().setStroke(Color.BLUE);
                    route.get(i).getGUI().setStrokeWidth(2);
                    blueRoute.add(route.get(i));
                }
            }
        });
    }

    /**
     * Metoda vycisti barvy na puvodni
     */
    public static void clearPicked(){
        if(!busClicked){
            for(Street str : blueRoute){
                str.getGUI().setStroke(Color.rgb(99, 214, 104));
                str.getGUI().setStrokeWidth(1.5);
            }
        }
        busClicked = false;
    }


    /**
     * Metoda nastavi cas startu
     * @param time
     */
    void setStart(String time){
        this.start = time;
    }

    /**
     * Metoda nastavi cestu
     * @param streetId
     */
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