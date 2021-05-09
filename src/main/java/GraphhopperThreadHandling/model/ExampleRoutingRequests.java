package GraphhopperThreadHandling.model;

import publicTransportRouting.model.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Class to create some testsRequest which can be used for Testing / Benchmarking
 * Requests are created by creating this class
 */
public class ExampleRoutingRequests {
    //------------------------------------------ Variable -------------------------------------------//
    public ArrayList<RoutingRequest> testingRequests;   //ArrayList which saves the requests

    //----------------------------------------- Constructor -----------------------------------------//
    public ExampleRoutingRequests(){
        testingRequests = new ArrayList<RoutingRequest>(); //Creating the Array List containing the Requests for the Testing purposes
        createExampleRoutingRequests();
    }

    //------------------------------------------- Methods -------------------------------------------//
    private void createExampleRoutingRequests(){
        System.out.println("Starting to create all Testing Requests");

        //Initialize the needed Variables to create the Requests for the Tests
        Location from;                  //Start Location of an Request
        Location to;                    //Target Location of an Request
        LocalDateTime queryTime;        //Time for which the Request is set to happen
        String routeSelection = "all";  //Selection to set the Routing (for testing always "all") [Graphhopper only]

        String routingName;         //Name describing the respective Request

        //-------------- Creating the Routing Request 1 (Relativ Lang / einmal quer durch Berlin Mitte) --------------//
        from = new Location(52.538662,13.409295);                                 //Hako Ramen Prenzlauer Berg (Nahe Berlin Mitte)
        to = new Location(52.483604,13.341451);                                   //U Rathaus Schöneberg (Nahe Rudolph-Wilde-Park)
        queryTime = LocalDateTime.of(2020,8,6,12,5);    //12:05
        routingName = "VON: Hako Ramen Prenzlauer Berg (Nahe Berlin Mitte) / NACH: U Rathaus Schöneberg (Nahe Rudolph-Wilde-Park)";
        RoutingRequest r1 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 1 created");

//        //------------------- Creating the Routing Request 2 (Lang / Berliner Ort nach Berlin Mitte ------------------//
//        from = new Location(52.639556,13.494802);                                 //Buch (Berliner Ort)
//        to = new Location(52.516423,13.378749);                                   //Brandenburger Tor (Sehenswürdigkeit)
//        queryTime = LocalDateTime.of(2020,8,6,8,0);     //8:00
//        routingName = "VON: Buch (Berliner Ort) / NACH: Brandenburger Tor (Sehenswürdigkeit)";
//        RoutingRequest r2 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 2 created");

        //------------------- Alternative Creating the Routing Request 2 (VON: Berlin Südkreuz / NACH: Denkmal) ------------------//
        from = new Location(52.475446,13.365343);                                 //Berlin Südkreuz
        to = new Location(52.514127,13.379614);                                   //Denkmal
        queryTime = LocalDateTime.of(2020,8,6,8,0);     //8:00
        routingName = "VON: Berlin Südkreuz / NACH: Denkmal";
        RoutingRequest r2 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 2 created");

        //-------------- Creating the Routing Request 3 (Realtiv kurz / Berliner Ort zu nahem anderem) ---------------//
        from = new Location(52.547906,13.193984);                                 //Neustadt (Berliner Ort) - Penny
        to = new Location(52.529015,13.210079);                                   //SensCity Hotel (Berlin / Spandau)
        queryTime = LocalDateTime.of(2020,8,6,8,30);    //8:30
        routingName = "VON: Neustadt (Berliner Ort) - Penny / NACH: SensCity Hotel (Berlin / Spandau)";
        RoutingRequest r3 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 3 created");

        //----------------- Creating the Routing Request 4 (Mittel von außerhalb nach Berlin Mitte) ------------------//
        from = new Location(52.429779,13.447502);                                 //Bezirk Neukölln (Berlin Stadtteil)
        to = new Location(52.516423,13.378749);                                   //Brandenburger Tor (Sehenswürdigkeit)
        queryTime = LocalDateTime.of(2020,8,6,9,0);     //9:00
        routingName = "VON: Bezirk Neukölln (Berlin Stadtteil) / NACH: Brandenburger Tor (Sehenswürdigkeit)";
        RoutingRequest r4 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 4 created");

//        //----------------- Creating the Routing Request 4 (Lang Ort zu Ort) ------------------//
//        from = new Location(52.431662, 13.193653);                                 //Zehlendorf
//        to = new Location(52.440872, 13.440159);                                   //Wesenberger Ring
//        queryTime = LocalDateTime.of(2020,8,6,13,15);   //13:15
//        routingName = "VON: Zehlendorf / NACH: Wesenberger Ring";
//        RoutingRequest r4 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 4 created");

//        //------------------------- Creating the Routing Request 4 (Mittlere Strecke) --------------------------------//
//        from = new Location(52.632995,13.509852);                                 //Berlin mitte
//        to = new Location(52.517805,13.442522);                                   //Golfresort
//        queryTime = LocalDateTime.of(2020,8,6,12,15);   //12:15
//        routingName = "VON: Berlin mitte / NACH: Golfresort";
//        RoutingRequest r4 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 4 created");

        //------------------------ Creating the Routing Request 5 (Relativ kurz Berlin Mitte) ------------------------//
        from = new Location(52.516998,13.417763);                                 //The Student Hotel Berlin (Berlin Mitte)
        to = new Location(52.526551,13.378090);                                   //Charité – Universitätsmedizin Berlin
        queryTime = LocalDateTime.of(2020,8,6,10,0);    //10:00
        routingName = "VON: The Student Hotel Berlin (Berlin Mitte) / NACH: Charité – Universitätsmedizin Berlin";
        RoutingRequest r5 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 5 created");

//        //------------------ Creating the Routing Request 6 (Relativ kurz innerhalb Bezirk Spandau) ------------------//
//        from = new Location(52.527813,13.174053);                                 //Wohngebiet innerhalb Spandau
//        to = new Location(52.534352,13.197452);                                   //Spandau Hbf (Bezirk Spandau)
//        queryTime = LocalDateTime.of(2020,8,6,11,35);   //11:35
//        routingName = "VON: Wohngebiet innerhalb Spandau / NACH: Spandau Hbf (Bezirk Spandau)";
//        RoutingRequest r6 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 6 created");

        //------------------------- Creating the Routing Request 6 (Mittlere Strecke) --------------------------------//
        from = new Location(52.440791,13.444667);                                 //Bezirk Neukölln
        to = new Location(52.437963,13.346589);                                   //Lankwitz
        queryTime = LocalDateTime.of(2020,8,6,18,00);   //18:00
        routingName = "VON: Bezirk Neukölln / NACH: Lankwitz";
        RoutingRequest r6 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 6 created");

//        //----------------------- Creating the Routing Request 7 (Kurz / Ziemlich direkter Weg) ----------------------//
//        from = new Location(52.405735,13.140910);                                 //Hotel Forsthaus (Rande von Berlin / Wannesee)
//        to = new Location(52.516423,13.378749);                                   //Brandenburger Tor (Sehenswürdigkeit)
//        queryTime = LocalDateTime.of(2020,8,6,15,48);   //15:48
//        routingName = "VON: Hotel Forsthaus (Rande von Berlin / Wannesee) / NACH: Brandenburger Tor (Sehenswürdigkeit)";
//        RoutingRequest r7 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 7 created");

        //----------------------- Alternative Creating the Routing Request 7 (VON: Berlin Mahlsdorf / NACH: Bezirk Hellersdorf) ----------------------//
        from = new Location(52.512281,13.611387);                                 //Berlin Mahlsdorf
        to = new Location(52.525540,13.535886);                                   //Bezirk Hellersdorf
        queryTime = LocalDateTime.of(2020,8,6,15,48);   //15:48
        routingName = "VON: Berlin Mahlsdorf / NACH: Bezirk Hellersdorf";
        RoutingRequest r7 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 7 created");

//        //------------------ Creating the Routing Request 8 (kurze Stecke von Ort zu naheliegendem) ------------------//
//        from = new Location(52.520344,13.387177);                                 //Bahnhof Berlin Friedrichstraße (Berlin Mitte)
//        to = new Location(52.429641,13.273599);                                   //Zehlendorf (Berliner Ort)
//        queryTime = LocalDateTime.of(2020,8,6,18,20);   //18:20
//        routingName = "VON: Bahnhof Berlin Friedrichstraße (Berlin Mitte) / NACH: Zehlendorf (Berliner Ort)";
//        RoutingRequest r8 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 8 created");

//        //------------------ Creating the Routing Request 8 (kurze Stecke von Ort zu naheliegendem) ------------------//
//        from = new Location(52.520344,13.387177);                                 //Bahnhof Berlin Friedrichstraße (Berlin Mitte)
//        to = new Location(52.491520, 13.339576);                                   //Schöneberg
//        queryTime = LocalDateTime.of(2020,8,6,18,20);   //18:20
//        routingName = "VON: Bahnhof Berlin Friedrichstraße (Berlin Mitte) / NACH: Schöneberg (Berliner Ort)";
//        RoutingRequest r8 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 8 created");

        //------------------------- Creating the Routing Request 8 (Mittlere Strecke) --------------------------------//
        from = new Location(52.434861, 13.260825);                                 //Zehlendorf
        to = new Location(52.480684, 13.263863);                                   //Grünwald
        queryTime = LocalDateTime.of(2020,8,6,14,15);   //14:15
        routingName = "VON: Zehlendorf / NACH: Grünwald";
        RoutingRequest r8 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 8 created");

//        //------------------ Creating the Routing Request 9 (Mittel Miite nach etwas weiter außen) -------------------//
//        from = new Location(52.512236,13.467226);                                 //Wohnort nahe Berlin Mitte
//        to = new Location(52.428394,13.440891);                                   //Wohnort Bezirk Neukölln
//        queryTime = LocalDateTime.of(2020,8,6,13,45);   //13:45
//        routingName = "VON: Wohnort nahe Berlin Mitte / NACH: Wohnort Bezirk Neukölln";
//        RoutingRequest r9 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 9 created");

//        //------------------ Alternative Creating the Routing Request 9 (Schönleinstr. Berlin (U-Bahn) /Burger King Mitte Berlin) -------------------//
//        from = new Location(52.493042,13.422109);                                 //Schönleinstr. Berlin (U-Bahn)
//        to = new Location(52.520193,13.387457);                                   //Burger King Mitte Berlin
//        queryTime = LocalDateTime.of(2020,8,6,13,45);   //13:45
//        routingName = "VON: Schönleinstr. Berlin (U-Bahn) / NACH: Burger King Mitte Berlin";
//        RoutingRequest r9 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
//        System.out.println("Test Request 9 created");

        //------------------------- Creating the Routing Request 9 (Mittlere Lange Stecke) --------------------------------//
        from = new Location(52.596076,13.335111);                                 //Wittenau
        to = new Location(52.525929, 13.367809);                                   //Berlin HbF
        queryTime = LocalDateTime.of(2020,8,6,10,25);   //10:25
        routingName = "VON: Wittenau / NACH: Berlin HbF";
        RoutingRequest r9 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 9 created");

        //-------------- Creating the Routing Request 10 (Mittlere Strecke / relativ mittig in Berlin) ---------------//
        from = new Location(52.516423,13.378749);                                 //Brandenburger Tor (Sehenswürdigkeit)
        to = new Location(52.517805,13.442522);                                   //Computerspielemuseum (Berlin)
        queryTime = LocalDateTime.of(2020,8,6,10,25);   //10:25
        routingName = "VON: Brandenburger Tor (Sehenswürdigkeit) / NACH: Computerspielemuseum (Berlin)";
        RoutingRequest r10 = new RoutingRequest(from,to,queryTime,routeSelection,routingName);
        System.out.println("Test Request 10 created");

        //Adding all the created Requests to the Test List
        testingRequests.add(r1);
        testingRequests.add(r2);
        testingRequests.add(r3);
        testingRequests.add(r4);
        testingRequests.add(r5);
        testingRequests.add(r6);
        testingRequests.add(r7);
        testingRequests.add(r8);
        testingRequests.add(r9);
        testingRequests.add(r10);

        System.out.println("Created all Testing Reqeusts");
    }

    //--------------------------------------- Getter & Setter ---------------------------------------//
    public ArrayList getTestRequest(){
        return testingRequests;
    }
}
    //----------------------------------------- Additional ------------------------------------------//
