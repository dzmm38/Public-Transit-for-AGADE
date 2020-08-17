# Public-Transport-Routing
Module for Public Transit for the AGADE System using Graphhopper

Um das Modul nutzen zu können, wird hierbei nur die **PT_Facade_Class** benutzt.<br>
Da es sich bei dem Modul
um eines handelt, welches nach dem Facade Pattern gebaut
wurde. <br>

In dieser Klasse finden sich dann alle wichtigen Methoden zum Erstellen
eines Graphen und um Multimodale Queries auf einem der erstellen Graphen 
durchzuführen. <br>

##### 1. Laden der zu benutzenden Daten.
    PT_Facade_Class.downloadFiles(String OsmFile_URL, String GtfsFile_URL)
    
        - Methode ist static
        - Speichertort = resources/OSM_Files oder resources/GTFS_Files
        - Benutzt Klasse FileDownloader
    
##### 2. Erstellen des Graphen.
    PT_Facade_Class.createGraph(String OsmDateiName,String GtfsDateiName)
    
        - Methode ist static
        - Benutzt Klasse PT_Graph
        - Speichert Graph und dazugehörige Config Datei in resources/graph
    
##### 3. Einen Graphen für zukünftige Abfragen laden.
    PT_Facade_Class pt = new PT_Facade_Class(String ZoneId,int Jahr,int Monat,int Tag,int Std,int Min,String Speicherformat)
    pt.loadGraph(String GraphOrdnerName)
    
        - zuerst muss ein Object der Facade Klasse erzeugt und initialisiert werden 
          mit ZoneId, Tag etc.
        - Dabei wird ein Object von PtRouteResource erstellt un gesetzt auf welchem
          die Abfragen stattfinden.
        - Nutzt auch die Klase PT_Graph
        
##### 4. Eine Abfrage erstellen, um dann eine Route zu erhalten.
    pt.ptRouteQuery(Location from,Location to,LocalDateTime zeitDerAbfrage,String bestOrAll)
    
        - Abfrage von einer Location zur nächsten zu einer bestimmten zeit und 
          Rückgabe aller oder einer Routen.
        - Routen werden dann in resources/Routes als eine JSON Datei erstellt 
          (pro Abfrage eine JSON).
        - Nutzt so gut wie alle andern Klassen.
        
Gibt auch eine Testklasse, FacadeTest, welche all dies einmal durchgeht und eine Route beispielsweise wiedergibt.<br>
URL´s zum testen:   OSM --> https://download.geofabrik.de/europe/germany/berlin-latest.osm.pbf <br>
URL´s zum testen:   GTFS -> https://www.vbb.de/media/download/2029/gtfs-vbb.zip
 
      
###Kurze Zusammenfassung aller Klassen: 

1. <ins>PT_Graph:</ins>
    * zum Erstellen und laden eines Graphen und der PtRouteResource
    * sowie speichern / erstellen / und laden der zugehörigen Config dateien
2. <ins>Routemodel</ins>
    * repräsentiert die Route und besteht aus mehreren Klassen
    * **PT_Route** = oberste Klasse des Models, beinhaltet liste von Legs und Stops sowie <br>
    einige Entscheidungskriterien und einfache Methoden zum Abfragen des nächsten Stops gegeben eines aktuellen.
    * **PT_Leg** = stellt Teil der Route da welche mit selben Fahrzeug zurückgelegt wird, <br>
    beinhaltet u.a. liste von stops des Legs.
    * **PT_Stop** = stellt eine Haltestelle mit Ankunfts- und Abfahrzeiten sowie der Location dar
3. <ins>PT_Query</ins>
    * erstellt einen Request und initialisiert diesen mit einschränkungen,<br>
    welcher dann an Graphhopper zum Routing übergeben werden kann.
4. <ins>PT_Route_Finder</ins>
    * gibt eine Anfrage an die PtRouteResource und damit Graphhopper weiter.
    * wandelt somit eine Anfrage in einen path um.
    * wandelt dann erstellte Route (nicht path) in entweder eine YAML oder JSON Datei um.
5. <ins>PT_Route_Builder</ins>
    * nimmt den erstellten path und bildet daraus dann eine Route mit 
      zugrundeliegendem Routemodel
6. <ins>FileDownloader</ins>
    * läd Osm und Gtfs Daten herunter (anhand von URL´s) und speichert diese
7. <ins>Location</ins>
    * Location Klasse von AGADE
8. <ins>TransitionConfig</ins>
    * um Config richtig als YAML speichern zu könnnen.
    * erstellt aus dieser die richtige GraphhopperConfig
9. <ins>RouteLoader</ins>
    * läd eine existierende JSON oder YAML Route ein.
10. <ins>TimeCalculator</ins>
    * Für umwandlung der Zeiten in Ticks
    * Enthält ein paar nicht so basic zeitrechnungen
    * zusammenfassung der basic time conversion Rechnungen
11. <ins>TimeConverter</ins>
    * Für einfache Zeitumrechnungen
    * um Zeit in deziamalZeit umzurechnen
12. <ins>PT_Facade_Class</ins>
    * Klasse welche die anderen verbindet 
    * besitzt Methoden zum Umsetzen der Hauptfunktionen
