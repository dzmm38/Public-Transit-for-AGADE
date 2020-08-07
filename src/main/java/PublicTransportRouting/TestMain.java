package PublicTransportRouting;

import PublicTransportRouting.Routemodel.Location;
import PublicTransportRouting.Routemodel.PT_Route;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Example Class to how to handle the module
 * - has examples for ever funktion of the Facade Class
 * - creating Graph
 * - loading Graph
 * - creating an Query
 * - loading a Route from a file
 * <p>
 * below are some URL for testing (at end of the class)
 */
public class TestMain {
    //---------------------------------------- Main Methode -----------------------------------------//
    public static void main(String[] args) {
        TestMain testMain = new TestMain();
        testMain.doAll();
        testMain.loadRoute();
    }
    //----------------------------------------- Constructor -----------------------------------------//

    public void doAll() {
        //Download Files from an URL link (OSM and GTFS Files) if non is available locally
        //(Folder to save: resources/OSM_Files and resources/GTFS_Files)
        //Example given is for Berlin !!!
        PT_Facade_Class.downloadFiles("https://download.geofabrik.de/europe/germany/nordrhein-westfalen-latest.osm.pbf",
                "http://download.vrsinfo.de/gtfs/GTFS_VRS_mit_SPNV.zip");

        //Creating a Graph if this exact Graph isn´t already build
        //(Folder to save: resources/graph/ Name of the Osm File + _with_Transit)
        //Example given is for Berlin with the associated Gtfs File
        PT_Facade_Class.createGraph("nordrhein-westfalen-latest.osm.pbf", "GTFS_VRS_mit_SPNV.zip");

        //loading an existing Graph from an folder
        //(Folder where the graphs are saved: resources/graph)
        PT_Facade_Class pt = new PT_Facade_Class("Europe/Paris", 2020, 7, 31, 13, 0, "JSON");
        pt.loadGraph("nordrhein-westfalen-latest.osm.pbf_with_Transit");

        //Creating a Query on a loaded Graph
        //Saves returning Routes in resources/Routes
        pt.ptRouteQuery(new Location(50.963189, 6.985617), new Location(50.943347, 6.982131),
                LocalDateTime.of(2020, 7, 31, 13, 0), "all");

        //Optional second or Xth Query
        pt.ptRouteQuery(new Location(50.962427, 6.980996), new Location(50.941313, 6.958251),
                LocalDateTime.of(2020, 7, 31, 14, 0), "all");

    }

    /*
    Loading an existing Route
    On the loaded Route is it possible to make some test or queries
    default it prints the criteria of the routes to the console
    can be edited by change the methode test() in RouteLoader
     */
    public void loadRoute() {
        PT_Facade_Class.loadRoute("src\\main\\resources\\Routes\\Route_52.456672,13.140033 -- 52.503737,13.502879.json");
    }
    //--------------------------------------- Getter & Setter ---------------------------------------//
    //----------------------------------------- Additional ------------------------------------------//
}
    /*
    -----------------------------------------------------------------------------------------------------------
    TEST FILES:
    OSM download from --> https://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf                            --> Berlin
                          https://download.geofabrik.de/europe/germany/nordrhein-westfalen-latest.osm.pbf               --> NRW

    GTFS download from -->https://www.vbb.de/media/download/2029/GTFS.zip                                               --> Berlin
                          http://download.vrsinfo.de/gtfs/GTFS_VRS_mit_SPNV.zip                                         --> NRW
     -----------------------------------------------------------------------------------------------------------
     */


