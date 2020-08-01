package PublicTransportRouting;

import PublicTransportRouting.Test.Route_Loader;
import PublicTransportRouting.supClass.FileDownloader;
import PublicTransportRouting.supClass.Location;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;

public class TestMain {
    public static void main(String[] args) {
        TestMain lul = new TestMain();
        lul.machwas();
//        lul.loadTest();
    }

    public void machwas(){

        //Dateien herunterladen, welche zum erstellen vom Graph gebruacht wird (Beispiel ist hier Berlin)
        PT_Facade_Class.downloadFiles("https://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf",
                                       "https://www.vbb.de/media/download/2029/gtfs-vbb.zip");

        //Einen nicht vorhandenen Graph erstellen
        PT_Facade_Class.createGraph("berlin-latest.osm.pbf","gtfs-vbb.zip");

        //Einen bereits vorhandenen Graph laden
        PT_Facade_Class pt = new PT_Facade_Class("Europe/Paris",2020,7,31,13,00,"JSON");
        pt.loadGraph("berlin-latest.osm.pbf_with_Transit");

        //Stelle eine Abfrage um eine Route zu erhalten
        pt.ptRouteQuery(new Location(52.640251,13.207669),new Location(52.518036,13.431559),
                                         LocalDateTime.of(2020,7,31,13,00),"all");

    }

    public void loadTest(){
        String FilePath;
        FilePath = "src\\main\\resources\\Routes\\RoutenListe.json";
        Route_Loader loader = new Route_Loader();

        try {
            loader.loadRouteFromFile(FilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loader.test();
    }
}


//------------------------------------------ Variable -------------------------------------------//
//----------------------------------------- Constructor -----------------------------------------//
//------------------------------------------- Methods -------------------------------------------//
//--------------------------------------- Getter & Setter ---------------------------------------//
//----------------------------------------- Additional ------------------------------------------//
