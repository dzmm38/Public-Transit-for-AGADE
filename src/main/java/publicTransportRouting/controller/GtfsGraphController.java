package publicTransportRouting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.graphhopper.GraphHopperConfig;
import com.graphhopper.gtfs.GraphHopperGtfs;
import com.graphhopper.gtfs.GtfsStorage;
import com.graphhopper.gtfs.PtRouter;
import com.graphhopper.gtfs.PtRouterImpl;
import com.graphhopper.storage.index.LocationIndex;
import com.graphhopper.util.TranslationMap;

import java.io.File;
import java.io.IOException;

/**
 * Class to create or load a graph (time expanded) for public transit routing.
 * Contains Method createGraph which is static and loadGraph
 */
public class GtfsGraphController {
    //------------------------------------------ Variable -------------------------------------------//
    //----------------------------------------- Constructor -----------------------------------------//
    public GtfsGraphController() {
        //Nothing to initialize because this class is only for the functions
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method to create a graph an store it in the graph folder in resources
     *
     * @param OsmFile Name of the file for the map, street and walk network
     *                (have to be located in OSM_Files folder in resources)
     * @param GtfsFile Name of the file which represents the public transit aspect
     *                (have to be located in the GTFS_Files folder in resources)
     */
    public static void createGraph(String OsmFile, String GtfsFile){
        //Sets the information for the config File which is needed to create the Graph

        //In graphopper their is no extra Encoder for public transit instead it uses "foot" which includes foot an public transit if a gtfs File is given
        String flagEncoder = "foot";
        String osmFile = "src\\main\\resources\\OSM_Files\\"+OsmFile;
        String graphLocation = "src\\main\\resources\\graph\\"+OsmFile+"_with_Transit";
        String gtfsFile = "src\\main\\resources\\GTFS_Files\\"+GtfsFile;

        //Creating a new config File with the set variables
        GraphHopperConfig config = createConfig(flagEncoder,osmFile,graphLocation,gtfsFile);

        System.out.println("Create Graph ..........");
        //Creating a hopper explicit for gtfs which then creates and saves the time expanded graph
        GraphHopperGtfs hopper = new GraphHopperGtfs(config);
        hopper.init(config);
        //After creating the hopper an graph is closed properly
        hopper.importAndClose();

        //Creates a tansitionConfig which is then saved as a .yml File properly for later recalls
        System.out.println("Save Config File ..........");
        TransitionConfigHandler transitionConfigHandler = new TransitionConfigHandler(config.getString("graph.flag_encoders", ""), config.getString("datareader.file", ""),
                                                              config.getString("graph.location",""),config.getString("gtfs.file",""));

        //Creating an Object Mapper and YAML Factory to save the transitionConfigHandler as a file in the associated graph folder
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            om.writeValue(new File(graphLocation + "\\" + "config_pt.yml"), transitionConfigHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to load an exiting Graph from an file name
     *
     * @param graphFolderName Name of the folder in which the graph is located
     *                        (have to be located in the graphs folder in resources)
     * @return PTRouteResource Object which contains the graph an can be routed on
     */
    public PtRouter loadGraph(String graphFolderName){
        System.out.println("Loading Graph ..........");

        GraphHopperConfig config;
        config = loadConfig(graphFolderName);           //Load the config of an existing graph to load the graph again

        GraphHopperGtfs hopper = new GraphHopperGtfs(config);

        hopper.init(config);
//        hopper.forDesktop();
//        hopper.setInMemory();
//        hopper.setStoreOnFlush(true);
        hopper.load(config.getString("graph.location",config.toString()));         //loading the graph from the location given in the config file

        //Creating some Objects which are needed to then create the PtRouteResource
        LocationIndex index = hopper.getLocationIndex();
        GtfsStorage storage = hopper.getGtfsStorage();
        PtRouter ptRouter = PtRouterImpl.createFactory(new TranslationMap().doImport(),hopper,index,storage).createWithoutRealtimeFeed();

        return ptRouter;
    }

    /**
     * Method to load an exiting Graph from an file name
     *
     * @param graphFolderName Name of the folder in which the graph is located
     *                        (have to be located in the graphs folder in resources)
     * @return GraphHopperGtfs a hopper Object which contains the Graph -- can be used to create a PtRouteRessource
     */
    public GraphHopperGtfs loadHopperForGraph(String graphFolderName){
        System.out.println("Loading Graph ..........");

        GraphHopperConfig config;
        config = loadConfig(graphFolderName);           //Load the config of an existing graph to load the graph again

        GraphHopperGtfs hopper = new GraphHopperGtfs(config);

        hopper.init(config);
        hopper.load(config.getString("graph.location",config.toString()));         //loading the graph from the location given in the config file

        System.out.println("Graph loaded");
        return hopper;
    }

    /*
    Creating a Config and set some attributes with the given parameter
    so the graph can be created
    Returning the finished Config file
     */
    private static GraphHopperConfig createConfig(String flag_encoder, String datareader_file, String graph_Location, String gtfs_file){
        System.out.println("Creating Config File ..........");

        GraphHopperConfig config = new GraphHopperConfig();

        config.putObject("graph.flag_encoders",flag_encoder);
        config.putObject("datareader.file",datareader_file);
        config.putObject("graph.location",graph_Location+"\\graph");
        config.putObject("gtfs.file",gtfs_file);

        return config;
    }

    /*
    Config File is loaded from an existing File and is then returned
    Needs a String "grapFolderName" to locate the config to that graph (grapFolderName = Name of the Folder that contains this graph)
    Returns then a GraphhopperConfig
     */
    private GraphHopperConfig loadConfig(String graphFolderName) {
        System.out.println("Loading Config File from: " + graphFolderName + " ..........");

        GraphHopperConfig config;

        String ConfigPath = "src\\main\\resources\\graph\\" + graphFolderName + "\\config_pt.yml";    //creating the exact path where the config is located
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        try {
            config = om.readValue(new File(ConfigPath), TransitionConfigHandler.class).switchConfigs();        //switchConfigs to create a GraphopperConfig file
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return config;
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    //----------------------------------------- Additional ------------------------------------------//
}
