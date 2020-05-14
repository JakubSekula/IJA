/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

/**
 * Trida drzi informace o cestach
 * @file Line.java.
 * @author Jakub Sekula (xsekul01)
 * @author Ondrej Potúček (xpotuc06)
 *
 */

package sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Line {

    /**
     * indetifikator
     */
    String id;

    /**
     * hash zastavek
     */
    HashMap<String, List<List<String>>> stoptimes = new HashMap<String, List<List<String>>>();

    /**
     * list zastavky
     */
    List<List<String>> stoptime = new ArrayList<List<String>>();

    /**
     * pocet instanci kolik se ma vytvorit pro linku
     */
    int reps;

    /**
     * Konstruktor tridy, priradi id
     * @param str identifikator
     */
    public Line(String str){
        id = str;
    }

    /**
     * Metoda podle parametru nainstancuje busy
     * @param repeats pocet instanci bus
     */
    public void setReps(String repeats){
        this.reps = Integer.parseInt(repeats);
    }

    /**
     * Metoda naplni List zastavkami
     * @param id identifikator
     * @param stoptime List zastavek a casu
     */
    public void fillMap(String id, List<List<String>> stoptime){
        this.stoptimes.put(id, stoptime);
    }

    /**
     * Metoda vrati informace o zastavkach
     * @param id identifikator
     * @return vraci List zastavek
     */
    public List<List<String>> getStopInfo(String id){
        return this.stoptimes.get(id);
    }

}
