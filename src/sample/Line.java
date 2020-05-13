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
    String id;
    List<Street> streets = new ArrayList<Street>();
    HashMap<String, List<List<String>>> stoptimes = new HashMap<String, List<List<String>>>();
    List<List<String>> stoptime = new ArrayList<List<String>>();
    int reps;

    public Line(String str){
        id = str;
    }

    public void setReps(String repeats){
        this.reps = Integer.parseInt(repeats);
    }

    public void fillMap(String id, List<List<String>> stoptime){
        this.stoptimes.put(id, stoptime);
    }

    public List<List<String>> getStopInfo(String id){
        return this.stoptimes.get(id);
    }

}
