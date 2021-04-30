package GraphhopperThreadHandling.Logs;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Class representing Log information for each Thread used for testing
 * containing necessary parameters for analysing
 */
public class ThreadLog {
    //------------------------------------------ Variable -------------------------------------------//
    private int threadNr;                   //Number of the thread of the Log Object
    private String threadName;              //Name describing the RoutingRequest
    private LocalTime threadStart;          //Time at which the Thread is starting
    private LocalTime threadEnd;            //Time at which the Thread is stopped
    private LocalTime runningTime;          //duration how long the thread was operating
    private double runningTimeInSeconds;    //duration of how long the thread was operating in seconds + millis

    //----------------------------------------- Constructor -----------------------------------------//
    public ThreadLog() {
        //For JSON
    }

    /**
     * Normal Constructor which should be used for working with the ThreadLog Class
     *
     * @param threadStart LocalTime representing at which time the thread started
     * @param threadNr    int representing the threads Number of creation
     * @param threadName  String representing the name of the thread (normally it´s an describing of the RoutingRequest)
     */
    public ThreadLog(LocalTime threadStart, int threadNr, String threadName) {
        this.threadStart = threadStart;
        this.threadNr = threadNr;
        this.threadName = threadName;
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method which calculates the active time of how long the thread was operating
     * for the runningTime Variable and also for the runningTimeInSeconds Variable
     */
    public void calculateRunningTime() {
        Duration test = Duration.between(threadStart, threadEnd);

        /*
        Here the separate values of the duration are cut of and stored separately
        to then create another LocalTime object
        After each step the cut of values are subtracted of the whole duration so the next can be cut off
        without complications
         */
        int hour = (int) test.toHours();
        test = test.minusHours(hour);
        int min = (int) test.toMinutes();
        test = test.minusMinutes(min);
        int sec = (int) test.getSeconds();
        test = test.minusSeconds(sec);
        int nano = (int) test.toNanos();

        runningTime = LocalTime.of(hour, min, sec, nano);

        /*
        Here the separate values of the time are transformed into seconds so it can be added
        and displayed all at seconds
         */
        double hourForSec = runningTime.getHour() * 60 * 60;
        double minForSec = runningTime.getMinute() * 60;
        double secForSec = runningTime.getSecond();
        double nanoForSec = ((double) runningTime.getNano()) / 1000000000;

        //Cutting of the 4-X decimals so the max representing time would be milliseconds
        runningTimeInSeconds = hourForSec + minForSec + secForSec + nanoForSec;
        runningTimeInSeconds = (int) (runningTimeInSeconds * 1000);
        runningTimeInSeconds = runningTimeInSeconds / 1000;
    }

    /**
     * Method to Print start and end time as well as the running time of the ThreadLog
     * Mainly for Testing
     */
    public void printAll() {
        System.out.println("");
        System.out.println("Thread Start Time   : " + threadStart);
        System.out.println("Thread End Time     : " + threadEnd);
        System.out.println("----------------------------------");
        System.out.println("Thread running Time : " + runningTime);
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public int getThreadNr() {
        return threadNr;
    }

    public void setThreadNr(int threadNr) {
        this.threadNr = threadNr;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getThreadStart() {
        return threadStart.getHour() + ":" + threadStart.getMinute() + ":" + threadStart.getSecond() + "." + threadStart.getNano() / 1000000; //return as String so in the LogFile (JSON) its clearer
    }

    public void setThreadStart(LocalTime threadStart) {
        this.threadStart = threadStart;
    }

    public void setThreadEnd(LocalTime threadEnd) {
        this.threadEnd = threadEnd;
    }

    public String getThreadEnd() {
        return threadEnd.getHour() + ":" + threadEnd.getMinute() + ":" + threadEnd.getSecond() + "." + threadEnd.getNano() / 1000000;     //return as String so in the LogFile (JSON) its clearer
    }

    public String getRunningTime() {
        return runningTime.getHour() + ":" + runningTime.getMinute() + ":" + runningTime.getSecond() + "." + runningTime.getNano() / 1000000; //return as String so in the LogFile (JSON) its clearer
    }

    public void setRunningTime(LocalTime runningTime) {
        this.runningTime = runningTime;
    }

    public double getRunningTimeInSeconds() {
        return runningTimeInSeconds;
    }

    public void setRunningTimeInSeconds(double runningTimeInSeconds) {
        this.runningTimeInSeconds = runningTimeInSeconds;
    }
}
//----------------------------------------- Additional ------------------------------------------//