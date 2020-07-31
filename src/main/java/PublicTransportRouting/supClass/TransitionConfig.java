package PublicTransportRouting.supClass;

import com.graphhopper.GraphHopperConfig;

/**
 * Class to replace the GraphhopperConfig Class to write and save the config
 * Data which is needed to reload a graph
 *
 * Has all important information and a Method to switch between this Config and
 * the GraphhopperCongig
 */
public class TransitionConfig {
    //------------------------------------------ Variable -------------------------------------------//
    public String graph_flag_encoders;
    public String datareader_file;
    public String graph_Location;
    public String gtfs_file;

    //----------------------------------------- Constructor -----------------------------------------//
    public TransitionConfig(){
        //only to create a TransitionConfig from a YAML File
    }

    public TransitionConfig(String graph_flag_encoders, String datareader_file, String graph_Location, String gtfs_file){
        this.graph_flag_encoders = graph_flag_encoders;
        this.datareader_file = datareader_file;
        this.graph_Location = graph_Location;
        this.gtfs_file = gtfs_file;
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method to switch form a TransitionConfig class to a GraphhopperConfig class
     * takes all attributes an creates an GraphhopperConfig Object
     *
     * @return GraphhopperConfig with all necessary information to load the graph
     */
    public GraphHopperConfig switchConfigs(){
        GraphHopperConfig config = new GraphHopperConfig();

        config.putObject("graph.flag_encoders",graph_flag_encoders);
        config.putObject("datareader.file",datareader_file);
        config.putObject("graph.location",graph_Location);
        config.putObject("gtfs.file",gtfs_file);

        return config;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public String getGraph_flag_encoders() {
        return graph_flag_encoders;
    }

    public void setGraph_flag_encoders(String graph_flag_encoders) {
        this.graph_flag_encoders = graph_flag_encoders;
    }

    public String getDatareader_file() {
        return datareader_file;
    }

    public void setDatareader_file(String datareader_file) {
        this.datareader_file = datareader_file;
    }

    public String getGraph_Location() {
        return graph_Location;
    }

    public void setGraph_Location(String graph_Location) {
        this.graph_Location = graph_Location;
    }

    public String getGtfs_file() {
        return gtfs_file;
    }

    public void setGtfs_file(String gtfs_file) {
        this.gtfs_file = gtfs_file;
    }

    //----------------------------------------- Additional ------------------------------------------//
}


