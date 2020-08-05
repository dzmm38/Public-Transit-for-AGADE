package PublicTransportRouting.Time;

import java.time.LocalTime;

/**
 * Class to calculate some time format changes and tick calculations
 *
 * Contains a timeConverter to do these calculation
 * Some Advanced Methods for calculations with ticks and times
 */
public class TimeCalculator {
    //------------------------------------------ Variable -------------------------------------------//
    private TimeConverter timeConverter;
    private static int tickZero;

    //----------------------------------------- Constructor -----------------------------------------//
    public TimeCalculator() {
        timeConverter = new TimeConverter();
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method to convert a Time in LocalTime format to a double value
     *
     * @param time Time which should be converted
     * @return the time in a double value which represents only hours and minutes
     */
    public double convertLocalTimeToTime(LocalTime time){
        double hour = time.getHour();
        double min = time.getMinute();

        return (hour + (min / 100));
    }

    /*
    Private because after the calculator gets initialized the startTime should not be changed
    Uses a LocalTime startTime to set The time at which the simulation starts (time to which the others can be
    compared to, to get the Tick of the simulation
    Sets the TickZero
     */
    public void calculateTickZero(LocalTime startTime) {

        double tickZero_Time;
        tickZero_Time = convertLocalTimeToTime(startTime);

        double result;
        result = timeConverter.hours_to_min(timeConverter.Time_To_DecimalTime(tickZero_Time));        //the time needs to be in Minutes because 1 Tick = 1 Min

        System.out.println("TickZero is set to : " + result + " Minutes of the Day");
        this.tickZero = (int) result;           //result should no longer have decimals so to set to TickZero it needs to be casted to int
    }

    /**
     * Method to convert a LocalTime to an int which represents
     * a tick of the AGADE simulation cycle
     *
     * @param time LocalTime which should be transformed in an tick
     * @return a tick (represents a minute) as an int value
     */
    public int convertTimeToTick(LocalTime time){            //MUSS NOCH GESCHAUT WERDEN OB DAS SO FUNKTIONIERT WIE ICH MIR DAS VORGESTELLT HABE
        int tick;
        double hour;
        double min;
        double result;
        hour = time.getHour();
        min = time.getMinute();
        min = min/100;
        result = hour+min;
        result = timeConverter.Time_To_DecimalTime(result);
        tick = timeConverter.hours_to_min(result);
        return tick;
    }

    /**
     * Method which calculates the difference between between the start Time and
     * the given time.
     *
     * @param tick Tick of the day (Time of the day in minutes) which should converted to an
     *             simulation Tick
     * @return int value, represents the simulation cycle in which the time is reached
     */
    public int tickAfterStart(int tick) {
        return tick - tickZero;
    }

    public int calculateSimulationTick(LocalTime time) {
        return tickAfterStart(convertTimeToTick(time));
    }

    public int calculateSimulationTick(String time) {
        return tickAfterStart(convertTimeToTick(LocalTime.parse(time)));
    }


    //--------------------------------------- Getter & Setter ---------------------------------------//
    public int getTickZero_Tick() {
        return tickZero;
    }

    //Here to get TickZero back in double it first needs to be transformed form tick to time format
    public double getTickZero_Time() {
        double result;
        result = timeConverter.DecimalTime_To_Time(timeConverter.min_to_hours(tickZero));
        return result;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
