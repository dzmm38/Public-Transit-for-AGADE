package publicTransportRouting.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Top class of the route model.
 * Contains some decision criteria to differentiate two routes with same
 * start and end location.
 *
 * Contains a list of legs and stops which then can be used for routing
 * Also this class provides some methods to route within the class
 */
public class Route implements Serializable {
    //------------------------------------------ Variable -------------------------------------------//
    private int legCounter = 0;                             //Counter for legs to be able to add a legId

    private LocalTime arrivalTime;
    private long durationInMin;
    private int transfers;
    private double walkDistanceInMeters;
    private BigDecimal cost;
    private int stopCounter;

    private LinkedList<Leg> legs;
    private LinkedList<Stop> stops;

    //----------------------------------------- Constructor -----------------------------------------//
    public Route() {
        //only to create a Route from a JSON File
    }

    public Route(LocalTime arrivalTime, long durationInMin, double walkDistanceInMeters, int transfers,BigDecimal cost) {
        this.arrivalTime = arrivalTime;
        this.durationInMin = durationInMin;
        this.walkDistanceInMeters = walkDistanceInMeters;
        this.cost = cost;
        this.transfers = transfers;                         //is set if legs are added. Because without legs or stops the transfers are always 0
        this.stopCounter = 0;                               //is set if stops are added.

        this.legs = new LinkedList<>();
        this.stops = new LinkedList<>();
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method to create a leg an then add it to given route
     *
     * @param start         location from where the leg starts
     * @param end           location to where the leg ends
     * @param departureTime time at which the first stop of the leg is left (time at which the leg starts)
     * @param arrivalTime   time at which the last stop of the leg is reached (time at which the leg ends)
     * @param legType       type of the leg which defines its functions and information (atm. only "pt" and "walk")
     */
    public void addLeg(Location start, Location end, LocalTime departureTime, LocalTime arrivalTime, String legType, int departureTick, int arrivalTick, String vehicle) {
        legCounter++;                                       //if a leg ist added the counter goes up by one to receive a unique leg id
        legs.add(new Leg(start, end, departureTime, arrivalTime, legType, legCounter, departureTick, arrivalTick, vehicle));
    }

    /**
     * Method to add a created leg to given route
     *
     * @param ptLeg Leg can be a public transit or walk leg
     */
    public void addLeg(Leg ptLeg) {
        legCounter++;                                       //if a leg ist added the counter goes up by one to receive a unique leg id
        ptLeg.setLegId(legCounter);
        legs.add(ptLeg);
    }

    /**
     * Method to create an add a stop to it´s given route
     *
     * @param location location (Gps data) which refers to the stop
     * @param stopName name of the stop
     * @param stopId unique Id of a stop
     * @param departureTime time at which the stop is left within the route
     * @param arrivalTime time at which the stop is reached within the route
     * @param departureTick simulation cycle at which the stop is left within the route
     * @param arrivalTick simulation cycle at which the stop is reached within the route
     */
    public void addStop(Location location, String stopName, String stopId,LocalTime departureTime,LocalTime arrivalTime,int departureTick,int arrivalTick){
        stops.add(new Stop(location, stopName, stopId, departureTime, arrivalTime, departureTick, arrivalTick));
        stopCounter++;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    /*
    There is an getter & Setter method for all variabels so the class can be written and constructed
    to or from a JSON file.
     */
    public String getArrivalTime() {
        return arrivalTime.toString();
    }   //Getter for the arrivalTime are giving back Strings because it´s clearer if it´s shown as a String in a JSON file

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = LocalTime.parse(arrivalTime);
    }   //thus the Setter needs a String to set the Arrival Time

    public long getDurationInMin() {
        return durationInMin;
    }

    public void setDurationInMin(long durationInMin) {
        this.durationInMin = durationInMin;
    }

    public int getTransfers() {
        return transfers;
    }

    public void setTransfers(int transfers) {
        this.transfers = transfers;
    }

    public double getWalkDistanceInMeters() {
        return walkDistanceInMeters;
    }

    public void setWalkDistanceInMeters(double walkDistanceInMeters) {
        this.walkDistanceInMeters = walkDistanceInMeters;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public int getStopCounter() {
        return stopCounter;
    }

    public void setStopCounter(int stopCounter) {
        this.stopCounter = stopCounter;
    }

    public LinkedList<Leg> getLegs() {
        return legs;
    }

    public void setLegs(LinkedList<Leg> legs) {
        this.legs = legs;
    }

    public LinkedList<Stop> getStops() {
        return stops;
    }

    public void setStops(LinkedList<Stop> stops) {
        this.stops = stops;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
