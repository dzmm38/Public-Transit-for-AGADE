package GraphhopperThreadHandling;

import GraphhopperThreadHandling.model.RoutingRequest;
import publicTransportRouting.model.Location;
import publicTransportRouting.service.PT_Facade_Class;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * A Class Representing a Thread with extra information about the Routing Requests
 * which should be calculated
 * and all things necessary for routing
 */
public class RoutingThread implements Runnable{
    //------------------------------------------ Variable -------------------------------------------//
    public RoutingRequest testRequest;      //A random test request (one of 10 different to choose from) --- in ExampleRoutingRequests
    public PT_Facade_Class facade_class;    //A copy of the PT_Facade_Class for the Routing Method which can called by this thread  [Graphhopper only]

    private int threadNumber;               //The number of the Thread which is created
    private String routingName;             //The name describing the Routing request of this thread

    //----------------------------------------- Constructor -----------------------------------------//
    public RoutingThread(int threadNumber, ArrayList<RoutingRequest> testRequestList, PT_Facade_Class facade_class, int threadSelectionNr){
        this.testRequest = testRequestList.get(threadSelectionNr);   //sets the request from the testRequestList with the threadSelectionNr (Random form 1 to 10)
        this.facade_class = facade_class;
        this.routingName = testRequest.getRoutingName();
        this.threadNumber = threadNumber;

        //System.out.println("RoutingThread Nr. " + threadNumber + " " + routingName + " created!");
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * The run Method of the RoutingThread class calls the ptRouteQueryMethod of the
     * own PT_Facade_class with the given testRequest -- also saves the calculated Route as a
     * JSON File
     */
    @Override
    public void run() {
        //Printing a message to the console with the time the Thread is starting + it´s number and description Name
        //System.out.println(LocalTime.now() + " ---- " + "Starting Thread Nr. " + threadNumber + "  -----  " + "Name: " + routingName);

        //Taking the testRequest and forming all necessary parts for an Graphhopper request
        Location from = testRequest.getFrom();
        Location to = testRequest.getTo();
        LocalDateTime queryTime = testRequest.getQueryTime();
        String routeSelection = testRequest.getRouteSelection();    //[Graphhopper only]

        //Sending the Request to Graphhopper for routing
        facade_class.ptRouteQuery(from,to,queryTime,routeSelection);

        //Printing a message to the console with the time the Thread is stopped + it´s number and description Name
        System.out.println(LocalTime.now() + " ---- " + "Stopping Thread Nr. " + threadNumber + "  -----  " + "Name: " + routingName);

//        try {
//            //System.out.println("test");
//            Main.ThreadCounter();       //Adding 1 to the ThreadCounter
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public int getThreadNumber() {
        return threadNumber;
    }

    public String getRoutingName() {
        return routingName;
    }
}
    //----------------------------------------- Additional ------------------------------------------//