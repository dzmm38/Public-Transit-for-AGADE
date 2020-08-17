package publicTransportRouting.controller;

import publicTransportRouting.model.Leg;
import publicTransportRouting.model.Location;
import publicTransportRouting.model.Route;
import publicTransportRouting.model.Stop;

import java.util.ListIterator;

/**
 * Class that makes it possible to be able to iterate / do call up on
 * a Route.
 * Contains Methods to get the Next Stop / Legs etc.
 */
public class RouteIterator {
    //------------------------------------------ Variable -------------------------------------------//
    private Route route;        //Route on which the call up should be called

    //----------------------------------------- Constructor -----------------------------------------//
    /**
     * Constructor to set the given route to the RouteIterator
     *
     * @param route Route on which you want to do queries
     */
    public RouteIterator(Route route){
        this.route = route;
    }

    //------------------------------------------- Methods -------------------------------------------//
    //TODO Here one may be inserting additional query Options for an Route if additional ones are needed

    /**
     * Method to determine the next stop in a route and
     * then returns it
     * Uses the LinkedList stops
     *
     * @param location location of a stop of the route
     * @return gives back, the next Stop within the route
     */
    public Stop getNextStop(Location location) {
        int index = getStopIndex(location);
        ListIterator<Stop> it = route.getStops().listIterator(index);
        Stop nextStop;
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
     * @return the previous Stop of the same route
     */
    public Stop getPreviousStop(Location location) {
        int index = getStopIndex(location);
        ListIterator<Stop> it = route.getStops().listIterator(index);
        Stop previousStop;

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
    public Stop getCurrentStop(Location location) {
        int index = getStopIndex(location);
        return route.getStops().get(index);
    }

    /**
     * Method which returns the index of a stop within the
     * LinkedList stops bases on a location of a stop of the route
     *
     * @param location location of a stop of the route
     * @return index of a stop (of List stops) which belongs to given location
     */
    public Integer getStopIndex(Location location) {
        Stop stop;
        int index = -1;                                     //-1 because first entry of the list has the index 0

        //iterates over the list until the stop is found or the list ends
        for (Stop _stop : route.getStops()) {
            stop = _stop;
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

    /**
     * Method to get the next leg in the Route given an leg Id
     *
     * @param legId Id of the current Leg
     * @return the nex Leg of the Route
     */
    public Leg getNextLeg(int legId) {
        ListIterator<Leg> it = route.getLegs().listIterator(legId);
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
     * @return previous Leg of the Route
     */
    public Leg getPreviousLeg(int legId) {
        ListIterator<Leg> it = route.getLegs().listIterator(legId);
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
     * @return current Leg
     */
    public Leg getCurrentLeg(int legId) {
        boolean legFound = false;
        Leg currentLeg = null;
        ListIterator<Leg> it = route.getLegs().listIterator();
        while (!legFound & it.hasNext()) {
            currentLeg = it.next();
            if (currentLeg.getLegId() == legId) {
                legFound = true;
            }
        }
        return currentLeg;
    }
    //--------------------------------------- Getter & Setter ---------------------------------------//
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
