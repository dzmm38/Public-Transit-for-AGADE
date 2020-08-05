package PublicTransportRouting.Routing;

import PublicTransportRouting.Routemodel.PT_Leg;
import PublicTransportRouting.Routemodel.PT_Route;
import PublicTransportRouting.Routemodel.PT_Stop;
import PublicTransportRouting.Time.TimeCalculator;
import PublicTransportRouting.Time.TimeConverter;
import PublicTransportRouting.supClass.Location;
import com.graphhopper.ResponsePath;
import com.graphhopper.Trip;
import org.locationtech.jts.geom.Coordinate;
import java.time.*;
import java.util.ArrayList;

/**
 * Class to build a route which then can be saved as a JSON or YAML file.
 * Builds a PT_Route with PT_Legs and PT_Stops with all important information form a ResponsePath of graphopper
 *
 *  Needs the calculated Response path, a zone Id and the simulation start time to construct a route
 */
public class PT_Route_Builder {
    //------------------------------------------ Variable -------------------------------------------//
    ResponsePath path;                              //the route which graphopper created
    PT_Route route;
    ZoneId zoneId;
    LocalDateTime startTime;
    TimeCalculator calculator;
    TimeConverter converter;
    ZoneOffset offset;                              //time difference between the given zone and the UTC (Coordinated Universal Time)
    boolean firstStopOfRoute = true;                //to check which is the first stop of an route, to set later the arrival time to the start time
    PT_Stop lastStopOfLeg = null;                   //to save the last stop of an leg to then set it´s correct departure time an the correct arrival time of the first stop of the next leg

    //----------------------------------------- Constructor -----------------------------------------//
    public PT_Route_Builder(ResponsePath path, ZoneId zoneId,LocalDateTime dateTime){
        this.path = path;
        this.zoneId = zoneId;
        this.startTime = dateTime;

        calculator = new TimeCalculator();
        converter = new TimeConverter();
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Builds the route and returns it, also save the route as either a JSON or YAML file
     *
     * @return PT_Route for given ResponsePath
     */
    public PT_Route build(){
        buildRoute();
        buildLegs();
        deleteDoubleStops();

        System.out.println("Route created");
        return route;
    }

    /*
    Build and creates the base concept of the route, the PT_Route itself without any legs or stops
    The criteria to differ between routes are set here
     */
    private void buildRoute(){
        offset = zoneId.getRules().getOffset(startTime);                            //sets the offset of the given zone
        long duration = converter.Sec_To_Min((path.getTime()/1000));                //the time which the ResponePath returns are in milliseconds, divide with 1000 to get seconds
        LocalTime arrivalTime = startTime.plusMinutes(duration).toLocalTime();      //to get the arrival time, the duration is added to the startTime
        double distance = Math.round(path.getDistance());
        int transfers = path.getNumChanges();

        route = new PT_Route(arrivalTime,duration,distance,transfers);
    }

    /*
    Builds the leg to the route which was build in buildRoute
    Creates all legs to the Route with all information as well as all Stops
    The Method adds the leg then to it´s route
     */
    private void buildLegs(){
        for (Trip.Leg leg : path.getLegs()){
            //Checks for all legs the type to either convert them to a Trip.PtLeg or a Trip.WalkLeg
            //Each of the special legs have different attributes to create the legs an stops
            if (leg.type.equals("pt")) {
                Trip.PtLeg ptLeg = (Trip.PtLeg) leg;

                //Always null because the location data for the first/last stop is only available if the first/last stop of the leg is created thus, location is set later
                Location start = null;
                Location end = null;

                /*
                Arrival and departure times from the legs are given in milliseconds and in epochal time
                It´s needed to first divide it with 1000 to get seconds and then create a LocalDateTime of the epochal seconds.
                The offset is set above an since there isn´t any nano seconds given it can be set to 0.
                Last its needed to get the LocalTime from the LocalDateTime an set it as departure an arrival time.
                 */
                LocalTime departureTime = LocalDateTime.ofEpochSecond(ptLeg.getDepartureTime().getTime() / 1000, 0, offset).toLocalTime();
                LocalTime arrivalTime = LocalDateTime.ofEpochSecond(ptLeg.getArrivalTime().getTime() / 1000, 0, offset).toLocalTime();
                int departureTick = calculator.calculateSimulationTick(departureTime);          //transform time to simulation tick
                int arrivalTick = calculator.calculateSimulationTick(arrivalTime);              //transform time to simulation tick
                String legType = ptLeg.type;
                String vehicle = getVehicleLine(ptLeg.trip_headsign);


                PT_Leg routeLeg = new PT_Leg(start, end, departureTime, arrivalTime, legType, 0, departureTick, arrivalTick, vehicle);  //legId 0 because if the leg is later added to it´s route the legId is set correctly
                buildStops(ptLeg, routeLeg);            //build and adds all stop to the route
                route.addLeg(routeLeg);
            } else {
                Trip.WalkLeg walkLeg = (Trip.WalkLeg) leg;

                //Please refer to where the departure and arrival times are set for the ptleg (also in this Method)
                //TODO diese methode und die Methode bei ptleg in eigene Methode da hier complett gleich --> spart platz !!!
                LocalTime departure = LocalDateTime.ofEpochSecond(walkLeg.getDepartureTime().getTime() / 1000, 0, offset).toLocalTime();
                LocalTime arrival = LocalDateTime.ofEpochSecond(walkLeg.getArrivalTime().getTime() / 1000, 0, offset).toLocalTime();
                int departureTick = calculator.calculateSimulationTick(departure);
                int arrivalTick = calculator.calculateSimulationTick(arrival);

                String legType = walkLeg.type;
                Coordinate[] coordinates = walkLeg.geometry.getCoordinates();                                           //array of all corner points coordinates of the walk leg
                Location start = new Location(cutGpsDecimals(coordinates[0].y), cutGpsDecimals(coordinates[0].x));      //first point in the list is then the start point of the walkleg
                Location end = new Location(cutGpsDecimals(coordinates[coordinates.length - 1].y),
                        cutGpsDecimals(coordinates[coordinates.length - 1].x));                     //last point in  the list is then the end point of the walkleg

                route.addLeg(start, end, departure, arrival, legType, departureTick, arrivalTick, "foot");
            }

        }
        fixLegs();
    }

    /*
    Method to create all stops of an leg and then add it to the stops list in PT_Leg and PT_Route
    To build the stops the Method needs an Trip.PtLeg to get the stops an PT_Leg to add it to
    The information form the Trip.Stop from graphhopper are then converted to a PT_Stop
     */
    private void buildStops(Trip.PtLeg ptLeg, PT_Leg routeLeg){
        int stopCounter = 0;

        for (Trip.Stop stop : ptLeg.stops){

            String stopName = stop.stop_name;           //TODO MUSS NOCH ENTFERNT WERDEN UN IN LOCATION EINGEFÜGT WERDEN
            String stopId = stop.stop_id;
            Location location = new Location(stop.geometry.getY(),stop.geometry.getX());

            LocalTime departureTime;
            LocalTime arrivalTime;
            int departureTick;
            int arrivalTick;

            /*
            Hier wird überprüft ob es sich um den Anfang/Ende der Route oder eines Legs handelt, welche keine departure
            oder arrival Zeit besitzt und dann eingefügt
             */
            if (stop.arrivalTime == null) {

                if (firstStopOfRoute) {
                    departureTime = LocalDateTime.ofEpochSecond((stop.departureTime.getTime() / 1000), 0, offset).toLocalTime();
                    arrivalTime = startTime.toLocalTime();          //sets the arrivalTime to the time of the request
                    firstStopOfRoute = false;
                } else {
                    departureTime = LocalDateTime.ofEpochSecond((stop.departureTime.getTime() / 1000), 0, offset).toLocalTime();
                    //if the arrival time is null an this isn´t the first stop of the route, then it´s have to be the first of the leg
                    //fixStops to solve the missing times for this stop an the one before
                    arrivalTime = fixStops(departureTime);
                }
            }else if (stop.departureTime == null){
                arrivalTime = LocalDateTime.ofEpochSecond(stop.arrivalTime.getTime()/1000,0,offset).toLocalTime();
                departureTime = LocalTime.of(0,0);      //first set departure time to O if non is available, time is set correctly later
            }else {
                /*
                Arrival and departure times from the stops are given in milliseconds and in epochal time
                It´s needed to first divide it with 1000 to get seconds and then create a LocalDateTime of the epochal seconds.
                The offset is set above an since there isn´t any nano seconds given it can be set to 0.
                Last its needed to get the LocalTime from the LocalDateTime an set it as departure an arrival time.
                 */
                arrivalTime = LocalDateTime.ofEpochSecond(stop.arrivalTime.getTime() / 1000, 0, offset).toLocalTime();
                departureTime = LocalDateTime.ofEpochSecond((stop.departureTime.getTime() / 1000), 0, offset).toLocalTime();
            }

            //Converts the set arrival an departure times to ticks dependent on the simulation start time
            arrivalTick = calculator.calculateSimulationTick(arrivalTime);
            departureTick = calculator.calculateSimulationTick(departureTime);

            //Checks if an stop is the first or the last of a leg
            if (stopCounter == 0) {
                routeLeg.setStartLocation(location);            //sets the start location for given leg which was set to null at creation
            } else if (stopCounter == ptLeg.stops.size() - 1) {
                routeLeg.setEndLocation(location);              //sets the end location for given leg which was set to null at creation
                lastStopOfLeg = new PT_Stop(location, stopName, stopId, departureTime, arrivalTime, departureTick, arrivalTick);  //because was checked to  be last stop of leg -> set to last stop
            }

            //Add the stops to both lists (stop list in PT_Leg and stop list in PT_Route)
            routeLeg.addStop(location,stopName,stopId,departureTime,arrivalTime,departureTick,arrivalTick);
            route.addStop(location,stopName,stopId,departureTime,arrivalTime,departureTick,arrivalTick);
            stopCounter++;
        }
    }

    /*
    Some stops dont have a correct arrival and departure time because those aren´t given by the graphhopper ResponsePath.
    Here the missing times are set.
    The departure time of stop x (first of an leg an not the first of the route) ist given to set its departure to stop x-1
    Returns then a Local time of stop x-1 arrival time (can then be set to x stop arrival time)
    So no stop is missing a time
     */
    private LocalTime fixStops(LocalTime departureTime) {
        LocalTime arrivalTime = LocalTime.parse(lastStopOfLeg.getArrivalTime());                        //parse arrival time because the getter is returning a String

        PT_Stop stop = route.getStops().get(route.getStopIndex(lastStopOfLeg.getLocation()));           //gets the index of the last stop of the leg to edit the stop in the list
        stop.setDepartureTime(departureTime.toString());
        stop.setDepartureTick(calculator.calculateSimulationTick(departureTime));                       //sets the departure tick which also cloudn´t be set because no time was given

        return arrivalTime;
    }

    private void fixLegs() {
        for (PT_Leg legs : route.getLegs()) {
            if (legs.getLegType().equals("pt")) {
                PT_Stop stop;
                stop = legs.getStops().get((int) legs.getStopCounter() - 1);
                PT_Leg nextLeg = route.getNextLeg(legs.getLegId());
                stop.setDepartureTime(nextLeg.getDepartureTime());
                stop.setDepartureTick(calculator.calculateSimulationTick(stop.getDepartureTime()));
            }
        }
    }

    /*
    Deletes the stops in the PT_Route stop list which are doubled
    (stops are doubled because if during the route an transfer occurs the same stop is used as last stop of the current
    leg and first stop of the next leg)
     */
    private void deleteDoubleStops() {
        ArrayList<PT_Stop> deleteList = new ArrayList<>();
        PT_Stop stop;
        for (int i = 0; i < route.getStops().size(); i++) {
            stop = route.getStops().get(i);
            if (i < route.getStops().size() - 1) {
                //for each stop it is checked if the name of the location is the same as the name of the next stop
                if (stop.getLocation().getLocationName().equals(route.getNextStop(stop.getLocation()).getLocation().getLocationName())) {
                    deleteList.add(stop);
                }
            }
            if (i == route.getStops().size() - 1) {
                stop.setDepartureTime(route.getArrivalTime());
                stop.setDepartureTick(calculator.calculateSimulationTick(stop.getArrivalTime()));
            }
        }

        //for every entry in the list the associated stop is deleted in PT_Route stops list
        for (PT_Stop deleteStop : deleteList) {
            route.getStops().remove(deleteStop);
        }
        route.setStopCounter(route.getStopCounter() - deleteList.size());
    }

    private String getVehicleLine(String trip_headsign) {
        String vehicleLine;
        String[] headsignSplit = trip_headsign.split(" ");
        vehicleLine = headsignSplit[0];
        return vehicleLine;
    }

    private double cutGpsDecimals(double latOrlon) {
        double normal = latOrlon * 1000000;
        long cut = (long) normal;
        return ((double) cut) / 1000000;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    //----------------------------------------- Additional ------------------------------------------//
}
