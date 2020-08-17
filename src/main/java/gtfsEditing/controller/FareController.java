package gtfsEditing.controller;

import gtfsEditing.model.Fare_Attribute;
import gtfsEditing.model.Fare_Rule;
import gtfsEditing.model.PaymentMethod;
import gtfsEditing.model.Transfers;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Route;
import org.onebusaway.gtfs.model.Stop;

import java.util.ArrayList;

/**
 * Class with which FareAttributes and FareRules can be created and defined
 * Contains Methods to create and define different types of Attributes and Rules
 */
public class FareController {
    //------------------------------------------ Variable -------------------------------------------//
    private ArrayList<Fare_Attribute> attributes;   //to get access to the attributes which aren´t created yet

    private TransformerHandler transformerHandler;

    //----------------------------------------- Constructor -----------------------------------------//

    /**
     * Constructor to initialise an FareEditor
     *
     * @param gtfsFilePath String which defines the path to the gtfs File which should be changed
     * @param outputFilePath String which defines the path and Name of the changed File
     *                       (path can also be equal to the gtfsFilePath then no new File would be created
     *                       and the existing one overwritten)
     */
    public FareController(String gtfsFilePath, String outputFilePath){
        attributes = new ArrayList<>();
        transformerHandler = new TransformerHandler(gtfsFilePath,outputFilePath);   //creates an transformerHandler and Transformer for the FareEditor to use
    }

    //------------------------------------------- Methods -------------------------------------------//
    /*
    Area wehre all kinds of Options of an Fare_Attribute can be created
     */

    /**
     * Method to create an Fare Attribute and add it to
     * the transformer Modification queue (without transfer duration)
     *
     * @param fare_id String to define the Attribute (has to be unique)
     * @param price float to represents the price for the ride
     * @param currencyType String which represents to Currency (EUR , USD , etc.)
     * @param paymentMethod Enum to represent the two different payment Models
     * @param transfers Enum which defines the Transfer Type model
     */
    public void createAttribute(String fare_id, float price, String currencyType, PaymentMethod paymentMethod, Transfers transfers){
        Fare_Attribute attribute = new Fare_Attribute();
        attribute.setTransfers(transfers.getCode());
        universalAttribute(fare_id,price,currencyType,paymentMethod,attribute);
    }

    /**
     * Method to create an Fare Attribute and add it to
     * the transformer Modification queue
     *
     * @param fare_id String to define the Attribute (has to be unique)
     * @param price float to represents the price for the ride
     * @param currencyType String which represents to Currency (EUR , USD , etc.)
     * @param paymentMethod Enum to represent the two different payment Models
     * @param transferDuration int which defines the validity of an ticket
     */
    public void createAttribute_with_TransferDuration(String fare_id,float price,String currencyType,PaymentMethod paymentMethod,int transferDuration){
        Fare_Attribute attribute = new Fare_Attribute();

        attribute.setTransfers(Transfers.NO_TRANSFERS.getCode());   //NO_TRANSFERS because the transferDuration is given in the parameters (can only be added if transfers = NO_TRANSFERS)
        attribute.setTransferDuration(transferDuration);

        universalAttribute(fare_id,price,currencyType,paymentMethod,attribute);
    }

    /*
    Method which every Attribute Methode calls to do the universal stuff which has to be done
    by every option of the different Attribute Methods
    Also adds the created Attribute to the attributes ArrayList to get access when creating Rules
    and also adds it to the TransformHandler
     */
    private void universalAttribute(String fare_id,float price,String currencyType,PaymentMethod paymentMethod, Fare_Attribute attribute){
        AgencyAndId agencyAndId = AgencyAndId.convertFromString(fare_id);

        //All initialisations which have to be done for every Attribute
        attribute.setFare_Id(agencyAndId);
        attribute.setPrice(price);
        attribute.setCurrencyType(currencyType);
        attribute.setPaymentMethod(paymentMethod.getCode());

        //TransformationString structure is needed to make to Transformation in OneBusAway GtfsTransformer
        //here the given base Structure is given an getContentSting() then adds the content to be written
        String transformationString = "{'op':'add','obj':"+ attribute.getContentString() + "}}";
        transformerHandler.addTransformation(transformationString); //adds the Transformation String to the Handler to be transformed
        attributes.add(attribute);
    }

    /*
    Area wehre all kinds of Options of an Fare_Rule can be created
     */
    /**
     * Method to create a Fare Rule
     * FareRule Option with an RouteId
     *
     * @param attribute an Fare_Attribute which are set to the specific Rule
     * @param route the Route which should be assigned to the Fare_Attribute
     */
    public void addRule_with_RouteId(Fare_Attribute attribute,Route route){
        Fare_Rule rule = new Fare_Rule();
        rule.setRoute_id(route.getId().getId());
        universalRule(attribute,rule);
    }

    /**
     * Method to create a Fare Rule
     * FareRule Option with an originZoneId from an Stop
     *
     * @param attribute an Fare_Attribute which are set to the specific Rule
     * @param originStop an Stop of an Zone form wich the originZoneId should  be get from
     */
    public void addRule_with_OriginId(Fare_Attribute attribute, Stop originStop){
        Fare_Rule rule = new Fare_Rule();
        rule.setOrigin_id(originStop.getZoneId()); //zoneId because of the structure form the gtfs Data Format
        universalRule(attribute,rule);
    }

    /**
     * Method to create a Fare Rule
     * FareRule Option with an destinationZoneId from a given stop
     *
     * @param attribute an Fare_Attribute which are set to the specific Rule
     * @param destinationStop an Stop of an Zone form wich the destinationZoneId should  be get from
     */
    public void addRule_with_DestinationId(Fare_Attribute attribute,Stop destinationStop){
        Fare_Rule rule = new Fare_Rule();
        rule.setDestination_id(destinationStop.getZoneId()); //zoneId because of the structure form the gtfs Data Format
        universalRule(attribute,rule);
    }

    /**
     * Method to create a Fare Rule
     * FareRule Option with an containsStop
     *
     * @param attribute an Fare_Attribute which are set to the specific Rule
     * @param containsStop Identifies the zones that a rider will enter while using a given fare class
     */
    public void addRule_with_ContainsId(Fare_Attribute attribute, Stop containsStop){
        Fare_Rule rule = new Fare_Rule();
        rule.setContains_id(containsStop.getZoneId());
        universalRule(attribute,rule);
    }

    /**
     * Method to create a Fare Rule
     * FareRule Option with a originZoneId from an Stop and
     * a destinationZoneId from a given stop
     *
     * @param attribute an Fare_Attribute which are set to the specific Rule
     * @param originStop an Stop of an Zone form wich the originZoneId should  be get from
     * @param destinationStop an Stop of an Zone form wich the destinationZoneId should  be get from
     */
    public void addRule_with_OriginId_DestinationId(Fare_Attribute attribute,Stop originStop,Stop destinationStop){
        Fare_Rule rule = new Fare_Rule();
        rule.setOrigin_id(originStop.getZoneId());
        rule.setDestination_id(destinationStop.getZoneId());
        universalRule(attribute,rule);
    }

    /*
    Method which is called by every addRule Method
    Contains some steps which are needed by every addRule.....
    also add the created Rule to the TransformHandler
     */
    private void universalRule(Fare_Attribute attribute, Fare_Rule rule){
        rule.setFare_id(attribute.getFare_Id().getId());

        //TransformationString structure is needed to make to Transformation in OneBusAway GtfsTransformer
        //here the given base Structure is given an getContentSting() then adds the content to be written
        String transformationString = "{'op':'add','obj':"+ rule.getContentString() + "}}";
        transformerHandler.addTransformation(transformationString); //adds the Transformation String to the Handler to be transformed
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public ArrayList<Fare_Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<Fare_Attribute> attributes) {
        this.attributes = attributes;
    }

    public TransformerHandler getTransformerHandler() {
        return transformerHandler;
    }

    public void setTransformerHandler(TransformerHandler transformerHandler) {
        this.transformerHandler = transformerHandler;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
