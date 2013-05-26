package webservice;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import jaxbClasses.Profile;
import jaxbClasses.Profiles;


import com.sun.jersey.api.client.*;

public class TestClient {

	public static  WebResource wrs;
	
	public static void main(String[] args) throws JAXBException, DatatypeConfigurationException {
	    String url = "http://localhost:4434/fridges/1/profiles/12";
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
	
	public static void testPUT() throws DatatypeConfigurationException {
		// PUT-Test
	    System.out.println("\nPUT.");
	    Profile p = new Profile();
	    p.setName("Salad Fingers");
	    p.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("1990-01-01"));
	    p.setGender("m");
	    p.setHeight(170f);
	    p.setWeight(65f);
	    ClientResponse r = wrs.type(MediaType.APPLICATION_XML).put(ClientResponse.class, p);
//	    wrs.type(MediaType.APPLICATION_XML).put(String.class, 
//	    		"<?xml version=\"1.0\" encoding=\"UTF-8\"?><p:profile xmlns:p=\"http://meinnamespace.meinefirma.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://meinnamespace.meinefirma.de profile.xsd \"><p:name>Julia</p:name><p:birthdate>2001-01-01</p:birthdate><p:gender>m</p:gender><p:height>170</p:height><p:weight>60</p:weight><p:currentPurchaseValue>0</p:currentPurchaseValue><p:lastMonthPurchaseValue>0</p:lastMonthPurchaseValue><p:recentBoughtProducts/><p:recentConsumedProducts/></p:profile>");
	    System.out.println("ende Client");
	}
}
