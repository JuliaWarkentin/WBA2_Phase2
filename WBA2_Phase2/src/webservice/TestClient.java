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

import jaxbClasses.CurrencyAttr;
import jaxbClasses.Notification;
import jaxbClasses.Product;
import jaxbClasses.ProductType;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;


import com.sun.jersey.api.client.*;

public class TestClient {

	public static  WebResource wrs;
	
	public static void main(String[] args) throws JAXBException, DatatypeConfigurationException {
	    String url = "http://localhost:4434/fridges/1/producttypes/1/products";
	    System.out.println("URL: " + url);
	    wrs = Client.create().resource(url);
	    
	    testPOSTproducts();
	}
	
	public static void testGET() {
		System.out.println("\nApplication/xml:");
	    System.out.println(wrs.accept("application/xml").get(String.class));
	    System.out.println("\ntext/xml:");
	    System.out.println(wrs.accept("text/xml").get(String.class));
	    
	    ClientResponse response = wrs.accept("text/plain").get(ClientResponse.class);
	    int status = response.getStatus();
	    String textEntity = response.getEntity(String.class);
	    System.out.println(textEntity);
	}
	
	public static void testPOSTprofiles() throws DatatypeConfigurationException {
		Profile p = new Profile();
	    p.setName("Salad Fingers");
	    p.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("1990-01-01"));
	    p.setGender("m");
	    p.setHeight(170f);
	    p.setWeight(65f);
	    
	    
	    ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, p);
	    System.out.println(r.getStatus());
	}
	
	public static void testPOSTnotifications() throws DatatypeConfigurationException {
		Notification n = new Notification();
	    n.setType("warning");
	    n.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("1990-01-01"));
	    n.setHead("Product stolen");
	    n.setText("Ein Produkt wurde gestohlen");
	    
	    ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, n);
	    System.out.println(r.getStatus());
	}
	
	public static void testPOSTproducttypes(){
		ProductType pt = new ProductType();
		ProductType.ProductInformation ppt = new ProductType.ProductInformation();
		ppt.setName("delikato Joghurt Dressing Light");
		pt.setProductInformation(ppt);
		
		ProductType.StockData stockdata = new ProductType.StockData();
		stockdata.setMinstock(3);
		stockdata.setStock(3);
		pt.setStockData(stockdata);
		
		ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, pt);
	    System.out.println(r.getStatus());
	}
	
	public static void testPOSTproducts() throws DatatypeConfigurationException{
		
		Product p = new Product();
		p.setInputdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2000-01-01"));
		p.setExpirationdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2000-01-02"));
		Product.Owner po = new Product.Owner();
		po.setValue("willbeignored");
		po.setHref("fridges/1/profile/4");
		p.setOwner(po);
		Product.PriceWas priceWas = new Product.PriceWas();
		priceWas.setCurrency(CurrencyAttr.EUR);
		priceWas.setValue(1.39f);
		p.setPriceWas(priceWas);
		
		ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, p);
	    System.out.println(r.toString());
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
	
	public static void printClientResponse(){
		
	}
}
