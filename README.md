# Public-Transit-For-AGADE

Module for Public Transit for the AGADE System using Graphhopper and
for editing graphs and changing the GTFS data. <br>

Dieses Modul besteht aus bis jetzt zwei Packages welche wiederum zwei
verschiedene Funktionen einnehmen. <br>
Beide Module können unabhängig voneinander benutzt werden, da jedes der Module seine eigene <br>
Facade Klasse besitzt

##### publicTransportRouting
    - Für erstellen und laden eines Graphen aus GTFS und OSM Dateien.
    - Ausführen von Queries zur Routenberechung mit verschiedenen Kriterien
    - Ermöglicht die Rückgabe von Routen mithilfe einer JSON oder YAML Datei welche nach erstellung
    gespeichert wird
    
    Für weitere Inforamtionen sowie Benutzeranweisungen bitte die README.md innerhalb des
    publicTransportRuting Packages lesen.

##### graphEditing
    - Für Ändern oder Hinzufügen von den Dateien fare_Attribute und fare_rules des GTFS Formates
    - Hiermiet lässt sich eine .zip Datei welche GTFS Daten repräsentiert einlesen bearbeiten 
    und wieder speichern
    - Zunkünfitg sollte hiermit auch die Änderung an andern Dateien des GTFS Formates möglich
    gemacht werden um somit z.B. Haltestellen innherhalb einer Route hinzuzufügen oder zu entfernen
    (Graph Manipulation)
    
    - Für weitere Informationen sowie Benutzeranweisungen bitte die README.md innerhalb des
    gtfsEditing Packages lesen.
    
     

