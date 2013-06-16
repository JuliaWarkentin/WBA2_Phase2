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
import jaxbClasses.Fridge;
import jaxbClasses.Fridge.ProductTypes;
import jaxbClasses.Fridge.ProductTypes.ProductType.Products;
import jaxbClasses.Fridge.ProductTypes.ProductType.StockData;
import jaxbClasses.Notification;
import jaxbClasses.Product;
import jaxbClasses.ProductType;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;


import com.sun.jersey.api.client.*;

public class TestClient {

	public static  WebResource wrs;
	
	public static void main(String[] args) throws JAXBException, DatatypeConfigurationException {
	    testPUTfridges();
	}
	
	// test ok
	public static void testPOSTprofiles() throws DatatypeConfigurationException {
		String url = "http://localhost:4434/profiles";
		wrs = Client.create().resource(url);
		Profile p = new Profile();
	    p.setName("Luigi Sommer");
	    p.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("1990-01-01"));
	    p.setGender("m");
	    p.setHeight(170f);
	    p.setWeight(65f);
	    
	    ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, p);
	    System.out.println(r.toString());
	}
	
	// test ok
	public static void testPOSTnotifications() throws DatatypeConfigurationException {
		String url = "http://localhost:4434/notifications";
		wrs = Client.create().resource(url);
		Notification n = new Notification();
		Notification.SendTo st = new Notification.SendTo();
		Notification.SendTo.Profile p = new Notification.SendTo.Profile();
		p.setHref("/profiles/4");
		p.setName("ignored");
		st.setProfile(p);
		n.setSendTo(st);
	    n.setType("warning");
	    n.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar("1990-01-01"));
	    n.setHead("Product stolen");
	    n.setText("Ein Produkt wurde gestohlen");
	    
	    ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, n);
	    System.out.println(r.toString());
	}
	
	// test ok
	public static void testPOSTproducts() throws DatatypeConfigurationException{
		String url = "http://localhost:4434/products";
		wrs = Client.create().resource(url);
		
		Product p = new Product();
		
		Product.ProductType pt = new Product.ProductType();
		pt.setHref("/producttypes/2"); 
		pt.setName("ignored");
		p.setProductType(pt);
		
		Product.InFridge f = new Product.InFridge();
		f.setHref("/fridges/1");
		f.setName("ignored");
		p.setInFridge(f);
		
		p.setInputdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2000-01-01"));
		p.setExpirationdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2000-01-02"));
		Product.Owner po = new Product.Owner();
		Product.Owner.Profile pro = new Product.Owner.Profile();
		pro.setHref("/profile/4");
		pro.setName("willbeignored");
		po.setProfile(pro);
		p.setOwner(po);
		Product.PriceWas priceWas = new Product.PriceWas();
		priceWas.setCurrency(CurrencyAttr.EUR);
		priceWas.setValue(0.99f);
		p.setPriceWas(priceWas);
		
		ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, p);
	    System.out.println(r.toString());
	}
	
	// test ok
	public static void testPOSTfridges() {
		String url = "http://localhost:4434/fridges";
		wrs = Client.create().resource(url);
		
		Fridge f = new Fridge();
		f.setName("Another Fridge");
		
		Fridge.Profiles ps = new Fridge.Profiles();
		Fridge.Profiles.Profile p = new Fridge.Profiles.Profile();
		p.setHref("/profiles/4");
		p.setName("shouldbeignored");
		ps.getProfile().add(p);
		f.setProfiles(ps);
		
		
		ClientResponse r = wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, f);
	    System.out.println(r.toString());
	}
	
	// test ok
	public static void testPUTfridges() {
		// GET Fridge
		String url = "http://localhost:4434/fridges/3";
		wrs = Client.create().resource(url);
		Fridge f = wrs.type(MediaType.APPLICATION_XML).get(Fridge.class);
	    System.out.println(f.toString());
	    
		// Change this Fridge
	    // -> Neuen Produkttyp mit einem neuen Produkt hinzufügen
	    Fridge.ProductTypes.ProductType pt = new Fridge.ProductTypes.ProductType();
	    pt.setHref("/producttype/2");
	    Fridge.ProductTypes.ProductType.Products products = new Fridge.ProductTypes.ProductType.Products();
	    Fridge.ProductTypes.ProductType.Products.Product product = new Fridge.ProductTypes.ProductType.Products.Product();
	    product.setHref("/products/6");
	    product.setState("inside");
	    products.getProduct().add(product);
	    pt.setProducts(products);
	    Fridge.ProductTypes.ProductType.StockData st = new Fridge.ProductTypes.ProductType.StockData();
	    st.setMinStock(1);
	    st.setMaxStock(3);
	    pt.setStockData(st);
	    if(f.getProductTypes() != null)
	    	f.getProductTypes().getProductType().add(pt);
	    else {
	    	System.out.println("neu");
	    	f.setProductTypes(new Fridge.ProductTypes());
	    	f.getProductTypes().getProductType().add(pt);
	    }
	    
		// PUT Fridge
	    ClientResponse r;
	    for(int i=0; i<5; i++) { // Mehrals putten, um Idempotenz zu prüfen
			r = wrs.type(MediaType.APPLICATION_XML).put(ClientResponse.class, f);
			System.out.println(r.toString());
		}
	}
	/*
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
	
	public static void testDELETE(){
		ClientResponse r = wrs.type(MediaType.APPLICATION_XML).delete(ClientResponse.class);
		System.out.println(r.toString());
	}
	
	public static void printClientResponse(){
		
	}
	*/
}
