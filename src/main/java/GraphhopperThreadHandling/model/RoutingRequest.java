package GraphhopperThreadHandling.model;

import publicTransportRouting.model.Location;
import java.time.LocalDateTime;

/**
 * A Model Class representing a Routing Request for Benchmarking / Testing purposes
 */
public class RoutingRequest {
    //------------------------------------------ Variable -------------------------------------------//
    private Location from;      //the start Location of the Request
    private Location to;        //the end Location of the Request

    private LocalDateTime queryTime;    //the time for which the routing is set
    private String routeSelection;      //the selection Mode of the Request for routing [Graphhopper only]

    private String routingName;         //a String describing the Request / Origin and destination --- only for the testRequests

    //----------------------------------------- Constructor -----------------------------------------//
    public RoutingRequest(Location from, Location to, LocalDateTime queryTime, String routeSelection, String routingName){
        this.from = from;
        this.to = to;
        this.queryTime = queryTime;
        this.routeSelection = routeSelection;   //[Graphhopper only]
        this.routingName = routingName;
    }

    public RoutingRequest(RoutingRequest routingRequest){
        this.from = new Location(routingRequest.from.getLat(),routingRequest.getFrom().getLon());
        this.to = new Location(routingRequest.to.getLat(),routingRequest.to.getLon());
        this.queryTime = LocalDateTime.of(routingRequest.getQueryTime().getYear(),routingRequest.getQueryTime().getMonthValue(),routingRequest.getQueryTime().getDayOfMonth(),
                                          routingRequest.getQueryTime().getHour(),routingRequest.getQueryTime().getMinute());
        this.routeSelection = routingRequest.getRouteSelection();
        this.routingName = routingRequest.getRoutingName();
    }

    //------------------------------------------- Methods -------------------------------------------//
    //--------------------------------------- Getter & Setter ---------------------------------------//
    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public LocalDateTime getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(LocalDateTime queryTime) {
        this.queryTime = queryTime;
    }

    public String getRouteSelection() {
        return routeSelection;
    }   //[Graphhopper only]

    public void setRouteSelection(String routeSelection) {
        this.routeSelection = routeSelection;
    }   //[Graphhopper only]

    public String getRoutingName(){
        return routingName;
    }
}

//----------------------------------------- Additional ------------------------------------------//


