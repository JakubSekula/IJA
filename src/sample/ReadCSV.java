package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ReadCSV {
    HashMap<String, Street> mapHash = new HashMap<String, Street>();
    HashMap<String, Stop> mapStopsHash = new HashMap<String, Stop>();
    HashMap<String, Line> mapLineHash = new HashMap<String, Line>();
    HashMap<String, Bus> mapBusHash = new HashMap<String, Bus>();

//    HashMap<String, Bus> busHash = new HashMap<String, Street>();
//    HashMap<String, Line> lineHash = new HashMap<String, Street>();

    public ReadCSV(String file, String fileType, HashMap<String, Street> streets, HashMap<String, Line> lines){
        if( fileType.equals("Map") ){
            loadMap( file );
        } else if ( fileType.equals("Bus") ){
            loadBus( file, streets, lines);
        } else if ( fileType.equals("Line") ){
            loadLine( file, streets);
        }else {
            System.exit( 32 );
        }
    }

    private void loadMap(String file){
        try {
            Scanner reader = new Scanner(new File(file));
            reader.nextLine();  //preskocenie prveho riadku
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] row = line.split(",");

                Coordinate c1 = new Coordinate(Integer.parseInt(row[2]), Integer.parseInt(row[3]));
                Coordinate c2 = new Coordinate(Integer.parseInt(row[4]), Integer.parseInt(row[5]));

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
//                HashMap<Integer, Line> instances = new HashMap<Integer, Line>();
                Line link = new Line(row[0]);
                link.setReps(row[1]);

                List<String> stoptime = new ArrayList<String>();
                List<List<String>> entire = new ArrayList<List<String>>();

                for(int i = 2; i < row.length; ++i){
                    String separate[] = row[i].split("/");
                    if(separate.length != 2){
                        System.exit(56);
                    }

                    stoptime.add(separate[0]);
                    if(streets.containsKey(separate[0])){
                        System.exit(56);
                    }
                    stoptime.add(separate[1]);
                    link.stoptime.add(stoptime);
                    entire.add(stoptime);
                    stoptime.clear();
                }
                entire.add(entire.get(0));
                stoptime.add(entire.get(0).get(0));
                stoptime.add("99:99");
                link.stoptime.add(stoptime);
                stoptime.clear();
                link.fillMap(row[0], entire);
                mapLineHash.put(row[0], link);
            }



        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otvarani suboru link.csv.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Chyba pri nacitavani liniek");
            e.printStackTrace();
        }
    }

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
                while(iter < lines.get(row[0]).reps){
                    List<String> times = new ArrayList<String>();
                    List<List<String>> test = lines.get(row[0]).getStopInfo(row[0]);

//                    times.add(getTimeDiff(test.get(0).get(1), lines.get(row[0]).reps, iter));

//                    if(times[0])
                }//while(iter < lines.get(row[0]).reps)
            }//while (reader.hasNextLine())

        } catch (FileNotFoundException e) {
            System.out.println("Chyba pri otvarani suboru Bus.csv.");
            e.printStackTrace();
        } catch (Exception e){
            System.out.println("Chyba pri nacitavani autobusov");
            e.printStackTrace();
        }
    }

    HashMap<String, Street> getMapHash(){
        return mapHash;
    }

    HashMap<String, Stop> getStopsHash(){
        return mapStopsHash;
    }

    HashMap<String, Line> getLineHash(){
        return mapLineHash;
    }

    HashMap<String, Bus> getBusHash(){
        return mapBusHash;
    }
}
