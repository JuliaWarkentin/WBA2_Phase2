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
		/*
		int fridgeid = 1, id = 1;
		ProductTypeLOCAL ptL = (ProductTypeLOCAL) MyMarshaller.unmarshall("data/fridges/"+ fridgeid + "/producttypes/"+ id + ".xml");
		System.out.println(ptL.getName());
		System.out.println(ptL.getStockData().getStock());
		
		String n = "Milsani Frische Vollmilch";
		ProductInformationLOCAL piL = (ProductInformationLOCAL) MyMarshaller.unmarshall("data/productinformation/" + n + ".xml");
		System.out.println(piL.getName());
		*/
	    SelectorThread srv = GrizzlyServerFactory.create(url);
	    System.out.println("URL: " + url);
	    Thread.sleep( 1000 * Integer.parseInt("60"));
	    srv.stopEndpoint();
	}
}
