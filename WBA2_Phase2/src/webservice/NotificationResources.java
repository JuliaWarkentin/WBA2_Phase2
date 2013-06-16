package webservice;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import com.sun.jersey.api.NotFoundException;

import jaxbClasses.Notification;
import jaxbClasses.Notifications;
import jaxbClasses.NotificationsLOCAL;
import jaxbClasses.ObjectFactory;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.ProductsLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;

@Path ("/notifications")
public class NotificationResources {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Notifications getNotifications() throws JAXBException, IOException {
		NotificationsLOCAL nsL = (NotificationsLOCAL) MyMarshaller.unmarshall("data/notificationsLOCAL.xml");
		
		// Liste der Notifications aus notificationLOCAL.xml ensprechend der notifications.xsd (REST) zusammenbauen
		Notifications ns = new Notifications();
		Notifications.Notification n;
		System.out.println(nsL.getNotification().size());
		for(int i=0; i<nsL.getNotification().size(); i++){
			n = new Notifications.Notification();
			n.setHref("/notifications/"+ nsL.getNotification().get(i).getId());
			
			Notifications.Notification.SendTo st = new Notifications.Notification.SendTo();
			Notifications.Notification.SendTo.Profile p = new Notifications.Notification.SendTo.Profile();
			p.setHref("/profiles/"+nsL.getNotification().get(i).getProfile().getId());
			p.setName(ProfileResource.getProfileNamebyID(nsL.getNotification().get(i).getProfile().getId()));
			st.setProfile(p);
			n.setSendTo(st);
			n.setType(nsL.getNotification().get(i).getType());
			n.setDate(nsL.getNotification().get(i).getDate());
			n.setHead(nsL.getNotification().get(i).getHead());
			ns.getNotification().add(n);
		}
		return ns;
	}
	
	
	@GET 
	@Path("/{notificationID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Notification getNotification(@PathParam("notificationID") int notificationID) throws JAXBException, IOException, DatatypeConfigurationException {
		NotificationsLOCAL nsL = (NotificationsLOCAL) MyMarshaller.unmarshall("data/notificationsLOCAL.xml");
		
		// Liste nach passender id durchsuchen. 
		NotificationsLOCAL.Notification nL = new NotificationsLOCAL.Notification();
		for (int i=0; i<nsL.getNotification().size(); i++) {
			 if(nsL.getNotification().get(i).getId() == notificationID){
				 nL = nsL.getNotification().get(i); // Nachricht herausnehmen
				 break;
			 }
		}
		
		Notification n = new Notification(); 
		Notification.SendTo st = new Notification.SendTo();
		Notification.SendTo.Profile p = new Notification.SendTo.Profile();
		p.setHref("/profiles/" +nL.getProfile().getId());
		p.setName(ProfileResource.getProfileNamebyID(nL.getProfile().getId()));
		st.setProfile(p);
		n.setSendTo(st);
		n.setType(nL.getType());
		n.setDate(nL.getDate());
		n.setHead(nL.getHead());
		n.setText(nL.getText());
		
		return n;
	}
	
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addNotification(Notification n) throws JAXBException, URISyntaxException{
		NotificationsLOCAL nsL = (NotificationsLOCAL) MyMarshaller.unmarshall("data/notificationsLOCAL.xml");
		// Nach einer freien id suchen
		int freeID = -1; boolean found;
		for(int i=1; i<=50 && freeID==-1; i++) {
			found = true;
			for(jaxbClasses.NotificationsLOCAL.Notification noti : nsL.getNotification()){
				if(noti.getId() == i) { // id belegt?
					found = false;
					break;
				}
			}
			if(found) // id noch frei?
				freeID = i; // übernehmen
		}
		
		// Neue Notification anlegen
		jaxbClasses.NotificationsLOCAL.Notification notification = new jaxbClasses.NotificationsLOCAL.Notification();
		notification.setId(freeID);
		NotificationsLOCAL.Notification.Profile p = new NotificationsLOCAL.Notification.Profile();
		String href = n.getSendTo().getProfile().getHref();
		p.setId(Integer.parseInt(href.substring(href.lastIndexOf("/")+1))); // ID aus der href beziehen
		notification.setProfile(p);
		notification.setType(n.getType());
		notification.setDate(n.getDate());
		notification.setHead(n.getHead());
		notification.setText(n.getText());
		
		nsL.getNotification().add(notification);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(nsL, "data/notificationsLOCAL.xml");
		
		// Neu erstellte URI in Repsone angeben:
		return Response.created(new URI("/notifications/"+freeID)).build();
	}
	/*
	@DELETE
	@Path("/{notificationID}")
	public void deleteProduct(@PathParam("notificationID") int notificationID, @PathParam("fridgeID") int fridgeID) throws JAXBException {
		NotificationsLOCAL nsL = (NotificationsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/notificationsLOCAL.xml");
		
		//Suche Nachricht
		boolean found = false;
		for(int i=0; i<nsL.getNotification().size(); i++){
			if(nsL.getNotification().get(i).getId() == notificationID) { // gefunden?
				found = true;
				nsL.getNotification().remove(i); // löschen
				break;
			}
		}
		if(!found) { // Nachricht gefunden?
			throw new NotFoundException("Product not found");
		}
		
		// Änderung übernehmen und speichern.
		MyMarshaller.marshall(nsL, "data/fridges/"+ fridgeID + "/notificationsLOCAL.xml");
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({ MediaType.APPLICATION_XML})
	public Response createProfileByID(@PathParam("id") int id, @PathParam("fridgeID") int fridgeID, Profile p) throws JAXBException, IOException {
		Data.writeProfile(fridgeID, id,
				p.getName(), p.getBirthdate().toString(), p.getGender(), 
				""+ p.getHeight(), ""+ p.getWeight());
		System.out.println("name: "+p.getName());
		return null;
	}
	*/
}
