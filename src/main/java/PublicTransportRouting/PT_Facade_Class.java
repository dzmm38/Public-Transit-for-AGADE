package PublicTransportRouting;

import PublicTransportRouting.PT_Graph.PT_Graph;
import PublicTransportRouting.Routing.PT_Query;
import PublicTransportRouting.Routing.PT_Route_Finder;
import PublicTransportRouting.Time.TimeCalculator;
import PublicTransportRouting.supClass.FileDownloader;
import PublicTransportRouting.supClass.Location;
import com.graphhopper.reader.gtfs.PtRouteResource;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Top Class (Facade Class)
 * Contains all necessary Methods to use the "API"
 *
 * Possible:    to create a graph which is then saved in resources/graph (static Method)
 *              to load a existing graph form a graph folder in resources/graph
 *              to make queries from a location to another location at a specific time
 */
public class PT_Facade_Class {
    //------------------------------------------ Variable -------------------------------------------//
    private ZoneId zoneId;                      //zone for which routing should be done for Example "Europe/Paris"
    private LocalDateTime dateTime;             //simulation start time (or time which should be simulated
    private PtRouteResource PT;                 //Object on with routing is done initialized by calling load graph
    private String fileFormat;                  //fileFormat in which the route should be saved ("JSON" or "YAML") default = JSON

    //----------------------------------------- Constructor -----------------------------------------//
    public PT_Facade_Class(String zoneId,int year,int month,int day,int hour,int min,String fileFormat){
        this.zoneId = ZoneId.of(zoneId);
        this.dateTime = LocalDateTime.of(year,month,day,hour,min);
        this.fileFormat = fileFormat;
        new TimeCalculator().calculateTickZero(dateTime.toLocalTime());
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Static Method on PT_Graph to create a Graph with passed File names,
     * which is saved in resources/graph
     *
     * @param OsmFile Name of the file for the map, street and walk network
     *                 (have to be located in OSM_Files folder in resources)
     * @param GtfsFile Name of the file which represents the public transit aspect
     *                 (have to be located in the GTFS_Files folder in resources)
     */
    public static void createGraph(String OsmFile, String GtfsFile){
        PT_Graph.createGraph(OsmFile,GtfsFile);
    }

    /**
     * Method to load an existing graph in resources/graph and sets it to the PTRouteResource
     *
     * @param graphFolderName Name of the folder in which the graph is located
     *                       (have to be located in the graphs folder in resources)
     */
    public void loadGraph(String graphFolderName){
        PT_Graph graph = new PT_Graph();
        PT = graph.loadGraph(graphFolderName);
    }

    /**
     * Method to create a new Query and then finds a route, builds it and then saves the route as a JSON file
     *
     * @param from location from where the query should start
     * @param to location to where the query should end
     * @param queryTime time at which the query is requestet
     */
    public void ptRouteQuery(Location from, Location to, LocalDateTime queryTime,String routeSelection){
        System.out.println("Constructing Query ..........");
        PT_Query abfrage = new PT_Query(from,to,queryTime,zoneId);

        PT_Route_Finder finder = new PT_Route_Finder(zoneId, queryTime);
        finder.findRoute(abfrage,PT,routeSelection,fileFormat);

    }

    /**
     * Method to download Osm and Gtfs files for an zone
     * saves them in resources/  OSM_Files and GTFS_Files
     *
     * @param OsmFile_URL the URL of an Osm file to download
     * @param GtfsFile_URL the URL of an Gtfs file to download
     */
    public static void downloadFiles(String OsmFile_URL,String GtfsFile_URL){
        try {
            FileDownloader.downloadOsmFile(OsmFile_URL);
            FileDownloader.downloadGtfsFile(GtfsFile_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public ZoneId getZoneId() {
        return zoneId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public PtRouteResource getPT() {
        return PT;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
