package PublicTransportRouting.Routemodel;

import PublicTransportRouting.supClass.Location;

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
        this.cost = 0.0;                                    //not implemented yet, also gtfs files need to have both of the fare files with corresponding content, but could be an important information later on
        this.transfers = transfers;                                 //is set if legs are added. Because without legs or stops the transfers are always 0
        this.stopCounter = 0;                               //is set if stops are added.

        this.legs = new LinkedList<PT_Leg>();
        this.stops = new LinkedList<PT_Stop>();
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method to create a leg an then add it to given route
     *
     * @param start location from where the leg starts
     * @param end location to where the leg ends
     * @param departureTime time at which the first stop of the leg is left (time at which the leg starts)
     * @param arrivalTime time at which the last stop of the leg is reached (time at which the leg ends)
     * @param legType type of the leg which defines its functions and information (atm. only "pt" and "walk")
     */
    public void addLeg(Location start, Location end, LocalTime departureTime, LocalTime arrivalTime, String legType){
        legCounter++;                                       //if a leg ist added the counter goes up by one to receive a unique leg id
        legs.add(new PT_Leg(start,end,departureTime,arrivalTime,legType,legCounter));
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
        PT_Stop nextStop = it.next();

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
    public Integer getStopIndex(Location location){
        boolean stopFound = false;
        PT_Stop stop;
        int index = -1;                                     //-1 because first entry of the list has the index 0
        ListIterator<PT_Stop> it = stops.listIterator();

        //iterates over the list until the stop is found or the list ends
        while(it.hasNext()){
            stop = it.next();
            index++;

            //checks if the given location and the location of the current stop are the same
            if (stop.getLocation().getLat() == location.getLat() && stop.getLocation().getLon() == location.getLon()){
                stopFound = true;
                return index;
            }
        }
        //if the stop couldn´t be found a console message and null instead of the index
        System.out.println("The stop couldn´t be found in the list of all stops of the route");
        return null;

        /*
        ------------------------------------------------------------------------------
        HIER NOCHMAL SCHAUEN DAS ICH EINE EXPETION MACH WENN KEIN STOP GEFUNDEN WURDE
        ------------------------------------------------------------------------------
         */
    }

/*
------------------------------------------------------------------------------------------
MUSS NOCHMAL NEU GEMACHT UND ÜBERARBEITET WERDEN WIE BEI DEN STOPS für alle legs
------------------------------------------------------------------------------------------
 */
    public PT_Leg getNextLeg(Location end){
        boolean legGefunden = false;
        PT_Leg nextLeg = null;
        Iterator<PT_Leg> it = legs.iterator();

        while (legGefunden = false && it.hasNext()){
            nextLeg = it.next();
            if (nextLeg.getEndLocation().equals(end)){
                nextLeg = it.next();
                legGefunden = true;
            }
        }
        if (legGefunden = false){
            return null;
        }else {
            return nextLeg;
        }
        /*
        suche anhand der end Location eines Legs nach dem nächsten dort müsste
        die anfangs Location gleich der gegebenen End Location sein
        nutze List<PT_Leg> legs
         */
    }

    public void getPreviousLeg(Location start){
        boolean previousLeg = false;
        ListIterator<PT_Leg> it = legs.listIterator();
    }

    public void getcurrentLeg(){

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
