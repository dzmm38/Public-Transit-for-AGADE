package gtfsEditing.service;

import gtfsEditing.controller.FareController;
import gtfsEditing.controller.ReaderHandler;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;

/**
 * Facade class for the FareController
 * Contains all necessary Methods to create and Fare Attributes and Fare Rules
 * for an Gtfs Feed
 */
public class EditorFacade {
    //------------------------------------------ Variable -------------------------------------------//
    private String inputFile;
    private String outputFile;

    private FareController fareController;          //to edit the Fare Files
    private GtfsDaoImpl gtfsFeed;

    //----------------------------------------- Constructor -----------------------------------------//

    /**
     * Constructor to create and initialise a Facade Class
     * Reads gtfs Data with an ReaderHandler
     * Creats and adds an FareController to the Facade
     *
     * @param inputFile the Gtfs.zip file which should be edited (path to the file as a String)
     * @param outputFile the file where the changed gtfs File should be saved (path)
     */
    public EditorFacade(String inputFile, String outputFile){
        this.inputFile = inputFile;
        this.outputFile = outputFile;

        fareController = new FareController(inputFile,outputFile);      //input an output file because it can either be the same file or two different
        ReaderHandler readerHandler = new ReaderHandler(inputFile);
        gtfsFeed = readerHandler.startReading();
    }

    /**
     * Constructor to create and initialise a Facade Class
     * Reads gtfs Data with an ReaderHandler
     * Creats and adds an FareController to the Facade
     *
     * @param inAndOut_File the gtfs File which should edited an the changes then overwrite the old one
     */
    public EditorFacade(String inAndOut_File){
        this.inputFile = inAndOut_File;
        this.outputFile = inAndOut_File;

        fareController = new FareController(inputFile,outputFile);      //input an output file because it can either be the same file or two different
        ReaderHandler readerHandler = new ReaderHandler(inputFile);
        gtfsFeed = readerHandler.startReading();
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method to apply all added Fare Changes to the gtfs data
     * then safes it to the given output File
     */
    public void applyFareChanges(){
        fareController.getTransformerHandler().startTransformation();   //start the transformation for the Fare Files
    }
    //--------------------------------------- Getter & Setter ---------------------------------------//

    /**
     * Method which returns the Fare Controller on which then the changes can be made
     *
     * @return the FareController of this Facade to make changes
     */
    public FareController getFareController(){
        return fareController;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public GtfsDaoImpl getGtfsFeed() {
        return gtfsFeed;
    }

    public void setGtfsFeed(GtfsDaoImpl gtfsFeed) {
        this.gtfsFeed = gtfsFeed;
    }
    //----------------------------------------- Additional ------------------------------------------//
}
