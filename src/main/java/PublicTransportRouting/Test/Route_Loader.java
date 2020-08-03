package PublicTransportRouting.Test;

import PublicTransportRouting.Routemodel.PT_Leg;
import PublicTransportRouting.Routemodel.PT_Route;
import PublicTransportRouting.Routemodel.PT_Stop;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class Route_Loader {
    PT_Route route = null;

    public void loadRouteFromFile(String FilePath) throws IOException {
        System.out.println("Route wird von der Datei geladen ..........");


        ObjectMapper om;
        if (FilePath.contains(".yml")){
            om = new ObjectMapper(new YAMLFactory());
            route = om.readValue(new File(FilePath),PT_Route.class);

        }else if (FilePath.contains(".json")){
            om = new ObjectMapper(new JsonFactory());
            route = om.readValue(new File(FilePath),PT_Route.class);

        }else {
            System.out.println("Der gegebene Dateityp kann nicht gelesen werden bitte nur .yml und .json Dateien verwenden");
        }
    }

    public void test(){
        for (PT_Stop stop : route.getStops()){
            System.out.println("-------------------------------------------------------------");
            System.out.println(stop.getStopId());
            System.out.println(stop.getLocation().getLocationName());
            System.out.println(stop.getLocation().getLat()+","+stop.getLocation().getLon());
            System.out.println("Zeiten: "+stop.getArrivalTime()+"  "+stop.getDepartureTime());
            System.out.println("Tick´s: "+stop.getArrivalTick()+"  "+stop.getDepartureTick());
            System.out.println("-------------------------------------------------------------");
        }
        for (PT_Leg leg : route.getLegs()){
            if (leg != null) {
                System.out.println("-------------------------------------------------------------");
                System.out.println(leg.getLegId());
                System.out.println(leg.getStartLocation().getLat() + "," + leg.getStartLocation().getLon());
                System.out.println(leg.getEndLocation().getLat()+" "+leg.getEndLocation().getLon());
                System.out.println(leg.getDepartureTime());
                System.out.println(leg.getArrivalTime());
                System.out.println(leg.getLegType());
                System.out.println(leg.getStopCounter());
                System.out.println(leg.getStops());
                System.out.println("-------------------------------------------------------------");
            }else {
                System.out.println("-------------------------------------------------------------");
                System.out.println("Das hier ist ein walk Leg");
                System.out.println("-------------------------------------------------------------");
            }
        }
        System.out.println(route.getStops().size());
    }
}
