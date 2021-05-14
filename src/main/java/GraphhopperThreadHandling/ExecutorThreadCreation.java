package GraphhopperThreadHandling;

import GraphhopperThreadHandling.model.ExampleRoutingRequests;
import GraphhopperThreadHandling.model.RoutingRequest;
import publicTransportRouting.service.PT_Facade_Class;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main Class --> Start Point of the Testing Process
 * Unter Settings you can change the variables to adjust the testing settings
 */
public class ExecutorThreadCreation {
    //------------------------------------------ Settings -------------------------------------------//
    static int AmountOfThreads = 100;           //TODO: Dann testen mit 100/1tsd/10tsd/etc.
    int ThreadPool = 100;                //Wenn der Thread Pool = Anzahl der Threads dann werden alle gleichzeitig bearbeitet

    String ZoneId = "Europe/Berlin";
    int simulationYear = 2020;
    int simulationMonth = 8;
    int simulationDay = 6;
    int simulationHour = 7;
    int simulationMin = 0;
    String fileFormat = "JSON";     //[Graphhopper only]
    String graphFolderName = "berlin-latest.osm.pbf_with_Transit";  //[Graphhopper only]

    //------------------------------------------ Variable -------------------------------------------//
    public ArrayList<RoutingRequest> testingRequests;   //List of all Requests created for testing --- in ExampleRoutingRequests.class
    public PT_Facade_Class facade_class;         //the PT_Facade_Class used to set the ones in the Threads / which then is used for Routing Methods [Graphhopper only]
    public ArrayList<Integer> pickedRouteList;          //List containing the choosen Amount of the TestRequests

    public LocalTime startTime;
    public LocalTime prepEnd;
    public LocalTime routingStart;
    public LocalTime routingEnd;

    /**
     * Method to start the Benchmarking / Testing
     * Loads a Graph Named X and initialises the PT_Facade_class
     * Creates all testing Requests
     * Creates X Threads every one of them get an random Request out of testing ones.
     * the Thread then is started an routing begins -- after the Routing is done the Requests is written as JSON
     *
     * Creating a Graph is needed to be done separately in a test class
     * @param args
     */
    //----------------------------------------- Main Method -----------------------------------------//
    public static void main(String[] args) {
        ExecutorThreadCreation executorThreadCreation = new ExecutorThreadCreation();

        executorThreadCreation.startTime = LocalTime.now();   //Timestamp --> for programm starting time

        executorThreadCreation.testingRequests = new ExampleRoutingRequests().getTestRequest();   //creates the testingRequests and sets them
        executorThreadCreation.GraphhopperHandling(); //loads a Graph an initialises the PT_FacadeClass [Graphhopper only]

        executorThreadCreation.prepEnd = LocalTime.now();     //Timestamp --> for preparation (graph loading / test route creating) end
        try {
            System.out.println("Starting The Routing test.....");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorThreadCreation.routingStart = LocalTime.now();     //Timestamp --> for Routing Start

        executorThreadCreation.createAndStartTest();    //Test Methode
    }

    //----------------------------------------- Constructor -----------------------------------------//
    //------------------------------------------- Methods -------------------------------------------//
    /*
    Initialises The Facade Class an loads the Graph
     */
    private void GraphhopperHandling(){
        facade_class = new PT_Facade_Class(ZoneId,simulationYear,simulationMonth,simulationDay,simulationHour,simulationMin,fileFormat);
        facade_class.loadGraph(graphFolderName);
    }

    /*
    Creates then Start the Threads nearly simultaneously
     */
    public void createAndStartTest(){
        pickedRouteList = new ArrayList<>();        // list of Integers representing the picked Example Route
        Random rand = new Random();
        int routeChoice;                                    // number of the example Request

        ExecutorService executorService = Executors.newFixedThreadPool(ThreadPool);
        System.out.println("Creating all Threads");
            for(int i = 0; i<AmountOfThreads; i++) {
                routeChoice = rand.nextInt(10);
                executorService.execute(new RoutingThread(i+1,new RoutingRequest(testingRequests.get(routeChoice)),facade_class));   //creates an executes the RoutingThreads
                pickedRouteList.add(routeChoice);
            }

        //Preparing the Shutdown of the ExecutorService
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        routingEnd = LocalTime.now();           //Timestamp --> for the end of the Routing

        checkHowOftenWhichRoute2(pickedRouteList);      //for analysing
        getTimes();                                     //for analysing
    }

    /*
    Prints a List which shows how often a Route is picked during the Test
     */
    public void checkHowOftenWhichRoute2(ArrayList<Integer> pickedRouteList){
        int route1 = 0;
        int route2 = 0;
        int route3 = 0;
        int route4 = 0;
        int route5 = 0;
        int route6 = 0;
        int route7 = 0;
        int route8 = 0;
        int route9 = 0;
        int route10 = 0;
        int error = 0;

        for (Integer i: pickedRouteList) {
            switch (i){
                case  0 : route1++; break;
                case  1 : route2++; break;
                case  2 : route3++; break;
                case  3 : route4++; break;
                case  4 : route5++; break;
                case  5 : route6++; break;
                case  6 : route7++; break;
                case  7 : route8++; break;
                case  8 : route9++; break;
                case  9 : route10++; break;
                default: error++; break;
            }
        }

        System.out.println("Printing how often each Route picked in this test");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Routing Request 1 picked: " + route1 +" times");
        System.out.println("Routing Request 2 picked: " + route2 +" times");
        System.out.println("Routing Request 3 picked: " + route3 +" times");
        System.out.println("Routing Request 4 picked: " + route4 +" times");
        System.out.println("Routing Request 5 picked: " + route5 +" times");
        System.out.println("Routing Request 6 picked: " + route6 +" times");
        System.out.println("Routing Request 7 picked: " + route7 +" times");
        System.out.println("Routing Request 8 picked: " + route8 +" times");
        System.out.println("Routing Request 9 picked: " + route9 +" times");
        System.out.println("Routing Request 10 picked: " + route10 +" times");
        System.out.println("Errors occurred: " + error + " times");
        System.out.println("--------------------------------------------------------------------------------");
    }

    public void getTimes(){
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Preparation time --> Loading a graph and creating the test routes");
        System.out.println("Routing time --> The time which the Programm needs to finish all thread requests");
        System.out.println("Completion time --> The time the programm runs for this test");
        System.out.println();
        System.out.println("Times Format is:  Minutes : Seconds . Milliseconds");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("--------------------------------------------------------------------------------");
        printTime("Preparation time: ",Duration.between(startTime,prepEnd));
        printTime("Routing time:     ",Duration.between(routingStart,routingEnd));
        printTime("Completion time:  ",Duration.between(startTime,LocalTime.now()));
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println(LocalTime.now());
    }

    public void printTime(String Time,Duration duration){
        Double seconds = ((double)duration.toMillis())/1000;

        int min;
        int sec;
        int milli;

        milli = (int) ((seconds * 1000) % 1000);
        min = (int)(seconds/60);
        sec = (seconds.intValue()) - (min*60);

        System.out.println(Time + min + ":" + sec + "." + milli);
    }
}
//--------------------------------------- Getter & Setter ---------------------------------------//
//----------------------------------------- Additional ------------------------------------------//





