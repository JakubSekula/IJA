package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class ReadCSV {
    HashMap<String, Street> mapHash = new HashMap<String, Street>();
    HashMap<String, Stop> mapStopsHash = new HashMap<String, Stop>();

//    HashMap<String, Bus> busHash = new HashMap<String, Street>();
//    HashMap<String, Line> lineHash = new HashMap<String, Street>();

    public ReadCSV(String file, String fileType){
        if( fileType == "Map" ){
            loadMap( file );
//        } else if ( fileType == "Bus" ){
//            loadBus( file );
//        } else if ( fileType == "Line" ){
//            loadLine( file );
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
            System.out.println("Chyba pri otvarani suboru.");
            e.printStackTrace();
        } catch (NumberFormatException e){
            System.out.println("Zle suradnice v subore.");
            e.printStackTrace();
        }
    }

    HashMap<String, Street> getMapHash(){
        return mapHash;
    }

    HashMap<String, Stop> getStopsHash(){
        return mapStopsHash;
    }
}
