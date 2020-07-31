package PublicTransportRouting.Routemodel;

import PublicTransportRouting.supClass.Location;

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
public class PT_Leg implements Serializable {
    //------------------------------------------ Variable -------------------------------------------//
    private int legId;
    private String legType;
    private Location startLocation;
    private Location endLocation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String vehicle;
    private long stopCounter;

    private List<PT_Stop> stops;

    //----------------------------------------- Constructor -----------------------------------------//
    public PT_Leg(){
        //only to create a PT_Leg from a JSON File
    }

    public PT_Leg(Location startLocation, Location endLocation, LocalTime departureTime, LocalTime arrivalTime, String legType, int legId){
        this.legId = legId;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.legType = legType;
        this.vehicle = null;                        //isn´t implemented yet, gtfs files need to contain this information, listed because seems like an important information
        this.stopCounter = 0;

        //Only creates a list of stops, if the type of the leg is an public transit (pt) leg,
        //because legs of the type walk don´t have stops
        if (legType == "pt"){
            stops = new LinkedList<PT_Stop>();
        }else {
            stops = null;
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
        stops.add(new PT_Stop(location,stopName,stopId,departureTime,arrivalTime,departureTick,arrivalTick));
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

    public List<PT_Stop> getStops() {
        return stops;
    }

    public void setStops(List<PT_Stop> stops) {
        this.stops = stops;
    }

    public String getVehicle() {
        return vehicle;
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
    public String getDepartureTime() {
        return departureTime.toString();
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = LocalTime.parse(departureTime);
    }

    public String getArrivalTime() {
        return arrivalTime.toString();
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = LocalTime.parse(arrivalTime);
    }

    //----------------------------------------- Additional ------------------------------------------//
}
