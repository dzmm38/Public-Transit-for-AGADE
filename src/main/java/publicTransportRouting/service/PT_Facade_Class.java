package publicTransportRouting.service;

import com.graphhopper.reader.gtfs.PtRouteResource;
import publicTransportRouting.controller.*;
import publicTransportRouting.model.Location;

import java.io.IOException;
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
        new TimeController().calculateTickZero(dateTime.toLocalTime());     //TickZero Variable is static so the same over all calculator classes
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Static Method on GtfsGraphController to create a Graph with passed File names,
     * which is saved in resources/graph
     *
     * @param OsmFile Name of the file for the map, street and walk network
     *                 (have to be located in OSM_Files folder in resources)
     * @param GtfsFile Name of the file which represents the public transit aspect
     *                 (have to be located in the GTFS_Files folder in resources)
     */
    public static void createGraph(String OsmFile, String GtfsFile){
        GtfsGraphController.createGraph(OsmFile, GtfsFile);
    }

    /**
     * Method to load an existing graph in resources/graph and sets it to the PTRouteResource
     *
     * @param graphFolderName Name of the folder in which the graph is located
     *                       (have to be located in the graphs folder in resources)
     */
    public void loadGraph(String graphFolderName){
        GtfsGraphController graph = new GtfsGraphController();
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

        GraphhopperController graphhopperController = new GraphhopperController();
        graphhopperController.createRequest(from, to, queryTime, zoneId);
        graphhopperController.setZoneId(zoneId);
        graphhopperController.setDateTime(queryTime);

        graphhopperController.findRoute(graphhopperController.getRequest(), PT, routeSelection, fileFormat);

    }

    /**
     * Method to download Osm and Gtfs files for an zone
     * saves them in resources/  OSM_Files and GTFS_Files
     *
     * @param OsmFile_URL  the URL of an Osm file to download
     * @param GtfsFile_URL the URL of an Gtfs file to download
     */
    public static void downloadFiles(String OsmFile_URL, String GtfsFile_URL) {
        try {
            FileDownloader.downloadOsmFile(OsmFile_URL);
            FileDownloader.downloadGtfsFile(GtfsFile_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static void loadRoute(String filePath) {
        RouteLoader loader = new RouteLoader();

        try {
            loader.loadRouteFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loader.test();
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
