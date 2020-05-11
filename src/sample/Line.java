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
