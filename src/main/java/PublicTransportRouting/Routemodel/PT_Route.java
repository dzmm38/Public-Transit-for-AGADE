package PublicTransportRouting.Routemodel;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;

/**
 * Top class of the route model.
 * Contains some decision criteria to differentiate two routes with same
 * start and end location.
 *
 * Contains a list of legs and stops which then can be used for routing
 * Also this class provides some methods to route within the class
 */
public class PT_Route implements Serializable {
    //------------------------------------------ Variable -------------------------------------------//
    private int legCounter = 0;                             //Counter for legs to be able to add a legId

    private LocalTime arrivalTime;
    private long durationInMin;
    private int transfers;
    private double walkDistanceInMeters;
    private double cost;
    private int stopCounter;

    private LinkedList<PT_Leg> legs;
    private LinkedList<PT_Stop> stops;

    //----------------------------------------- Constructor -----------------------------------------//
    public PT_Route(){
        //only to create a PT_Route from a JSON File
    }

    public PT_Route(LocalTime arrivalTime, long durationInMin, double walkDistanceInMeters,int transfers){
        this.arrivalTime = arrivalTime;
        this.durationInMin = durationInMin;
        this.walkDistanceInMeters = walkDistanceInMeters;
        this.cost = 0.0;                                    //TODO Method for costs to add this criteria even if no fare data is given in gtfs / atm not implemented cause no fare data is given in gtfs
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
        legs.add(new PT_Leg(start, end, departureTime, arrivalTime, legType, legCounter, departureTick, arrivalTick, vehicle));
    }

    /**
     * Method to add a created leg to given route
     *
     * @param ptLeg Leg can be a public transit or walk leg
     */
    public void addLeg(PT_Leg ptLeg){
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
        stops.add(new PT_Stop(location,stopName,stopId,departureTime,arrivalTime,departureTick,arrivalTick));
        stopCounter++;
    }

    /**
     * Method to determine the next stop in a route and
     * then returns it
     * Uses the LinkedList stops
     *
     * @param location location of a stop of the route
     * @return gives back, the next PT_Stop within the route
     */
    public PT_Stop getNextStop(Location location){
        int index = getStopIndex(location);
        ListIterator<PT_Stop> it = stops.listIterator(index);
        PT_Stop nextStop;
        nextStop = it.next();

        //Check if the given stop has a successor, if not then it´s the last stop of the route an the nex stop equals null
        if (it.hasNext()){
            nextStop = it.next();
        }else {
            nextStop = null;
            System.out.println("The given stop or location doesn´t have a successor and thus is the last stop of the route");
        }
        return nextStop;
    }

    /**
     * Method to determine the previous stop of a given location from a stop
     * and then returns it
     * Uses LinkedList stops
     *
     * @param location location of a stop of the route
     * @return the previous PT_Stop of the same route
     */
    public PT_Stop getPreviousStop(Location location) {
        int index = getStopIndex(location);
        ListIterator<PT_Stop> it = stops.listIterator(index);
        PT_Stop previousStop;

        //Checks if the given stop has a predecessor, if not then it´s the first stop of a route and the stop equals null
        if (it.hasPrevious()){
            previousStop = it.previous();
        }else {
            previousStop = null;
            System.out.println("The given stop doesn´t have a predecessor and thus is the first stop of a route");
        }
        return previousStop;
    }

    /**
     * Method which returns the stop to a given location
     * Only if the location is in the stops list
     *
     * @param location location of a stop of the route
     * @return a stop to the given location
     */
    public PT_Stop getCurrentStop(Location location){
        int index = getStopIndex(location);
        return stops.get(index);
    }

    /**
     * Method which returns the index of a stop within the
     * LinkedList stops bases on a location of a stop of the route
     *
     * @param location location of a stop of the route
     * @return index of a stop (of List stops) which belongs to given location
     */
    public Integer getStopIndex(Location location) {
        PT_Stop stop;
        int index = -1;                                     //-1 because first entry of the list has the index 0

        //iterates over the list until the stop is found or the list ends
        for (PT_Stop pt_stop : stops) {
            stop = pt_stop;
            index++;

            //checks if the given location and the location of the current stop are the same
            if (stop.getLocation().getLat() == location.getLat() && stop.getLocation().getLon() == location.getLon()) {
                return index;
            }
        }
        //if the stop couldn´t be found a console message and null instead of the index
        System.out.println("The stop couldn´t be found in the list of all stops of the route");
        return null;
    }


    //TODO vllt nochmal mehr methoden um abfragen auf der Route zu machen --> getStops getLegs auch mit anderen param

    /**
     * Method to get the next leg in the Route given an leg Id
     *
     * @param legId Id of the current Leg
     * @return the nex PT_Leg of the Route
     */
    public PT_Leg getNextLeg(int legId) {
        ListIterator<PT_Leg> it = legs.listIterator(legId);
        if (it.hasNext()) {
            return it.next();
        } else {
            System.out.println("Leg has no successor");
            return null;
        }
    }

    /**
     * Method to get the previous leg in the Route given a leg Id
     *
     * @param legId Id of the current leg
     * @return previous PT_Leg of the Route
     */
    public PT_Leg getPreviousLeg(int legId) {
        ListIterator<PT_Leg> it = legs.listIterator(legId);
        if (it.hasPrevious()) {
            return it.previous();
        } else {
            System.out.println("Leg has no predecessor");
            return null;
        }
    }

    /**
     * Method to get the current leg given a leg Id
     *
     * @param legId Id of the leg which should be returned
     * @return current PT_Leg
     */
    public PT_Leg getcurrentLeg(int legId) {
        boolean legFound = false;
        PT_Leg currentLeg = null;
        ListIterator<PT_Leg> it = legs.listIterator();
        while (!legFound & it.hasNext()) {
            currentLeg = it.next();
            if (currentLeg.getLegId() == legId) {
                legFound = true;
            }
        }
        return currentLeg;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getStopCounter() {
        return stopCounter;
    }

    public void setStopCounter(int stopCounter) {
        this.stopCounter = stopCounter;
    }

    public LinkedList<PT_Leg> getLegs() {
        return legs;
    }

    public void setLegs(LinkedList<PT_Leg> legs) {
        this.legs = legs;
    }

    public LinkedList<PT_Stop> getStops() {
        return stops;
    }

    public void setStops(LinkedList<PT_Stop> stops) {
        this.stops = stops;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
