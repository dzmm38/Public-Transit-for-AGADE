package PublicTransportRouting.Test;

import PublicTransportRouting.Routemodel.PT_Leg;
import PublicTransportRouting.Routemodel.PT_Route;
import PublicTransportRouting.Routemodel.PT_Stop;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;

/**
 * Class to Load a saved Rout in either JSON or YAML
 */
public class Route_Loader {
    //------------------------------------------ Variable -------------------------------------------//
    PT_Route route = null;

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

        if (FilePath.contains(".yml")) {
            om = new ObjectMapper(new YAMLFactory());
            route = om.readValue(new File(FilePath), PT_Route.class);
        } else if (FilePath.contains(".json")) {
            om = new ObjectMapper(new JsonFactory());
            route = om.readValue(new File(FilePath), PT_Route.class);
        } else {
            System.out.println("File type could not be found please use .yml an .json only");
        }
    }

    /**
     * Method to test if the loading was successful by printing out text to the console
     */
    public void test() {
        for (PT_Leg leg : route.getLegs()) {
            if (leg != null) {
                System.out.println("-------------------------------------------------------------");
                System.out.println(leg.getLegId());
                System.out.println(leg.getStartLocation().getLat() + "," + leg.getStartLocation().getLon());
                System.out.println(leg.getEndLocation().getLat() + " " + leg.getEndLocation().getLon());
                System.out.println(leg.getLegStartTime());
                System.out.println(leg.getLegEndTime());
                System.out.println(leg.getLegType());
                System.out.println(leg.getStopCounter());
                System.out.println(leg.getStops());
                System.out.println("-------------------------------------------------------------");
            } else {
                System.out.println("-------------------------------------------------------------");
                System.out.println("Das hier ist ein walk Leg");
                System.out.println("-------------------------------------------------------------");
            }
        }
        for (PT_Stop stop : route.getStops()) {
            System.out.println("-------------------------------------------------------------");
            System.out.println(stop.getStopId());
            System.out.println(stop.getLocation().getLocationName());
            System.out.println(stop.getLocation().getLat() + "," + stop.getLocation().getLon());
            System.out.println("Zeiten: " + stop.getArrivalTime() + "  " + stop.getDepartureTime());
            System.out.println("Tick´s: " + stop.getArrivalTick() + "  " + stop.getDepartureTick());
            System.out.println("-------------------------------------------------------------");
        }
        System.out.println(route.getStops().size());
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public PT_Route getRoute() {
        return route;
    }

    public void setRoute(PT_Route route) {
        this.route = route;
    }

    //----------------------------------------- Additional ------------------------------------------//

    //TODO Things to to with the loaded Route can be written here
}
