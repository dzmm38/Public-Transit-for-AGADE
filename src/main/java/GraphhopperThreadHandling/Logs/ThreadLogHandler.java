package GraphhopperThreadHandling.Logs;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

/**
 * Class to Handle the LogFiles created during testing
 * Handels calculating some values as well as writing the Logs as an JSON File
 * So it can be analysed later
 */
public class ThreadLogHandler {
    //------------------------------------------ Variable -------------------------------------------//
    private static CompleteTestLog testLog;     //the LogObject which will be written as a File Containing all Information
    private static int threadCounter;
    private static int maxThreads;

    private static LocalTime routingStartTime;  //time at which routing starts
    private static LocalTime routingEndTime;    //time at which routing is done

    public static LocalTime programmStartTime;  //time at which programm starts
    public static LocalTime programmEndTime;    //time at which the programm ends

    //----------------------------------------- Constructor -----------------------------------------//

    /**
     * Constructor which is used to initialise the ThreadLogHandler
     *
     * @param threadNumber int representing the amount of threads used in the Test
     */
    public ThreadLogHandler(int threadNumber) {
        this.testLog = new CompleteTestLog(threadNumber);
        this.threadCounter = 0;
        this.maxThreads = threadNumber;
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method to add a ThreadLog to the Log Array
     * if its the first Log it starts the time needed for the completeRouting Time measure
     * if its the last thread (thread X) then the routing time ends and the Logs are closed and needed values are calculated as well as writing the LogFile
     *
     * @param log A ThreadLog Object which should be added to the Log Array
     */
    public static void addLog(ThreadLog log) {
        testLog.getLogs()[threadCounter] = log;
        threadCounter++;

        if (threadCounter == maxThreads) {
            routingEndTime = LocalTime.now();   //timestamp for the done routing
            //calculating necessary values needed for the LogFile
            testLog.calculateRunningTimes();
            testLog.calculateCompleteRoutingTime(routingStartTime, routingEndTime);
            programmEndTime = LocalTime.now();  //timestamp for the done programm
            calculateProgrammRunningTime();

            printLogs();    //writing the Log as an JSON File in Routes
        }
    }

    /**
     * Method to write the Logs as a JSON File
     * (src/main/resources/Logs)
     */
    private static void printLogs() {
        String extraFileName = "_with_" + maxThreads + "_Threads";

        ObjectMapper om = new ObjectMapper(new JsonFactory());
        om.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            om.writeValue(new File("src\\main\\resources\\Logs\\Log" + extraFileName + ".json"), testLog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to calculate the Preparation Time for the Test
     *
     * @param preparationStart LocalTime representing the time the Preparation work starts
     * @param preparationEnd   LocalTime representing the time the preparation work is done
     */
    public static void calculatePreparationTime(LocalTime preparationStart, LocalTime preparationEnd) {
        testLog.calculatePreparationTime(preparationStart, preparationEnd);
    }

    /**
     * Method to calculate the Time the Programm is running for the Test
     */
    public static void calculateProgrammRunningTime() {
        testLog.calculateProgrammRunningTime(programmStartTime, programmEndTime);
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public void setRoutingStartTime(LocalTime routingStartTime) {
        this.routingStartTime = routingStartTime;
    }
}
//----------------------------------------- Additional ------------------------------------------//
