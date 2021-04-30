package GraphhopperThreadHandling.Logs;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Class to Log the Testing Threads with some parameters to check on performance and
 * time distribution
 * <p>
 * Represents the Top Class of the LogFiles
 */
public class CompleteTestLog {
    //------------------------------------------ Variable -------------------------------------------//
    private double preparationTime;         //Time which is needed to do all preparations (containing graph loading and creating of threads and example Requests)
    private double completeRoutingTime;     //Added time for all Routing of every Request
    private double averageThreadTime;       //Average time a Routing Request needs (over all requests done in the test)
    private double programmRunningTime;     //Complete Time the Programm needs to determine
    private ThreadLog[] logs;           //List of logs of each thread containing additional parameters

    //----------------------------------------- Constructor -----------------------------------------//
    public CompleteTestLog() {
        //For JSON
    }

    /**
     * Normal Constructor to use if using the CompleteTestLog Class
     *
     * @param threadNr number of threads created and used for the test
     */
    public CompleteTestLog(int threadNr) {
        this.logs = new ThreadLog[threadNr];
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method to calculate each running time of an thread / RoutingRequest
     * Also calculates the average time a thread needs for completing a request
     */
    public void calculateRunningTimes() {
        for (ThreadLog log : logs) {
            log.calculateRunningTime();     //to calculate the time each thread needs for an request
        }
        calculateAverageThreadTime();   //to calculate the average time a threads needs for an request
    }

    /**
     * Method to calculate the average time of an thread / Routing Request
     * Adds up all times and then dived them by the amount of threads
     */
    private void calculateAverageThreadTime() {
        double addedTime = 0;

        //adds up each runningTime of an log
        for (ThreadLog log : logs) {
            addedTime += log.getRunningTimeInSeconds();
        }

        this.averageThreadTime = addedTime / logs.length;
    }

    /**
     * Method to calculate the complete time which is needed to handle all request and their Routing
     *
     * @param start LocalTime representing the time at which the first threads starts it´s routing
     * @param end   LocalTime representing the time at which the last thread is done routing
     */
    public void calculateCompleteRoutingTime(LocalTime start, LocalTime end) {
        Duration runningTime = Duration.between(start, end);     //calculates the difference between the start and end time as an Duration object
        double sec = ((double) runningTime.toMillis()) / 1000;     //divides the milliseconds given from the duration Object with 1000 to get seconds
        completeRoutingTime = sec;
    }

    /**
     * Method to calculate the time the Programm needs for Preparation (loading the Graph and creating Threads and testing data)
     *
     * @param preparationStart LocalTime representing the time at which the preparation starts
     * @param preparationEnd   LocalTime representing the time at which the preparation ends
     */
    public void calculatePreparationTime(LocalTime preparationStart, LocalTime preparationEnd) {
        Duration preparationTime = Duration.between(preparationStart, preparationEnd);       //see calculateCompleteRoutingTime Method
        double sec = ((double) preparationTime.toMillis()) / 1000;
        this.preparationTime = sec;
    }

    /**
     * Method to calculate the time the Programm is running (all Functions expect the writing process of the File)
     *
     * @param programmStart LocalTime representing the time the Programm is started
     * @param programmEnd   LocalTime representing the time the Programm is determined
     */
    public void calculateProgrammRunningTime(LocalTime programmStart, LocalTime programmEnd) {
        Duration programmRunningTime = Duration.between(programmStart, programmEnd);
        double sec = ((double) programmRunningTime.toMillis()) / 1000;
        this.programmRunningTime = sec;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public ThreadLog[] getLogs() {
        return logs;
    }

    public void setLogs(ThreadLog[] logs) {
        this.logs = logs;
    }

    public double getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(double preparationTime) {
        this.preparationTime = preparationTime;
    }

    public double getCompleteRoutingTime() {
        return completeRoutingTime;
    }

    public void setCompleteRoutingTime(double completeRoutingTime) {
        this.completeRoutingTime = completeRoutingTime;
    }

    public double getAverageThreadTime() {
        return averageThreadTime;
    }

    public void setAverageThreadTime(double averageThreadTime) {
        this.averageThreadTime = averageThreadTime;
    }

    public double getProgrammRunningTime() {
        return programmRunningTime;
    }

    public void setProgrammRunningTime(double programmRunningTime) {
        this.programmRunningTime = programmRunningTime;
    }
}
//----------------------------------------- Additional ------------------------------------------//