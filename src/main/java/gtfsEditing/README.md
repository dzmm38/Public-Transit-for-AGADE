#Gtfs-Editing Modul

Module for editing graphs and changing the data

Das Modul ist dafür da, Graphen welche zum Routing in dem Modul **publicTransportRouting** im voraus zu ändern <br>
Dies bezieht sich jedoch nur auf den Teil, welcher sich mit dem öffentlichen Verkehr beschäftigt. <br>

Mit diesem Modul können GTFS Daten bearbeitet und erstellt werden. <br>
Die Erstellung und Bearbeitung bezieht sich am Anfang jedoch **nur auf Fare Dateien** des GTFS Formates. <br>

Für zukünfitge Erweiterungen wäre es möglich weitere somit hier möglich einfach weitere **Controller** für die <br>
jeweiligen Gtfs Dateien zu schreiben und diese dann mithilfe der Facade Klasse zu bedienen umso das Modul zu erweitern.

##### 1. Erstellen eines Editor Facade Objects.
    EditorFacade editorFacade = new EditorFacade(gtfsFile, outputFile);
    
    - Als parameter sollen hier zu einem die Gtfs Datei übergeben werden,
    welche bearbeitet werden soll, und zu anderem entwerder dieselbe Datei,
    wenn man diese mit der beareiteten überschreiben werden möchte oder eine
    neu definierte datei (Übergeben sollen diese jedoch als Pfade)
    - die gtfs Datei wird dann eingelesen, damit man darauf zugreifen kann
    - ein FareController Objekt wird erstellt und der Facade zugewiesen, um 
    darauf dann änderungen zu definieren.
    
##### 2.Erstellen eines Fare_Attribute
    editorFacade.getFareController().createAttribute(String fare_id, float price, String currencyType, PaymentMethod paymentMethod, Transfers transfers);
    editorFacade.getFareController().createAttribute(String fare_id, float price, String currencyType, PaymentMethod paymentMethod, int transferDuration);
    
    - Um Fare Rules zuordnen zu können muss man erst einmal Fare Attribute erstellen.
    - dazu greift man über die Facade Klasse auf den FareController zu (getFareController()) und ruf davon dann die gewünschte
    createAttribute Methode auf
        - es gibt 2 welche entweder den Transfer Mode oder die Transfer Duration als Parameter benötigen (rest ist gleich)
    - anhand dessen, werden dann die Änderungen erstellt und dem Transformer hinzugefügt um später umgesetzt zu werden.

##### 3. Erstellen einer Fare_Rule
    Eine Fare_Rule ist das Zuordnen eines Attributes zu entweder:
    
    - einer Route mit:
        editorFacade.getFareController().addRule_with_RouteId(Fare_Attribute attribute,Route route);
    
    - einer OriginZone mit:
        editorFacade.getFareController().addRule_with_OriginId(Fare_Attribute attribute, Stop originStop);
    
    - einer DestinationZone:
        editorFacade.getFareController().addRule_with_DestinationId(Fare_Attribute attribute,Stop destinationStop);
    
    - mehreren Zonen mit ContainsZone mit:
        editorFacade.getFareController().addRule_with_ContainsId(Fare_Attribute attribute, Stop containsStop);
    
    - einer Origin- und DesinationZone mit:
        editorFacade.getFareController().addRule_with_OriginId_DestinationId(Fare_Attribute attribute,Stop originStop,Stop destinationStop);
        
    Dabei wird die Fare_Rule mit den jeweiligen Parametern erstellt und dem Transformer zur Änderung hinzugefügt.
    Die Logik welche Attribute und Routen etc. zugeordnet werden (welche Route mit welchem Attribut), muss jedoch selbst erstellt werden.
    
##### 4. Umsetzen der Änderungen
    editorFacade.applyFareChanges();
    
    - damit werden sämtliche Änderungen welche die beiden Fare Dateien betreffen in den Gtfs Daten umgesetzt
    - die Änderungen werden dann inform einer Neuen datei gespeichert oder wenn in und Output Datei gleich sein sollten
    überschrieben
    
Gibt auch eine Testklasse (**EditorFacadeTest**) in der alle diese Sachen einmal umgesetzt sind. <br>

Für Erweiterungen / Erklärungen oder wie GTFS Daten aufgebaut sind : https://developers.google.com/transit/gtfs/reference <br>
Für genauere Erklärungen zu den Fare Dateien und Beispiele: https://code.google.com/archive/p/googletransitdatafeed/wikis/FareExamples.wiki


###Kurze Zusammenfassung aller Klassen

1. <ins>FareController</ins>
    * zuständig für die eigentliche Logik der Transformationen bzw. Änderungen der fare_attribute und fare_rule Datei
    * mit verschiedenen Optionen zu den jeweiligen Dateien
    * Führt nicht die Änderungen aus, aber fügt diese dem Transformer hinzu
2. <ins>ReaderHandler</ins>
    * Verwaltet den GtfsReader und kümmert sich um das Einlesen der aktuellen Gtfs Daten
3. <ins>TransformerHandler</ins>
    * Verwaltet alle Sachen, welche direkt mit der Transformation der GTFS Daten zu tun hat.
    * Verwaltet alle definierten Änderungen im Transformer
    * Führt die Transformation der Daten sowie dessen Speicherung durch
4. <ins>Model</ins>
    * repräsentiert die beiden Fare Klassen des GTFS Formates
    * **Fare_Attribute**: Attribute mit Preis, Gültigkeit, ID, etc. <br>
    Enthält Methode zum Zurückgeben des Attributes als  String im Format zur Transformation
    * **Fare_Rule**: Regeln zum Festlegen der Attribute zu Routen / Zonen etc. <br>
    Enthält Methode zum Zurückgeben des Attributes als  String im Format zur Transformation
    * **PaymentMethod**: Enum in der alle gültigen Werte der Bezahlungsmethoden definiert sind.
    * **Transfers**: Enum in der alle gültigen Werte der Transfer Optionen definiert sind
5. <ins>EditorFacade</ins>
    * Facade Klasse 
    * Enthält FareController und gtfsFeed um Fare_Attributes and Fare_Rules damit erstellen zu können.
    * getFareController um darauf dann auf alle Methoden zugreifen zu können
    * **Für Zukunft noch andere Controller hier adden um Facade zu erweitern. <br>
    mit get......Controller können dann auf diese auch zugegriffen werden.**

