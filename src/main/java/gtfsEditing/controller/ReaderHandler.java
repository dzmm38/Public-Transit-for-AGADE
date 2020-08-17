package gtfsEditing.controller;


import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.impl.GtfsRelationalDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsReader;
import java.io.File;
import java.io.IOException;

/**
 * Class to Read the gtfs Data and
 * then saves it in an GtfsDaoImpl Object on which the
 * Data can be accessed
 */
public class ReaderHandler {
    //------------------------------------------ Variable -------------------------------------------//
    private GtfsReader reader;
    private File gtfsFile;
    private GtfsDaoImpl gtfsFeed;   //Data structure from which the loaded Data can be accessed

    //----------------------------------------- Constructor -----------------------------------------//

    /**
     * To initialise the ReaderHandler
     *
     * @param gtfsFilePath String which represents the path to the gtfs File which should be read
     */
    public ReaderHandler(String gtfsFilePath){
        this.gtfsFile = new File(gtfsFilePath);

        reader = new GtfsReader();
        gtfsFeed = new GtfsDaoImpl();
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method wich initialises and starts the Reading Process
     * then closes the reader afterwards.
     *
     * @return GtfsDaoImpl - a Data structure which allows access to the loaded gtfs Data
     */
    public GtfsDaoImpl startReading() {
        try {
            reader.setEntityStore(gtfsFeed);
            reader.setInputLocation(gtfsFile);
            reader.run();                       //starts the actual reading
            reader.close();                     //closes the reader cause no longer needed
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getGtfsFeed();
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public File getGtfsFile() {
        return gtfsFile;
    }

    public void setGtfsFile(File gtfsFile) {
        this.gtfsFile = gtfsFile;
    }

    public GtfsDaoImpl getGtfsFeed() {
        return gtfsFeed;
    }

    public void setGtfsFeed(GtfsRelationalDaoImpl gtfsFeed) {
        this.gtfsFeed = gtfsFeed;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
