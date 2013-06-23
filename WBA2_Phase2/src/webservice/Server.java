package webservice;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import xmpp.XMPPSession;

import jaxbClasses.ObjectFactory;
import jaxbClasses.Profile;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;

/**
 * Einstiegspunkt für den Server. Stellt REST-Schnittstelle bereit.
 * Darüber hinaus werden Daten für alle Kühlschränke geprüft und 
 * ggf. Nachrichten auf dem XMPP-Server übermittelt.
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class Server {
	public static String url = "http://localhost:4434";
	public static WebResource wrs;
	public static XMPPSession xmpp;
	
	public Server()  {
		// Starte Server (Grizzly) und verbinde zum XMPP-Server
	    SelectorThread srv = null;
		try {
			srv = GrizzlyServerFactory.create(url);
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace(); System.out.println("Error creating GizzlyServer...");
		}
	    System.out.println("Grizzly created. Port: " + srv.getPort() + " | " + srv.getPortLowLevel() + "    URL: "+ url);
	    xmpp = new XMPPSession("hans69", "hans69");
	    
	    // Prüfe jeden Tag den Datenbestand. Simuliere über mehrere Tage hinweg
	    NotificationService s = new NotificationService();
	    int secondsPerDay = 10;
	    TimeSimulator ts = new TimeSimulator(secondsPerDay, 2001, 1, 10);
	    for(int i=0; i<10; i++) { // 10-Tage
	    	ts.printCurrentDate();
	    	s.checkProducts(ts.getXMLDate());
	    	ts.sleep1Day();
	    }
	    	
//	    s.checkProducts(DatatypeFactory.newInstance().newXMLGregorianCalendar("2001-01-15"));
	    
	    // Clean up
	    xmpp.disconnect();
	    srv.stopEndpoint();
	    System.out.println("Webservice down!");
	}
}
