package publicTransportRouting.model;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Second instance of the route model.
 * Contains all information about a Leg. And a List of Stops which
 * all belongs to the same Leg
 *
 * A leg is part of a Route in which the vehicle is remains the same
 */
public class Leg implements Serializable {
    //------------------------------------------ Variable -------------------------------------------//
    private int legId;
    private String legType;
    private Location startLocation;
    private Location endLocation;
    private LocalTime legStartTime;        //departure at current leg (LegStartTime)
    private int legStartTick;
    private LocalTime legEndTime;          //arrival at next stop (LegEndTime)
    private int legEndTick;
    private String vehicle;
    private long stopCounter;

    private List<Stop> stops;

    //----------------------------------------- Constructor -----------------------------------------//
    public Leg() {
        //only to create a Leg from a JSON File
    }

    public Leg(Location startLocation, Location endLocation, LocalTime legStartTime, LocalTime legEndTime,
               String legType, int legId, int legStartTick, int legEndTick, String vehicle) {
        this.legId = legId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.legStartTime = legStartTime;
        this.legStartTick = legStartTick;
        this.legEndTime = legEndTime;
        this.legEndTick = legEndTick;
        this.legType = legType;
        this.vehicle = vehicle;         //vehicle is implemented so that if shows the transit line and possibly the vehicle type
        this.stopCounter = 0;

        //Only creates a list of stops, if the type of the leg is an public transit (pt) leg
        if (legType.equals("pt")) {
            stops = new LinkedList<>();
        } else {
            stops = null;               //because legs of the type walk don´t have stops
        }
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method to create a stop an then add it to the given leg.
     *
     * @param location the location of the stop
     * @param stopName name of the stop
     * @param stopId unique Id of a stop
     * @param departureTime time at which the stop is left within the route
     * @param arrivalTime time at which the stop is reached within the route
     * @param departureTick simulation cycle at which the stop is left within the route
     * @param arrivalTick simulation cycle at which the stop is reached within the route
     */
    public void addStop(Location location,String stopName,String stopId,LocalTime departureTime,LocalTime arrivalTime,int departureTick,int arrivalTick){
        stops.add(new Stop(location, stopName, stopId, departureTime, arrivalTime, departureTick, arrivalTick));
        stopCounter++;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    /*
    There is an getter & Setter method for all variabels so the class can be written and constructed
    to or from a JSON file.
     */
    public int getLegId() {
        return legId;
    }

    public void setLegId(int legId) {
        this.legId = legId;
    }

    public String getLegType() {
        return legType;
    }

    public void setLegType(String legType) {
        this.legType = legType;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public long getStopCounter() {
        return stopCounter;
    }

    public void setStopCounter(long stopCounter) {
        this.stopCounter = stopCounter;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public String getVehicle() {
        return vehicle;
    }

    public int getLegStartTick() {
        return legStartTick;
    }

    public void setLegStartTick(int legStartTick) {
        this.legStartTick = legStartTick;
    }

    public int getLegEndTick() {
        return legEndTick;
    }

    public void setLegEndTick(int legEndTick) {
        this.legEndTick = legEndTick;
    }

    /*
        ---------------------------------------------------------------------------------------------------
        Getter for times are give back Strings because if a LocalTime is written in JSON there would be four
        entries for hour, minute, second, and millisecond. Not all of them are necessary an would make the
        JSON file less clear. (there should be no drawbacks to this)
        ---------------------------------------------------------------------------------------------------
        Setter need String´s to build the LocalTime´s. Reason why are explained above and so are needed
        ---------------------------------------------------------------------------------------------------
         */
    public String getLegStartTime() {
        return legStartTime.toString();
    }

    public void setLegStartTime(String legStartTime) {
        this.legStartTime = LocalTime.parse(legStartTime);
    }

    public String getLegEndTime() {
        return legEndTime.toString();
    }

    public void setLegEndTime(String legEndTime) {
        this.legEndTime = LocalTime.parse(legEndTime);
    }

    //----------------------------------------- Additional ------------------------------------------//
}
