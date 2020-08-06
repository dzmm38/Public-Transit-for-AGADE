package PublicTransportRouting.Test;

import PublicTransportRouting.Routemodel.PT_Route;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class to Load a saved Rout in either JSON or YAML
 */
public class Route_Loader {
    //------------------------------------------ Variable -------------------------------------------//
    PT_Route[] routeArray;

    //----------------------------------------- Constructor -----------------------------------------//
    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Methode to load an existing Route from a file
     *
     * @param FilePath String which represents the exact File path.
     *                 for example src\main\resources\Routes\RoutenListe.json
     * @throws IOException Exeption if the File could not be loaded or found
     */
    public void loadRouteFromFile(String FilePath) throws IOException {
        System.out.println("Reading Route from file ..........");
        ObjectMapper om;

        //Checks if the File is an YAML or JSON File
        if (FilePath.contains(".yml")) {
            om = new ObjectMapper(new YAMLFactory());
            routeArray = om.readValue(new File(FilePath), PT_Route[].class);    //reading the YAML file in an array, every entry represents a Route
        } else if (FilePath.contains(".json")) {
            om = new ObjectMapper(new JsonFactory());
            routeArray = om.readValue(new File(FilePath), PT_Route[].class);    //reading the JSON file in an array, every entry represents a Route
        } else {
            System.out.println("File type could not be found please use .yml an .json only");
        }
    }

    /**
     * Method to test if the loading was successful by printing out text to the console
     */
    public void test() {
        Arrays.stream(routeArray).forEach(testroute -> {
            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("ArrivalTime:     " + testroute.getArrivalTime());
            System.out.println("Route Duration:  " + testroute.getDurationInMin());
            System.out.println("Transfers:       " + testroute.getTransfers());
            System.out.println("Walk Distance:   " + testroute.getWalkDistanceInMeters());
            System.out.println("Kosten der Route:" + testroute.getCost());
            System.out.println("Anzahl Stops:    " + testroute.getStopCounter());
            System.out.println("-------------------------------------------------------------------------------------");
        });
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public PT_Route[] getRouteArray() {
        return routeArray;
    }

    public void setRouteArray(PT_Route[] routeArray) {
        this.routeArray = routeArray;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
