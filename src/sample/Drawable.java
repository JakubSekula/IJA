/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

/**
 * Interface pro graficke elementy
 * @file Drawable.java.
 * @author Jakub Sekula (xsekul01)
 * @author Ondrej Potúček (xpotuc06)
 *
 */

package sample;

import javafx.scene.shape.Shape;

import java.util.List;

/**
 * Interface pre objekty ktoré je možné vykresliť.
 */
public interface Drawable {
    /**
     * Funkcia na vrátenie objektu typu Shape na vykreslenie.
     * @return Objekt typu Shape
     */
    Shape getGUI();
}

