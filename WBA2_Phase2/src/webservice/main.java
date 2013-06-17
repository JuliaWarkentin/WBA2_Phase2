package webservice;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import jaxbClasses.ObjectFactory;
import jaxbClasses.Profile;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;

public class main {
	public static String url = "http://localhost:4434";
	public static  WebResource wrs;
	public static void main(String[] args) throws Exception {
		// Start Grizzly
	    SelectorThread srv = GrizzlyServerFactory.create(url);
	    System.out.println("Grizzly Port: " + srv.getPort() + " | " + srv.getPortLowLevel() + " | URL: "+ url);
	    System.out.println(srv.toString());
	    Thread.sleep(10000); // 10 sekunden
	    
	    Service s = new Service();
	    s.checkProducts(DatatypeFactory.newInstance().newXMLGregorianCalendar("2001-01-15"));
	    
	    srv.stopEndpoint();
	    System.out.println("Server AUS!");
	}
}
