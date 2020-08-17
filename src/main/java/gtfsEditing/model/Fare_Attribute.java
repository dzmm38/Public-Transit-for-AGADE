package gtfsEditing.model;

import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.FareAttribute;

/**
 * Class which represents a FareAttribute form OneBusAway
 * with an extra Method, getContentString
 */
public class Fare_Attribute {
    //------------------------------------------ Variable -------------------------------------------//
    private FareAttribute fareAttribute;    //FareAttribute from the oneBusAway Modul so it´s not needed
                                            //to write it again. (Had to make a new because extends was not available

    //----------------------------------------- Constructor -----------------------------------------//

    /**
     * Constructor
     */
    public Fare_Attribute(){
        fareAttribute = new FareAttribute();
    }

    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method to create a String with which the OneBusAway Modul can then be
     * create a transformation for the gtfs Data
     *
     * @return String an in certain Structure which represents all necessary information
     *         about the FareAttribute
     */
    public String getContentString(){
        String transformationString;
        transformationString = "{'file':'fare_attributes.txt'," +
                "'fare_id':'"+          (getFare_Id().getId())  +"',"+
                "'price':'"+            String.valueOf(getPrice())            +"',"+
                "'currency_type':'"+    getCurrencyType()                     +"',"+
                "'payment_method':'"+   String.valueOf(getPaymentMethod())    +"',";

        if (getTransfers() != -1){      //Enum Transfers.UNLIMITED has the value -1 an can be left empty
            transformationString = transformationString +
                    "'transfers':'"  +String.valueOf(getTransfers()) +"',";
            if (getTransfers() == 0){       //transfer Duration only can be given if the transfers are 0
                if (getTransferDuration() != 0)     //if the transfer duration is 0 then there is no transfer Duration and thus can be left out
                transformationString = transformationString +
                        "'transfer_duration':'"+ String.valueOf(getTransferDuration())+"'";
            }
        }
        return transformationString;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public AgencyAndId getFare_Id(){
        return fareAttribute.getId();
    }

    public float getPrice(){
        return fareAttribute.getPrice();
    }

    public String getCurrencyType(){
        return fareAttribute.getCurrencyType();
    }

    public int getPaymentMethod(){
        return fareAttribute.getPaymentMethod();
    }

    public int getTransfers(){
        return fareAttribute.getTransfers();
    }

    public int getTransferDuration(){
        return fareAttribute.getTransferDuration();
    }

    public void setFare_Id(AgencyAndId fare_id){
        fareAttribute.setId(fare_id);
    }

    public void setPrice(float price){
        fareAttribute.setPrice(price);
    }

    public void setCurrencyType(String currencyType){
        fareAttribute.setCurrencyType(currencyType);
    }

    public void setPaymentMethod(int paymentMethod){
        fareAttribute.setPaymentMethod(paymentMethod);
    }

    public void setTransfers(int transfers){
        fareAttribute.setTransfers(transfers);
    }

    public void setTransferDuration(int transferDuration){
        fareAttribute.setTransferDuration(transferDuration);
    }

    public FareAttribute getFareAttribute() {
        return fareAttribute;
    }

    public void setFareAttribute(FareAttribute fareAttribute) {
        this.fareAttribute = fareAttribute;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
