Fridgemanager
===========
Simon Klinge (11082448),
Julia Warketnin (11082815)

## Informationen zum Testen des Projekts:
### Umgebung
* Webservice und XMPP-Server laufen über localhost (Default Ports)
* Server und Client bauen Verbindung zu je einem User auf dem XMPP-Server auf (in unserem Fall: "hans69" und "hans70")
* Der Leafnode mit dem String: expirationNodeID (Aus der Klasse XMPPData, in unserem Fall "expiration") muss auf dem XMPP-Server mit erlaubten Payload erstellt sein. Ansonsten einmal createNode("expiration") aus XMPPSession.java aufrufen.

### Ausfühung
* Als erstes die "main.java" für den Webservice starten, kurz danach die "main.java" für die Clientapplikation. 
* In der Applikation unter dem Reiter "Notifications" -> "Subscribe to..." (es erscheint eine Liste aller NodeID´s) -> "expiration" auswählen und abonnieren. Nicht zuviel Zeit lassen,
* denn das Startdatum für den Server und der Datenbestand in den Kühlschränken ist so gestellt, das nach etwa 40 Sekunden ein Produkt abläuft und der Server eine Nachricht "published". Ansonsten in Server.java den Wert für seconsPerDay erhöhen.
