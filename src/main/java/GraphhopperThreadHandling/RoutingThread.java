package GraphhopperThreadHandling;

import GraphhopperThreadHandling.Logs.ThreadLog;
import GraphhopperThreadHandling.Logs.ThreadLogHandler;
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
    public Thread thread;

    //----------------------------------------- Constructor -----------------------------------------//
    public RoutingThread(int threadNumber, ArrayList<RoutingRequest> testRequestList, PT_Facade_Class facade_class, int threadSelectionNr){
        this.testRequest = testRequestList.get(threadSelectionNr);   //sets the request from the testRequestList with the threadSelectionNr (Random form 1 to 10)

        try {
            this.facade_class = (PT_Facade_Class) facade_class.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.routingName = testRequest.getRoutingName();
        this.threadNumber = threadNumber;

        this.thread = new Thread(this, this.threadNumber + "_" + routingName);    //creates the thread with this class and the routingName
        //System.out.println("Thread Nr. " + threadNumber + " created");
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * The run Method of the RoutingThread class calls the ptRouteQueryMethod of the
     * own PT_Facade_class with the given testRequest -- also saves the calculated Route as a
     * JSON File
     */
    @Override
    public void run() {
        //For Logging, creates a ThreadLog Object to later comprehend times etc.
        //LocalTime startTime = LocalTime.now();
        //ThreadLog threadLog = new ThreadLog(startTime,threadNumber,routingName);

        //Printing a message to the console with the time the Thread is starting + it´s number and description Name
        //System.out.println(startTime + " ---- " + "Starting Thread Nr. " + threadNumber + "  -----  " + "Name: " + routingName);

        //Taking the testRequest and forming all necessary parts for an Graphhopper request
        Location from = testRequest.getFrom();
        Location to = testRequest.getTo();
        LocalDateTime queryTime = testRequest.getQueryTime();
        String routeSelection = testRequest.getRouteSelection();    //[Graphhopper only]

        //Sending the Request to Graphhopper for routing
        facade_class.ptRouteQuery(from,to,queryTime,routeSelection);

        //For Logging, creates a ThreadLog Object to later comprehend times etc.
        LocalTime endTime = LocalTime.now();
        //threadLog.setThreadEnd(endTime);

        //Printing a message to the console with the time the Thread is stopped + it´s number and description Name
        System.out.println(endTime + " ---- " + "Stopping Thread Nr. " + threadNumber + "  -----  " + "Name: " + routingName);

        //ThreadLogHandler.addLog(threadLog);
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