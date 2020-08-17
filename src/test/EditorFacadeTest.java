import gtfsEditing.model.Fare_Attribute;
import gtfsEditing.model.PaymentMethod;
import gtfsEditing.model.Transfers;
import gtfsEditing.service.EditorFacade;
import org.junit.Test;
import org.onebusaway.gtfs.model.Route;
import java.util.ArrayList;

public class EditorFacadeTest {

    /**
     * The test uses the already existing gtfs.zip File (Berlin) and adds two Fare Attributes and to every Route
     * either 1 or 2
     * (Pretty simple use only for testing)
     */
    @Test
    public void doTest() {
        //First set one ore two FilePaths which the will be used during the Transform process
        String gtfsFile = "src\\main\\resources\\GTFS_Files\\gtfs.zip";             //File Path to the gtfs Data which should be changed
        String outputFile = "src\\main\\resources\\GTFS_Files\\gtfs_withFares.zip"; //File Path to either also the gtfs Data to overwrite it or to create a new File to save the data with the changes

        //Now create an Facade class which then creates an controller added to the facade to use
        //Also reads in the gtfs Data and stores it an an gtfsFeed to have access to the data
        EditorFacade editorFacade = new EditorFacade(gtfsFile, outputFile);

        //Here two Attributes are created / using the FareController of the Facade an then uses its Methods
        //Because of the AgencyAndID Class of OneBusAway Modul the fare_id needs an "_" before the actual Id
        editorFacade.getFareController().createAttribute("_1", 1.75f, "EUR", PaymentMethod.PAY_ON_BOARD, Transfers.UNLIMITED);
        editorFacade.getFareController().createAttribute("_2", 2.50f, "EUR", PaymentMethod.PAY_ON_BOARD, Transfers.UNLIMITED);

        //To have easier access to the just now created attributes get the arrayList and store it locally here
        ArrayList<Fare_Attribute> fares = editorFacade.getFareController().getAttributes();

        //as an test here are created one have oft the Rules with Attribute 1 and the other one with 2
        int zaehler = 0;
        for (Route route : editorFacade.getGtfsFeed().getAllRoutes()) {
            if (zaehler < editorFacade.getGtfsFeed().getAllRoutes().size() / 2) {
                //Here an Rule is defined / using the FareController of the Facade an then uses its Methods, the rule uses only the fare and the route (Id´s)
                editorFacade.getFareController().addRule_with_RouteId(fares.get(0), route);
            } else {
                editorFacade.getFareController().addRule_with_RouteId(fares.get(1), route);
            }
            zaehler++;
        }
        editorFacade.applyFareChanges();    //apply all the changes which are defined above and apply these to the gtfs Data
    }
}
