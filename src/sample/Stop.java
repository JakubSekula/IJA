/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Stop implements Drawable{
    String id;
    Coordinate position;
    Street street;
//    Shape stop;

    public Stop(String str){
        id = str;
    }

    public String getId() {
        return id;
    }

    public Coordinate getCoordinate() {
        return position;
    }

    public void setStreet(Street s) {
        street = s;
    }

    public Street getStreet() {
        return street;
    }

    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass())
            return false;
        else{
            return (((Stop) o).getId().equals(this.getId()));
        }
    }


    @Override
    public Shape getGUI() {
        return new Circle(position.getX(), position.getY(), 4, Color.BLUE);
    }
}
