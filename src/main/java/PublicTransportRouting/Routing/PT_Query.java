package PublicTransportRouting.Routing;

import PublicTransportRouting.Routemodel.Location;
import com.graphhopper.reader.gtfs.Request;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class which contains a request form a location to another location.
 * Used to initialize a request for routing if it´s created
 */
public class PT_Query {
    //------------------------------------------ Variable -------------------------------------------//
    private Request request;

    //----------------------------------------- Constructor -----------------------------------------//
    public PT_Query(Location from, Location to, LocalDateTime dateTime, ZoneId zoneId){
        request = createRequest(from,to,dateTime,zoneId);
    }

    //------------------------------------------- Methods -------------------------------------------//
    /*
    Method to create the request with given parameters and then returns the request
     */
    private Request createRequest(Location from, Location to, LocalDateTime dateTime, ZoneId zoneId){
        Request request = new Request(from.getLat(), from.getLon(), to.getLat(), to.getLon());  //creating the graphhopper request
        request.setEarliestDepartureTime(dateTime.atZone(zoneId).toInstant());                  //setting the start time for the earliest departure time within a time zone
        request.setProfileQuery(true);
        request.setIgnoreTransfers(false);         //Transfers are taken into consideration
        request.setLimitSolutions(5);              //setting a maximum of resulting Routes


        return request;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public Request getRequest() {
        return request;
    }

    //----------------------------------------- Additional ------------------------------------------//

}
