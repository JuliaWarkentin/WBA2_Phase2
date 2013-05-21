package webservice;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import JAXBClasses.Profiles;
import JAXBClasses.FrigdeManagerStorage.Profiles.Profile;

import com.sun.jersey.api.client.*;

public class TestClient {

	public static  WebResource wrs;
	
	public static void main(String[] args) throws JAXBException {
		
	    String url = "http://localhost:4434/fridges/2/profiles/1";
	    System.out.println("URL: " + url);

	    wrs = Client.create().resource(url);
	    
	    testPUT();
	}
	
	public static void testGET() {
		System.out.println("\nApplication/xml:");
	    System.out.println(wrs.accept("application/xml").get(String.class));
	    System.out.println("\ntext/xml:");
	    System.out.println(wrs.accept("text/xml").get(String.class));
	}
	
	public static void testPUT() {
		// PUT-Test
	    System.out.println("\nPUT.");
	    wrs.type(MediaType.APPLICATION_XML).put(Profile.class, 
	    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p:profile xmlns:p=\"http://meinnamespace.meinefirma.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://meinnamespace.meinefirma.de profile.xsd \"><p:name>Peter</p:name><p:birthdate>2001-01-01</p:birthdate><p:gender>m</p:gender><p:height>170</p:height><p:weight>60</p:weight><p:currentPurchaseValue>0</p:currentPurchaseValue><p:lastMonthPurchaseValue>0</p:lastMonthPurchaseValue><p:recentBoughtProducts/><p:recentConsumedProducts/></p:profile>");
	}
}
