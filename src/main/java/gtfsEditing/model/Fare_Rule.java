package gtfsEditing.model;

/**
 * Class to represent a Fare Rule in the Gtfs Data
 */
public class Fare_Rule {
    //------------------------------------------ Variable -------------------------------------------//
    private String fare_id;             //Id of an Fare Attribute which the rule should be assigned to
    private String route_id;            //Id of an Rute to Rule should be assigned to
    private String origin_id;           //Id of an origin Zone id (given in stops.zoneid) the Rule should be assigned
    private String destination_id;      //Id of an destination Zone id (given in stops.zoneid) the Rule should be assigned
    private String contains_id;         //Id of an passing through Zone id (given in stops.zoneid) the Rule should be assigned

    //----------------------------------------- Constructor -----------------------------------------//
    //------------------------------------------- Methods -------------------------------------------//

    /**
     * Method to create a String with which the OneBusAway Modul can then be
     * create a transformation for the gtfs Data
     *
     * @return String an in certain Structure which represents all necessary information
     *         about the Fare Rule
     */
    public String getContentString(){
        StringBuilder builder = new StringBuilder();

        /*
        Because not all Attributes of Fare Rules are needed for an specific option they can be left
        empty or represent an empty String if they are not set
         */
        builder.append("{'file':'fare_rules.txt','fare_id':'"+getFare_id()+"'");

        if (route_id != null){
            builder.append(",'route_id':'"+getRoute_id()+"'");
        }else {
            builder.append(",'route_id':''");
        }
        if (origin_id != null){
            builder.append(",'origin_id':'"+getOrigin_id()+"'");
        }else {
            builder.append(",'origin_id':''");
        }
        if (destination_id != null){
            builder.append(",'destination_id':'"+getDestination_id()+"'");
        }else {
            builder.append(",'destination_id':''");
        }
        if (contains_id != null){
            builder.append(",'contains_id':'"+getContains_id()+"'");
        }else {
            builder.append(",'contains_id':''");
        }

        return builder.toString();
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public String getFare_id() {
        return fare_id;
    }

    public void setFare_id(String fare_id) {
        this.fare_id = fare_id;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(String origin_id) {
        this.origin_id = origin_id;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public String getContains_id() {
        return contains_id;
    }

    public void setContains_id(String contains_id) {
        this.contains_id = contains_id;
    }

    //----------------------------------------- Additional ------------------------------------------//
}
