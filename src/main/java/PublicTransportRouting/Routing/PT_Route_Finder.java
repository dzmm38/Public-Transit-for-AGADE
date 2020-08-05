package PublicTransportRouting.Routing;

import PublicTransportRouting.Routemodel.PT_Route;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.graphhopper.GHResponse;
import com.graphhopper.ResponsePath;
import com.graphhopper.reader.gtfs.PtRouteResource;
import com.graphhopper.util.PointList;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to find the routes for a given Query
 * Here the routing from graphhopper happens with a PtRouteResource
 */
public class PT_Route_Finder {
    //------------------------------------------ Variable -------------------------------------------//
    GHResponse response;                            //response from graphhopper to given request
    List<ResponsePath> path;                        //list of path contained by response
    PT_Route_Builder builder;                       //builder class to create the routes which get returned
    public ArrayList<PT_Route> routes;
    ZoneId zoneId;
    LocalDateTime dateTime;

    //----------------------------------------- Constructor -----------------------------------------//
    public PT_Route_Finder(ZoneId zoneId, LocalDateTime dateTime){
        this.zoneId = zoneId;
        this.dateTime = dateTime;
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method which addresses graphhopper to get the response with some routes
     *
     * @param ptQuery the query for which the routing should be done
     * @param PT Object form graphhopper on which the routing happens
     * @param routeSelection String which selects which routes are returned ("all" or "best")
     *                       best relates to earliestArrivalTime
     * @param saveFileFormat String which selects the file format as which the route should be saved
     *                       ("JSON" or "YAML")
     */
    public void findRoute(PT_Query ptQuery, PtRouteResource PT, String routeSelection, String saveFileFormat){
        System.out.println("Searching Routes ..........");
        response = PT.route(ptQuery.getRequest());              //routing form graphhopper and setting the response
        path = new ArrayList<>();

        //selects the how much an which routes should be created "all" and "best" --> default = "best"
        switch (routeSelection){
            case "all":
                path.addAll(response.getAll());
                break;
            case "best":
            default:
                path.add(response.getBest());
                break;
        }
        createRoute(saveFileFormat);
    }

    /**
     * Method to create a Route_Builder to build a route in the route model
     *
     * @param fileFormat format in which the file should be saved
     */
    private void createRoute(String fileFormat) {
        System.out.println("Creating Routes ..........");
        routes = new ArrayList<>();
        PT_Route route;

        for (ResponsePath responsePath : path) {
            builder = new PT_Route_Builder(responsePath, zoneId, dateTime);

            route = builder.build();
            routes.add(route);
        }
        System.out.println("Routes created");
        //selects which data format should be chosen to save the route
        switch (fileFormat) {
            case "YAML":
                toYAML();
                break;
            case "JSON":
            default:
                toJSON();
        }
    }

    /*
    Methode to create a YAML file which contains the route
     */
    private void toYAML() {
        //Name of the File contains the start and end location (as Gps coordinates)
        String extraFileName = cutDecimals(path.get(0).getWaypoints(), 0) + " -- " + //path index doesnt matter cause all routes start and end the same
                cutDecimals(path.get(0).getWaypoints(), 1);           //waypoint only contains start und end Locations so index 0 for start and 1 for end

        System.out.println("Create and saves Route_" + extraFileName + ".yml ..........");
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            om.writeValue(new File("src\\main\\resources\\Routes\\Route_" + extraFileName + ".yml"), routes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Methode to create a JSON file which contains the route
     */
    private void toJSON() {
        //Name of the File contains the start and end location (as Gps coordinates)
        String extraFileName = cutDecimals(path.get(0).getWaypoints(), 0) + " -- " + //path index doesnt matter cause all routes start and end the same
                cutDecimals(path.get(0).getWaypoints(), 1);           //waypoint only contains start und end Locations so index 0 for start and 1 for end

        System.out.println("Creates and saves Route_" + extraFileName + ".json ..........");
        ObjectMapper om = new ObjectMapper(new JsonFactory());
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            om.writeValue(new File("src\\main\\resources\\Routes\\Route_" + extraFileName + ".json"), routes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Method to cut decimals for a Gps Point to the sixth decimal
    then returning it as a String
     */
    private String cutDecimals(PointList pointList, int index) {
        long lat = (long) (pointList.getLat(index) * 1000000);
        long lon = (long) (pointList.getLon(index) * 1000000);
        double latCut = ((double) lat) / 1000000;
        double lonCut = ((double) lon) / 1000000;

        return latCut + "," + lonCut;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    //----------------------------------------- Additional ------------------------------------------//
}

