import org.junit.Test;
import publicTransportRouting.model.Location;
import publicTransportRouting.service.PT_Facade_Class;

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
public class FacadeTest {
    //---------------------------------------- Main Methode -----------------------------------------//

    @Test
    public void performCompleteTest() {
        downloadFiles();
        createGraph();
    }
    //----------------------------------------- Constructor -----------------------------------------//


    @Test
    public void downloadFiles() {
        String osmFile_URL = "https://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf";
        String gtfsFile_URL = "https://www.vbb.de/media/download/2029/GTFS.zip";

        //Download Files from an URL link (OSM and GTFS Files) if non is available locally
        //(Folder to save: resources/OSM_Files and resources/GTFS_Files)
        //Example given is for Berlin !!!
        PT_Facade_Class.downloadFiles(osmFile_URL, gtfsFile_URL);

    }

    @Test
    public void createGraph() {
        String osmFile = "berlin-latest.osm.pbf";
        String gtfsFile = "gtfs.zip";


        //Creating a Graph if this exact Graph isn´t already build
        //(Folder to save: resources/graph/ Name of the Osm File + _with_Transit)
        //Example given is for Berlin with the associated Gtfs File
        PT_Facade_Class.createGraph(osmFile, gtfsFile);
    }

    @Test
    public void loadGraph() {
        String graphFolderName = "berlin-latest.osm.pbf_with_Transit";

        //loading an existing Graph from an folder
        //(Folder where the graphs are saved: resources/graph)
        PT_Facade_Class pt = new PT_Facade_Class("Europe/Paris", 2020, 8, 6, 10, 0, "JSON");
        pt.loadGraph(graphFolderName);
    }

    @Test
    public void ptRouteQuery() {
        Location from = new Location(52.456428, 13.139477);
        Location to = new Location(52.503893, 13.502197);
        LocalDateTime queryTime = LocalDateTime.of(2020, 8, 6, 11, 34);
        String routeSelection = "all"; //alternatives: best

        //Creating a Query on a loaded Graph
        //Saves returning Routes in resources/Routes
        PT_Facade_Class pt = new PT_Facade_Class("Europe/Paris", 2020, 8, 6, 10, 0, "JSON");
        pt.loadGraph("berlin-latest.osm.pbf_with_Transit");
        pt.ptRouteQuery(from, to, queryTime, routeSelection);

    }


    @Test
    public void loadRoute() {
        /*
    Loading an existing Route
    On the loaded Route is it possible to make some test or queries
    default it prints the criteria of the routes to the console
    can be edited by change the methode test() in RouteLoader
     */
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


