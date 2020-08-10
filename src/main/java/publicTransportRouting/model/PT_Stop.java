package publicTransportRouting.model;


import java.io.Serializable;
import java.time.LocalTime;

/**
 * Lowest part of the route model.
 * Contains all information about a stop
 * X stops can be part of a leg or a route
*/
public class PT_Stop implements Serializable {
    //------------------------------------------ Variable -------------------------------------------//
    private String stopId;
    private Location location;
    private int departureTick;
    private LocalTime departureTime;
    private int arrivalTick;
    private LocalTime arrivalTime;

    //----------------------------------------- Constructor -----------------------------------------//
    public PT_Stop(){
        //only to create a PT_Stop from a JSON File
    }

    public PT_Stop(Location location,String stopName,String stopId,LocalTime departureTime,LocalTime arrivalTime,int departureTick,int arrivalTick){
        this.stopId = stopId;
        this.location = location;
        this.location.setLocationName(stopName);
        this.departureTick = departureTick;
        this.departureTime = departureTime;
        this.arrivalTick = arrivalTick;
        this.arrivalTime = arrivalTime;
    }

    //------------------------------------------- Methods -------------------------------------------//
    //--------------------------------------- Getter & Setter ---------------------------------------//
    /*
    There is an getter & Setter method for all variabel´s so the class can be written and constructed
    to or from a JSON file.
     */
    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDepartureTick() {
        return departureTick;
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
    public void setDepartureTick(int departureTick) {
        this.departureTick = departureTick;
    }

    public String getDepartureTime() {
        return departureTime.toString();
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = LocalTime.parse(departureTime);
    }

    public int getArrivalTick() {
        return arrivalTick;
    }

    public void setArrivalTick(int arrivalTick) {
        this.arrivalTick = arrivalTick;
    }

    public String getArrivalTime() {
        return arrivalTime.toString();
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = LocalTime.parse(arrivalTime);
    }

    //----------------------------------------- Additional ------------------------------------------//
}
