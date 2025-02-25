/******************************************************************************
 * Projekt: Aplikace zobrazující autobusovou dopravu                          *
 * Předmet: Seminář Java - FIT VUT v Brně                                     *
 * Rok:     2019/2020                                                         *
 * Autoři:                                                                    *
 *          Jakub Sekula (xsekul01) - xsekul00@stud.fit.vutbr.cz              *
 *          Ondrej Potúček (xpotuc06) - xpotuc06@stud.fit.vutbr.cz            *
 ******************************************************************************/

/**
 * Trida cte vstupni informace a dava je do hashu
 * @file ReadCSV.java.
 * @author Jakub Sekula (xsekul01)
 * @author Ondrej Potúček (xpotuc06)
 *
 */

package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Trieda pre načítanie informácií z .csv súborov
 */
public class ReadCSV {
    /**
     * HashMap ulic
     */
    private HashMap<String, Street> mapHash = new HashMap<>();
    /**
     * HashMap zastavok
     */
    private HashMap<String, Stop> mapStopsHash = new HashMap<>();
    /**
     * HashMap liniek
     */
    private HashMap<String, Line> mapLineHash = new HashMap<>();
    /**
     * HashMap autobusov
     */
    private HashMap<String, HashMap<String, Bus>> mapBusHash = new HashMap<>();

    /**
     * Konstruktor podla typu suboru nacita mapu, autobusy alebo linky.
     * @param file Názov súboru
     * @param fileType Typ súboru
     * @param streets Cesty na mape
     * @param lines Linky
     */
    public ReadCSV(String file, String fileType, HashMap<String, Street> streets, HashMap<String, Line> lines){
        switch (fileType) {
            case "Map":
                loadMap(file);
                break;
            case "Bus":
                loadBus(file, streets, lines);
                break;
            case "Line":
                loadLine(file, streets);
                break;
            default:
                System.exit(32);
        }
    }

    /**
     * Metóda pre načítanie mapy.
     * @param file Názov súboru
     */
    private void loadMap(String file){
        try {
            Scanner reader = new Scanner(new File(file));
            reader.nextLine();  //preskocenie prveho riadku
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] row = line.split(",");

                Coordinate c1 = new Coordinate(Integer.parseInt(row[2])+400, Integer.parseInt(row[3])+400);
                Coordinate c2 = new Coordinate(Integer.parseInt(row[4])+400, Integer.parseInt(row[5])+400);

                Street street = new Street(row[0], row[1], c1, c2);

                if(row[6].equals("YES")){
                    Stop stop = new Stop(row[0]);
                    stop.position = street.getMiddle();
                    street.addStop(stop);
                    mapStopsHash.put(row[0], stop);
                }

                mapHash.put(row[0], street);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otvarani suboru newyork.csv.");
            e.printStackTrace();
        } catch (NumberFormatException e){
            System.out.println("Zle suradnice v subore.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Chyba pri nacitavani mapy");
            e.printStackTrace();
        }
    }

    /**
     * Metóda na načítanie liniek.
     * @param file Názov súboru
     * @param streets Cesty
     */
    private void loadLine(String file, HashMap<String, Street> streets){
        try{
            Scanner reader = new Scanner(new File(file));
            reader.nextLine();  //preskocenie prveho riadku

            while (reader.hasNextLine()){
                String line = reader.nextLine();
                String[] row = line.split(",");
                if(row[0].equals("")) {
                    break;
                }

                Line link = new Line(row[0]);
                link.setReps(row[1]);
                int cntStop = 0;
                List<List<String>> entire = new ArrayList<>();

                int cnt = 0;
                for(int i = 2; i < row.length; ++i){
                    String[] separate = row[i].split("/");
                    if(separate.length != 2){
                        System.exit(56);
                    }
                    entire.add(new ArrayList<>());
                    entire.get(cnt).add(separate[0]);
                    if(!streets.containsKey(separate[0])){
                        System.exit(56);
                    }
                    entire.get(cnt).add(separate[1]);
                    link.stoptime.add(entire.get(cnt));
                    cnt++;
                }
                entire.add(entire.get(0));
                link.stoptime.get(cntStop).add(entire.get(0).get(0));
                link.stoptime.get(cntStop).add("99:99");
                link.fillMap(row[0], entire);
                mapLineHash.put(row[0], link);
                cntStop++;
            }



        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otvarani suboru link.csv.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Chyba pri nacitavani liniek");
            e.printStackTrace();
        }
    }

    /**
     * Metóda pre načítanie autobusov.
     * @param file Názov súboru
     * @param streets Cesty
     * @param lines Linky
     */
    private void loadBus(String file, HashMap<String, Street> streets, HashMap<String, Line> lines){
        try{
            Scanner reader = new Scanner(new File(file));
            reader.nextLine();  //preskocenie prveho riadku

            while (reader.hasNextLine()){
                String line = reader.nextLine();
                String[] row = line.split(",");
                if(row[0].equals("")) {
                    break;
                }

                int iter = 0;
                this.mapBusHash.put(row[0], new HashMap<>());
                while(iter < lines.get(row[0]).reps){
                    List<String> times = new ArrayList<>();
                    List<List<String>> test = lines.get(row[0]).getStopInfo(row[0]);

                    times.add(getTimeDiff(test.get(0).get(1), lines.get(row[0]).reps, iter));

                    if(times.get(0).length() == 1){
                        times.set(0, "0"+times.get(0)+" : "+test.get(0).get(1).substring(test.get(0).get(1).length() - 2));
                    }

                    if(times.get(0).length() == 2){
                        times.set(0, times.get(0)+" : "+test.get(0).get(1).substring(test.get(0).get(1).length() - 2));
                    }

                    Bus bus = new Bus();
                    bus.getStreets(streets);
                    bus.nameIt(row[0]);

                    for(int i = 1; i < row.length; ++i){
                        if(!streets.containsKey(row[i])){
                            System.exit(56);
                        }
                        bus.setRout(row[i]);
                    }
                    int cnt = 0;
                    for(int i = 0; i < lines.get(row[0]).stoptime.size(); ++i){
                        bus.plannedStops.add(new ArrayList<>());

                        if(i < lines.get(row[0]).stoptime.size()){
                            bus.plannedStops.get(cnt).add(lines.get(row[0]).stoptime.get(i).get(0));
                            bus.plannedStops.get(cnt).add(getTimeDiff(lines.get(row[0]).stoptime.get(i).get(1), lines.get(row[0]).reps, iter));
                        }
                        else {
                            bus.plannedStops.get(cnt).add(lines.get(row[0]).stoptime.get(i).get(0));
                            bus.plannedStops.get(cnt).add(lines.get(row[0]).stoptime.get(i).get(1));
                        }
                        cnt++;
                    }

                    List<String> addLast = new ArrayList<>();

                    addLast.add( 0, bus.plannedStops.get( bus.plannedStops.size() - 1 ).get( 0 ) );
                    String time = bus.plannedStops.get( bus.plannedStops.size() - 1 ).get( 1 );

                    String[] parse = time.split( ":" );
                    int minutes = Integer.parseInt( parse[ 0 ] );
                    minutes = minutes + 2;

                    String realtime = String.valueOf( minutes );

                    if( realtime.length() == 1 ){
                        realtime = "0" + realtime + ":00";
                    } else {
                        realtime = realtime + ":00";
                    }

                    addLast.add( 1, realtime );

                    bus.plannedStops.add( addLast );

                    int realstart;

                    realstart = lines.get(row[0]).reps + lines.get(row[0]).reps * iter;

                    String real = String.valueOf(realstart);
                    if(real.length() == 1){
                        real = "0" + real;
                    }
                    bus.setStart(times.get(0));
                    bus.getStops();
//                    busHash.put(String.valueOf(iter), bus);
                    this.mapBusHash.get(row[0]).put(String.valueOf(iter), bus);
                    iter++;
                }//while(iter < lines.get(row[0]).reps)
//                this.mapBusHash.put(row[0], busHash);
//                busHash.clear();
            }//while (reader.hasNextLine())

        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otvarani suboru Bus.csv.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Chyba pri nacitavani autobusov");
            e.printStackTrace();
        }
    }

    /**
     * Metóda na vypočítanie odchodu autobusu.
     * @param time Čas
     * @param reps Počet liniek za hodinu
     * @param iter Poradove cislo autobusu
     * @return Odchod autobusu
     */
    private String getTimeDiff(String time, int reps, int iter){
        if( time.length() != 5 ){
            System.exit( 50 );
        }
        int minutes = Integer.parseInt(time.substring(0,2));
        int another_departure = 60 /reps;
        String departure = (another_departure * iter + minutes) + ":"+ time.substring(time.length() - 2);
        if( departure.length() == 4 ){
            departure = "0" + departure;
        }
        if(Integer.parseInt(departure.substring(0,2)) >= 60){
            departure = String.valueOf(Integer.parseInt(departure.substring(0,2)) - 60) + ":"+time.substring(time.length()-2);
            if(departure.length() == 4){
                departure = "0"+departure;
            }
        }
        return departure;
    }

    /**
     * Metóda na vrátenie HashMap ulíc.
     * @return HashMap ulíc
     */
    public HashMap<String, Street> getMapHash(){
        return mapHash;
    }

    /**
     * Metóda na vrátenie HashMap zastávok.
     * @return HashMap zastávok
     */
    public HashMap<String, Stop> getStopsHash(){
        return mapStopsHash;
    }

    /**
     * Metóda na vrátenie HashMap liniek.
     * @return HashMap liniek
     */
    public HashMap<String, Line> getLineHash(){
        return mapLineHash;
    }

    /**
     * Metóda na vrátenie HashMap autobusov.
     * @return HashMap autobusov
     */
    public HashMap<String, HashMap<String, Bus>> getBusHash(){
        return mapBusHash;
    }
}

