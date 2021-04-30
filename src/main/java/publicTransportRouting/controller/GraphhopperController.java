package publicTransportRouting.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.graphhopper.GHResponse;
import com.graphhopper.ResponsePath;
import com.graphhopper.gtfs.PtRouter;
import com.graphhopper.gtfs.Request;
import com.graphhopper.util.PointList;
import publicTransportRouting.model.Location;
import publicTransportRouting.model.Route;

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
public class GraphhopperController {
    //------------------------------------------ Variable -------------------------------------------//
    GHResponse response;                            //response from graphhopper to given request
    List<ResponsePath> path;                        //list of path contained by response
    GraphhopperResponseHandler graphhopperResponseHandler;                       //graphhopperResponseHandler class to create the routes which get returned
    public ArrayList<Route> routes;
    ZoneId zoneId;
    LocalDateTime dateTime;
    Request request;

    //----------------------------------------- Constructor -----------------------------------------//
    public GraphhopperController() {
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method to create a Request to Graphhopper for Routing
     *
     * @param from Location to start from
     * @param to Location as destination
     * @param dateTime Time at which the request should start
     * @param zoneId zone in which the request is given
     */
    public void createRequest(Location from, Location to, LocalDateTime dateTime, ZoneId zoneId) {
        Request request = new Request(from.getLat(), from.getLon(), to.getLat(), to.getLon());  //creating the graphhopper request
        request.setEarliestDepartureTime(dateTime.atZone(zoneId).toInstant());                  //setting the start time for the earliest departure time within a time zone
        request.setProfileQuery(true);
        request.setIgnoreTransfers(true);         //Transfers are taken into consideration if false
        request.setLimitSolutions(5);             //setting a maximum of resulting Routes +1

        this.request = request;
    }


    /**
     * Method which addresses graphhopper to get the response with some routes
     *
     * @param request the query for which the routing should be done
     * @param ptRouter Object form graphhopper on which the routing happens
     * @param routeSelection String which selects which routes are returned ("all" or "best")
     *                       best relates to earliestArrivalTime
     * @param saveFileFormat String which selects the file format as which the route should be saved
     *                       ("JSON" or "YAML")
     */
    public void findRoute(Request request, PtRouter ptRouter, String routeSelection, String saveFileFormat) {
        System.out.println("Searching Routes ..........");
        response = ptRouter.route(request);              //routing form graphhopper and setting the response
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
        Route route;

        for (ResponsePath responsePath : path) {
            graphhopperResponseHandler = new GraphhopperResponseHandler(responsePath, zoneId, dateTime);

            route = graphhopperResponseHandler.build();
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
    public GHResponse getResponse() {
        return response;
    }

    public void setResponse(GHResponse response) {
        this.response = response;
    }

    public List<ResponsePath> getPath() {
        return path;
    }

    public void setPath(List<ResponsePath> path) {
        this.path = path;
    }

    public GraphhopperResponseHandler getGraphhopperResponseHandler() {
        return graphhopperResponseHandler;
    }

    public void setGraphhopperResponseHandler(GraphhopperResponseHandler graphhopperResponseHandler) {
        this.graphhopperResponseHandler = graphhopperResponseHandler;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    //----------------------------------------- Additional ------------------------------------------//
}

