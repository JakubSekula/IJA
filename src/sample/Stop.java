/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

/**
 * Trida drzici informace o zastavkach
 * @file Stop.java.
 * @author Jakub Sekula (xsekul01)
 * @author Ondrej Potúček (xpotuc06)
 *
 */

package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Stop implements Drawable{
    String id;
    Coordinate position;
    Street street;

    /**
     * @param str id zastavky
     */
    public Stop(String str){
        id = str;
    }

    /**
     * @return identifikator zastavky
     */
    public String getId() {
        return id;
    }

    /**
     * @param s id ulice
     */
    public void setStreet(Street s) {
        street = s;
    }

    /**
     * @param o zvoleny objekt
     * @return
     */
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass())
            return false;
        else{
            return (((Stop) o).getId().equals(this.getId()));
        }
    }


    /**
     * @return objekt tvar
     */
    @Override
    public Shape getGUI() {
        return new Circle(position.getX(), position.getY(), 4, Color.BLUE);
    }
}

