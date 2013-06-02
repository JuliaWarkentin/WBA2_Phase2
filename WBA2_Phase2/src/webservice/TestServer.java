package webservice;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import jaxbClasses.ObjectFactory;
import jaxbClasses.ProductInformationLOCAL;
import jaxbClasses.Profile;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;

public class TestServer {
	public static String url = "http://localhost:4434";
	public static void main(String[] args) throws Exception {
	    SelectorThread srv = GrizzlyServerFactory.create(url);
	    System.out.println("URL: " + url);
	    Thread.sleep( 1000 * Integer.parseInt("120"));
	    srv.stopEndpoint();
	}
}
