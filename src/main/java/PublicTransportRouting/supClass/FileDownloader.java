package PublicTransportRouting.supClass;

import org.apache.commons.io.FilenameUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * Class to download Files which then can be used to create Graphs
 * Queries and so on.
 *
 */
public class FileDownloader {
    //------------------------------------------ Variable -------------------------------------------//
    private static FilenameUtils filenameUtils;

    //----------------------------------------- Constructor -----------------------------------------//
    public FileDownloader(){
    }

    //------------------------------------------- Methods -------------------------------------------//
    /**
     * Method to download Osm files and stores them in resources/OSM_Files
     *
     * @param URL the path to the file form the internet as an String
     * @throws MalformedURLException if the given String isn´t an URL
     */
    public static void downloadOsmFile(String URL) throws MalformedURLException {
        System.out.println("Downloading Osm File from: "+URL);
        URL url = new URL(URL);
        String fileName = filenameUtils.getName(url.getPath());             //to get the name of the file so it can be stored correctly

        try {
            ReadableByteChannel readChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOS = new FileOutputStream("src\\main\\resources\\OSM_Files\\"+fileName);
            FileChannel writeChannel = fileOS.getChannel();
            writeChannel.transferFrom(readChannel,0,Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Download complete");
    }

    /**
     * Method to download Gtfs files and stores them in resources/GTFS_Files
     *
     * @param URL the path to the file form the internet as an String
     * @throws MalformedURLException if the given String isn´t an URL
     */
    public static void downloadGtfsFile(String URL) throws MalformedURLException {
        System.out.println("Downloading Gtfs File from: "+URL);
        URL url = new URL(URL);
        String fileName = filenameUtils.getName(url.getPath());             //to get the name of the file so it can be stored correctly

        try {
            ReadableByteChannel readChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOS = new FileOutputStream("src\\main\\resources\\GTFS_Files\\"+fileName);
            FileChannel writeChannel = fileOS.getChannel();
            writeChannel.transferFrom(readChannel,0,Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Download complete");
    }
    //--------------------------------------- Getter & Setter ---------------------------------------//
    //----------------------------------------- Additional ------------------------------------------//
    /*
    -----------------------------------------------------------------------------------------------------------
    TEST FILES:
    OSM download from --> https://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf
    GTFS download from --> http://transitfeeds.com/p/verkehrsverbund-berlin-brandenburg/213/latest/download
                           https://www.vbb.de/media/download/2029/GTFS.zip
     -----------------------------------------------------------------------------------------------------------
     */
}
