/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

/**
 * Trida drzi informace o ulicich
 * @file Street.java.
 * @author Jakub Sekula (xsekul01)
 * @author Ondrej Potúček (xpotuc06)
 *
 */

package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Street implements Drawable {

    /**
     * grafika ulice
     */
    private Shape street;

    String id, name;

    /**
     * stred ulice
     */
    Coordinate middle;

    /**
     * delka ulice
     */
    float streetLength = 0;

    /**
     * zastavka
     */
    Stop stop = null;

    /**
     * koordinaty ulice
     */
    List<Coordinate> coords = new ArrayList<Coordinate>();

    /**
     * barva ulice, stupen provozu
     */
    public int color = 1;   //1-green, 2-orange, 3-red

    /**
     * provoz neni mozne menit, jestli je na ni bus
     */
    public boolean changeable = true;
    /**
     * hash objizdnych tras
     */
    public static HashMap<String, List<Street>> Detour = new HashMap<>();
    /**
     * aktualni hash key
     */
    public static String usingKey = null;
    /**
     * jestli je mozne menit ulice
     */
    public static boolean changingLink = false;
    /**
     * list objizdek
     */
    public static List<Street> alternateRoute = new ArrayList<>(); //!< Vektor ciest v obchadzke, prva cesta je uzavreta a ostatne su obchadzka


    private Color green = Color.rgb(99, 214, 104);
    private Color orange = Color.rgb(255,151,77);
    private Color red = Color.rgb(242,60,50);
    private  Color blue = Color.rgb(0,170,240);
    private  Color grey = Color.rgb(170,170,170);

    /**
     * Konstruktor tridy
     * @param str identifikator
     * @param name nazev ulice
     * @param c0 koordinat
     * @param c1 koordinat
     */
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
            /**
             * Hnadler kliku
             * @param event mouse klik
             */
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

    /**
     * Metoda vycisti objizdnou trasu
     */
    public static void clearAtlernateRoute(){
        while(alternateRoute.size() != 0){
            alternateRoute.get(0).setBackColor();
        }
    }

    /**
     * Metoda prida ulici, pokud je v poradku, do objizdky
     */
    private void closeStreet() {
        if(changingLink){
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

            if( alternateRoute.size() > 0){
                //ak tato ulica navazuje na poslednu pridanu do obchadzky
                if(alternateRoute.get(alternateRoute.size()-1).equals(this)){
                    alternateRoute.add(this);
                }
                //ulice nenavazuju - chyba
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Cesty na sebe nenavazuji!");
                    alert.showAndWait();
                }
            }
            else{
                alternateRoute.add(this);
            }

        } else {
            System.out.println( "HEER" );
        }
    }

    /**
     * nastavi barvu ulice podle provozu
     */
    private void setBackColor() {
        alternateRoute.remove(this);
        if(this.color == 1) this.street.setStroke(green);
        else if(this.color == 2) this.street.setStroke(orange);
        else if(this.color == 3) this.street.setStroke(red);
    }

    /**
     * Metoda pocita delku ulice
     */
    private void countStreetLength(){
        float XDiff = Math.abs( coords.get( 0 ).getX() - coords.get( 1 ).getX() );
        float YDiff = Math.abs( coords.get( 0 ).getY() - coords.get( 1 ).getY() );

        float hypotenuse = ( float ) Math.sqrt( Math.pow( XDiff, 2 ) + Math.pow( YDiff, 2 ));

        streetLength = hypotenuse;

    }

    /**
     * Metoda vraci identifikator ulice
     * @return identifikator
     */
    public String getId(){
        return id;
    }

    /**
     * Vraci zastavku
     * @return vraci null nebo zastavku
     */
    Stop getStop(){
        if( this.stop != null ){
            return stop;
        } else {
            return null;
        }
    }

    /**
     * Prida zastavku na ulici
     * @param stop zastavka
     * @return bool
     */
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

    /**
     * Metoda vraci zacatek ulice
     * @return koordinat
     */
    public Coordinate begin(){
        return coords.get(0);
    }

    /**
     * Metoda vraci konec ulice
     * @return koordinat
     */
    public Coordinate end(){
        return coords.get(coords.size()-1);
    }

    /**
     * Metoda pocita stred ulice
     * @param c0 koordinat
     * @param c1 koordinat
     */
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

    /**
     * Vraci souradnice stredu
     * @return koordinat
     */
    public Coordinate getMiddle(){
        countMiddle(this.begin(), this.end());
        return middle;
    }

    /**
     * Vraci zacatek ulice
     * @return koordinat
     */
    public Coordinate getStreetStart(){
        return coords.get(0);
    }

    /**
     * Vraci konec ulice
     * @return koordinat
     */
    public Coordinate getStreetEnd(){
        return coords.get(coords.size()-1);
    }

    /**
     * Metoda porovnava ulice
     * @param street ulice
     * @return bool
     */
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

    /**
     * Metoda zjisti jestli se dalsi ulice napojuje pres jeji konec nebo zacatek
     * @param s2 ulice
     * @return bool
     */
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

    /**
     * Vraci gui objekt
     * @return vraci gui objekt
     */
    @Override
    public Shape getGUI() {
        return street;
    }
}

