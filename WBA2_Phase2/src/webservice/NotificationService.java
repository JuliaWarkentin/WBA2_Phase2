package webservice;

import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import jaxbClasses.Fridge;
import jaxbClasses.Notification;
import jaxbClasses.Product;
import jaxbClasses.Products;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import xmpp.XMPPData;
import xmpp.XMPPSession;

/**
 * Erstellt und published Nachrichten(Notifications) bedingt auf unserer
 * Fachlogik. Diese Klasse sollte ausschließlich vom Server verwendet werden.
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class NotificationService {
	
	/**
	 * Prüft alle nicht-verbrauchten Produkte auf ihr Haltbarkeitsdatum.
	 * Ist ein Produkt abgelaufen wird eine Nachricht auf /notifications
	 * hinterlegt (POST). Anschließend wird auf dem Knoten eine Referenz auf 
	 * diese Nachricht veröffentlicht.
	 * 
	 * @param currentDate Wird mit dem Haltbarkeitsdatum verglichen
	 */
	public void checkProducts(XMLGregorianCalendar currentDate) {
		// Alle Produkte anfordern
		String url = Server.url+"/products";
		Server.wrs = Client.create().resource(url);
		Products ps = Server.wrs.type(MediaType.APPLICATION_XML).get(Products.class);
		for(int i=0; i<ps.getProduct().size(); i++) {
			// Hyperlink folgen, um an Expirationdate zu kommen
			url = Server.url + ps.getProduct().get(i).getHref();
			Server.wrs = Client.create().resource(url);
			Product product = Server.wrs.type(MediaType.APPLICATION_XML).get(Product.class);
			// Abgelaufen?
			if((product.getExpirationdate().compare(currentDate) == DatatypeConstants.EQUAL) &&
					product.getState().equals("inside")) {
				System.out.println("Product: "+ url.substring(url.lastIndexOf("/")+1) +" ist abgelaufen!");
				// Erstelle Nachricht (POST auf /notifications)
				Notification n = new Notification();
				Notification.SendTo st = new Notification.SendTo();
				Notification.SendTo.Profile p = new Notification.SendTo.Profile();
				String href = product.getOwner().getProfile().getHref(); // Empfänger ist der Besitzer des Products
				p.setHref("/profiles/"+ href.substring(href.lastIndexOf("/")+1)); 
				st.setProfile(p);
				n.setSendTo(st);
				n.setDate(currentDate);
			    n.setType("warning");
			    n.setHead("Product expired");
			    n.setText("Your Product '" + product.getProductType().getName() + 
			    		"' in Fridge '" +product.getInFridge().getName() + 
			    		"' expired today! "+ currentDate.toString());
			    // POST it
			    url = Server.url + "/notifications";
				Server.wrs = Client.create().resource(url);
				ClientResponse r = Server.wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, n);
				System.out.println(r.toString());
				// Referenz publishen
				System.out.println("location: "+r.getLocation());
				Server.xmpp.pubItemInNode(XMPPData.expirationNodeID, "<href xmlns='pubsub:expiration'>JUNGE!</href>");
			}
		}
	}
	
	public void publishNotificationURI() {
		
	}
}
