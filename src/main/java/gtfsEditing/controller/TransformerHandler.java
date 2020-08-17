package gtfsEditing.controller;

import org.onebusaway.gtfs_transformer.GtfsTransformer;
import org.onebusaway.gtfs_transformer.TransformSpecificationException;
import org.onebusaway.gtfs_transformer.factory.TransformFactory;
import java.io.File;
import java.io.IOException;

/**
 * Class which handels the Transformations / Changes on the Gtfs Data
 * and saves it at an output File
 */
public class TransformerHandler {
    //------------------------------------------ Variable -------------------------------------------//
    private GtfsTransformer transformer;
    private TransformFactory factory;

    private File inputFile;     //Gtfs File
    private File outputFile;    //Gtfs File which should be overwritten or new File path to save changed File

    //----------------------------------------- Constructor -----------------------------------------//

    /**
     * Constructor to setup the TransformerHandler
     * creating Transformer and TransformFactory and add it to this.
     *
     * @param inputFile gtfsFile which should be changed
     * @param outputFile File wich should be overwritten or new File to save
     */
    public TransformerHandler(String inputFile,String outputFile){
        this.inputFile = new File(inputFile);
        this.outputFile = new File(outputFile);

        transformer = new GtfsTransformer();
        transformer.setGtfsInputDirectory(this.inputFile);  //setting input file to the transformer
        transformer.setOutputDirectory(this.outputFile);    //setting outputfile to the transformer
        factory = new TransformFactory(transformer);        //setting TransformFacotry to be able to do changes then setting Transformer to the factory
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method to add single Transformations / Changes to the Transformer and store them
     * until the Transformation happens (startTransformation)
     *
     * @param transformationString String in an special Structure needed to make changes using the OneBusAway Modul
     */
    public void addTransformation(String transformationString){
        try {
            transformer.getTransformFactory().addModificationsFromString(transformationString); //adds the change to the Transformer
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformSpecificationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which starts the Transformation with all added Modifications form addTransformation
     * After finished Transformation then saves the all as an .zip File
     */
    public void startTransformation(){
        try {
            transformer.run();      //does the Transformation of all Modification and the Storing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public GtfsTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(GtfsTransformer transformer) {
        this.transformer = transformer;
    }

    public TransformFactory getFactory() {
        return factory;
    }

    public void setFactory(TransformFactory factory) {
        this.factory = factory;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
