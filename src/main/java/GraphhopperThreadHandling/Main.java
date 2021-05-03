package GraphhopperThreadHandling;

import GraphhopperThreadHandling.Logs.ThreadLogHandler;
import GraphhopperThreadHandling.model.ExampleRoutingRequests;
import GraphhopperThreadHandling.model.RoutingRequest;
import publicTransportRouting.service.PT_Facade_Class;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main Class --> Start Point of the Testing Process
 * Unter Settings you can change the variables to adjust the testing settings
 */
//TODO: Hier noch schauen das Routen (Namen dieser) etwas anders gespeichert werden damit nicht nur max 10 Routen gespeichert werden.
public class Main {
    //------------------------------------------ Settings -------------------------------------------//
    int AmountOfThreads = 1000;           //TODO: Dann testen mit 100/1tsd/10tsd/etc.
    int AmountOfFacadeClasses = 2;      //Defines the Amount of Facade Classes, only works if testingType = ManyFacades
    String testingType = "Referenz";       //Alternativen sind "Referenz" , "ManyFacades" , "Clone" , "ExecutorService"

    String ZoneId = "Europe/Berlin";
    int simulationYear = 2020;
    int simulationMonth = 8;
    int simulationDay = 6;
    int simulationHour = 7;
    int simulationMin = 0;
    String fileFormat = "JSON";     //[Graphhopper only]
    String graphFolderName = "berlin-latest.osm.pbf_with_Transit";  //[Graphhopper only]

    //------------------------------------------ Variable -------------------------------------------//
    public ArrayList<RoutingThread> threadList;         //List of all threads created
    public ArrayList<RoutingRequest> testingRequests;   //List of all Requests created for testing --- in ExampleRoutingRequests.class

    static PT_Facade_Class facade_class;         //the PT_Facade_Class used to set the ones in the Threads / which then is used for Routing Methods [Graphhopper only]
    public static PT_Facade_Class[] facadeList;  //the list of the different loaded Facade Classes [Graphhopper only]

    //For Logging and to comprehend testing results
    ThreadLogHandler logHandler;

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
        ThreadLogHandler.programmStartTime = LocalTime.now();   //timestamp for the ProgrammStart

        Main main = new Main();

        LocalTime preparationStart = LocalTime.now();   //timestamp for the PreparationStart

        main.GraphhopperHandling(); //loads a Graph an initialises the PT_FacadeClass [Graphhopper only]
        main.testingRequests = new ExampleRoutingRequests().getTestRequest();   //creates the testingRequests and sets them
        main.createThreads(main.AmountOfThreads,facade_class);        //creates all Threads an initialises them

//        main.checkThreads();          //Lists all Threads with their Number
//        main.checkThreadContent();    //Lists all Threads with their Content (Routing Requests etc.)
        main.checkHowOftenWhichRoute();

        LocalTime preparationEnd = LocalTime.now(); //timestamp for the PreparationEnd

        //Create a log Handler to comprehend results later
        main.logHandler = new ThreadLogHandler(main.AmountOfThreads);
        ThreadLogHandler.calculatePreparationTime(preparationStart,preparationEnd);

        if(main.testingType == "ExecutorService"){
            main.createAndStartTest();    //Test Methode
        } else {
            main.startThreads();        //starts all threads nearly simultaneously
        }
    }

    //----------------------------------------- Constructor -----------------------------------------//
    //------------------------------------------- Methods -------------------------------------------//
    /*
    Initialises The Facade Class an loads the Graph
     */
    private void GraphhopperHandling(){
        if (testingType == "ManyFacades"){
            facadeList = new PT_Facade_Class[AmountOfFacadeClasses];

            for(int i=0;i<AmountOfFacadeClasses;i++){
                PT_Facade_Class extra_facade_class = new PT_Facade_Class(ZoneId,simulationYear,simulationMonth,simulationDay,simulationHour,simulationMin,fileFormat);
                extra_facade_class.loadGraph(graphFolderName);
                facadeList[i] = extra_facade_class;

                System.out.println("Created PT_Facade_Class " + extra_facade_class.getClass());
            }
            System.out.println("Creation of " + AmountOfFacadeClasses + " Facade Classes .... Done ");
        }else {
            facade_class = new PT_Facade_Class(ZoneId,simulationYear,simulationMonth,simulationDay,simulationHour,simulationMin,fileFormat);

            if (testingType == "Clone"){
                facade_class.loadHopper(graphFolderName);
            }else if (testingType == "Referenz"){
                facade_class.loadGraph(graphFolderName);
            }
        }
    }

    /*
    Creates all threads an initialises them
     */
    private void createThreads(int NumberOfThreads, PT_Facade_Class facade_class){
        Random rand = new Random();             //creating a Random Object to know which test Reqeust to choose
        threadList = new ArrayList<>();         //Erstellen der threadList um die Threads zu speichern

        //Prints a threadList to show which Threads are created
        System.out.println("Starting to create all Threads");
        System.out.println("Testing Type for this run is " + testingType);

        if (testingType == "Referenz"){
            for (int i=1; i<NumberOfThreads+1; i++){
                threadList.add(new RoutingThread(i,testingRequests,facade_class,rand.nextInt(10)));
                //System.out.println("Created Thread Nr." + i);
            }
        }else if (testingType == "Clone"){
            for (int i=1; i<NumberOfThreads+1; i++){
                try {
                    threadList.add(new RoutingThread(i,testingRequests,(PT_Facade_Class)this.facade_class.clone(),rand.nextInt(10)));
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                //System.out.println("Created Thread Nr." + i);
            }
        }else if (testingType == "ManyFacades"){
            threadList = new ArrayList<>();         //Erstellen der threadList um die Threads zu speichern
            int threadNr = 0;

            for (int i=1; i<AmountOfThreads+1; i++){
                if (threadNr >= AmountOfFacadeClasses){
                    threadNr = 0;
                }
                threadList.add(new RoutingThread(i,testingRequests,facadeList[threadNr],rand.nextInt(10)));
                threadNr++;

                //System.out.println("Created Thread Nr." + i);
            }
        }

        System.out.println("Created: " + NumberOfThreads + " Threads and added those to the a Thread List");
    }

    /*
    Starts all threads nearly simultaneously
     */
    private void startThreads(){
        System.out.println("Starting all Threads");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");

        logHandler.setRoutingStartTime(LocalTime.now()); //timestamp for the routingStart

        for (RoutingThread rt: threadList) {
            rt.thread.start();
        }

        //Sleep of main Thread to match the println (For Fashion :)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    /*
    Test Methods for the Threads
    Prints out all Threads which their Number (shows which threads are created
     */
    public void checkThreads(){
        System.out.println(" <Listing all created Threads of the threadList> ");
        System.out.println("-------------------------------------------------");
        for (RoutingThread rt: threadList) {
            System.out.println("Thread Nr. " + rt.getThreadNumber());
        }
        System.out.println("-------------------------------------------------");
        System.out.println("End of the Thread List");
    }

    /*
    Test Methods for the Threads Content
     */
    public void checkThreadContent(){
        System.out.println(" <Listing all created Threads of the threadList and their content> ");
        System.out.println("-------------------------------------------------------------------");
        for (RoutingThread rt: threadList) {
            System.out.println("Thread Nr. " + rt.getThreadNumber());
            System.out.println("--------------------------------------------");
            System.out.println(rt.getRoutingName());
            System.out.println("FROM: " + rt.testRequest.getFrom().getLat()+","+rt.testRequest.getFrom().getLon() + " |" +
                               " TO: " + rt.testRequest.getTo().getLat()+","+rt.testRequest.getTo().getLon());
            System.out.println("QUERYTIME: " + rt.testRequest.getQueryTime());
            System.out.println("ROUTESELECTION: " + rt.testRequest.getRouteSelection());    //[Graphhopper only]
            System.out.println("--------------------------------------------");
        }
        System.out.println("-------------------------------------------------------------------");
        System.out.println("End of the Thread List content");
    }

    /*
    Test Method to know which Route is picked by the Test and how often this route is resembled
     */
    public void checkHowOftenWhichRoute(){
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

        for (RoutingThread rt: threadList) {
            switch (rt.getRoutingName()){
                case "VON: Hako Ramen Prenzlauer Berg (Nahe Berlin Mitte) / NACH: U Rathaus Schöneberg (Nahe Rudolph-Wilde-Park)" : route1++; break;
                case "VON: Buch (Berliner Ort) / NACH: Brandenburger Tor (Sehenswürdigkeit)" : route2++; break;
                case "VON: Berlin Südkreuz / NACH: Denkmal" : route2++; break;
                case "VON: Neustadt (Berliner Ort) - Penny / NACH: SensCity Hotel (Berlin / Spandau)" : route3++; break;
                case "VON: Bezirk Neukölln (Berlin Stadtteil) / NACH: Brandenburger Tor (Sehenswürdigkeit)" : route4++; break;
                case "VON: The Student Hotel Berlin (Berlin Mitte) / NACH: Charité – Universitätsmedizin Berlin" : route5++; break;
                case "VON: Wohngebiet innerhalb Spandau / NACH: Spandau Hbf (Bezirk Spandau)" : route6++; break;
                case "VON: Hotel Forsthaus (Rande von Berlin / Wannesee) / NACH: Brandenburger Tor (Sehenswürdigkeit)" : route7++; break;
                case "VON: Berlin Mahlsdorf / NACH: Bezirk Hellersdorf" : route7++; break;
                case "VON: Bahnhof Berlin Friedrichstraße (Berlin Mitte) / NACH: Zehlendorf (Berliner Ort)" : route8++; break;
                case "VON: Wohnort nahe Berlin Mitte / NACH: Wohnort Bezirk Neukölln" : route9++; break;
                case "VON: Schönleinstr. Berlin (U-Bahn) / NACH: Burger King Mitte Berlin" : route9++; break;
                case "VON: Brandenburger Tor (Sehenswürdigkeit) / NACH: Computerspielemuseum (Berlin)" : route10++; break;
                default: error++; break;
            }
        }
        System.out.println("Printing how often each Route picked in this test");
        System.out.println("------------------------------------------------");
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
        System.out.println("------------------------------------------------");
    }

    /*
    Vorgeschlagene Alternative zur eigenen Methode zum starten der Threads
    keine großen erkennbaren unterschiede, deshalb wird diese Methode während der Tests nicht verwendet !!
    Aber für Info behalten
     */
    public void createAndStartTest(){
        Random rand = new Random();
        ExecutorService executorService = Executors.newFixedThreadPool(AmountOfThreads);

            for(int i = 0; i<AmountOfThreads; i++) {
                int finalI = i;
                executorService.execute(new RoutingThread(i+1,testingRequests,facade_class,rand.nextInt(10)));
            }

            executorService.shutdown();

//            while(true){
//                try {
//                    Thread.sleep(500l);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
    }
}
//--------------------------------------- Getter & Setter ---------------------------------------//
//----------------------------------------- Additional ------------------------------------------//





