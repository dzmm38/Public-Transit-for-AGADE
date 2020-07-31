package PublicTransportRouting.Routing;

import PublicTransportRouting.Routemodel.PT_Route;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.graphhopper.GHResponse;
import com.graphhopper.ResponsePath;
import com.graphhopper.reader.gtfs.PtRouteResource;
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
    LocalDateTime dateTime;                         //TODO wie genau war das nochmal mit der Zeit hier wofür steht die ? brauche ich zwei zeiten für simulationsstart und abfragezeit???

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
        path = new ArrayList<ResponsePath>();


        switch (routeSelection){
            case "best" : path.add(response.getBest()); break;
            case "all"  :
                for (ResponsePath responsePath : response.getAll()){
                    path.add(responsePath);
            }
                break;
            default: path.add(response.getBest()); break;
        }
        createRoute(saveFileFormat);
    }

    /**
     * Method to create a Route_Builder to build a route in the route model
     *
     * @param fileFormat format in which the file should be saved
     */
    private void createRoute(String fileFormat){
        System.out.println("Creating Routes ..........");
        routes = new ArrayList<PT_Route>();
        PT_Route route;

        for (ResponsePath responsePath : path){
            builder = new PT_Route_Builder(responsePath,zoneId,dateTime);

            route = builder.build();
            routes.add(route);
        }
        switch (fileFormat){
            case "JSON" : toJSON(); break;
            case "YAML" : toYAML(); break;
            default: toJSON();
        }
    }
    /*
    Methode to create a YAML file which contains the route
     */
    private void toYAML(){
        String extraFileName =  path.get(0).getWaypoints().getLat(0)+","+path.get(0).getWaypoints().getLon(0)+"_" +
                                path.get(0).getWaypoints().getLat(1)+","+path.get(0).getWaypoints().getLon(1);
        System.out.println("Create and saves Route_"+extraFileName+".yml ..........");
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            om.writeValue(new File("src\\main\\resources\\Routes\\Route_"+extraFileName+".yml"),routes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Methode to create a JSON file which contains the route
     */
    private void toJSON(){
        String extraFileName =  path.get(0).getWaypoints().getLat(0)+","+path.get(0).getWaypoints().getLon(0)+"_" +
                                path.get(0).getWaypoints().getLat(1)+","+path.get(0).getWaypoints().getLon(1);
        System.out.println("Creates and saves Route_"+extraFileName+".json ..........");
        ObjectMapper om = new ObjectMapper(new JsonFactory());
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            om.writeValue(new File("src\\main\\resources\\Routes\\Route_"+extraFileName+".json"),routes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    //----------------------------------------- Additional ------------------------------------------//
}

