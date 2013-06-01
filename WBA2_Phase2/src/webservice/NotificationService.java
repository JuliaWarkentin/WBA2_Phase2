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

import jaxbClasses.Notification;
import jaxbClasses.Notifications;
import jaxbClasses.NotificationsLOCAL;
import jaxbClasses.ObjectFactory;
import jaxbClasses.ProductInformationLOCAL;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;

@Path ("fridges/{fridgeid}/notifications")
public class NotificationService {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Notifications getNotifications(@PathParam("fridgeid") int fridgeid, @QueryParam("name") String name) throws JAXBException, IOException, DatatypeConfigurationException {
		NotificationsLOCAL nsL = (NotificationsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeid + "/notificationsLOCAL.xml");
		
		// Liste der Notifications aus notificationLOCAL.xml ensprechend der notifications.xsd (REST) zusammenbauen
		Notifications ns = new Notifications();
		Notifications.Notification n;
		System.out.println(nsL.getNotification().size());
		for(int i=0; i<nsL.getNotification().size(); i++){
			n = new Notifications.Notification();
			n.setHref("fridges/"+ fridgeid +"/notifications/"+ nsL.getNotification().get(i).getId());
			n.setType(nsL.getNotification().get(i).getType());
			n.setDate(nsL.getNotification().get(i).getDate());
			n.setHead(nsL.getNotification().get(i).getHead());
			ns.getNotification().add(n);
		}
		return ns;
	}
	
	
	@GET 
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Notification getNotification(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid) throws JAXBException, IOException, DatatypeConfigurationException {
		NotificationsLOCAL nsL = (NotificationsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeid + "/notificationsLOCAL.xml");
		
		// Liste nach passender id durchsuchen. Index merken
		int indexFound = -1;
		for (int i=0; i<nsL.getNotification().size(); i++) {
			 if(nsL.getNotification().get(i).getId() == id){
				 indexFound = i;
			 	break;
			 }
		}
		
		Notification n = new Notification();
		n.setType(nsL.getNotification().get(indexFound).getType());
		n.setDate(nsL.getNotification().get(indexFound).getDate());
		n.setHead(nsL.getNotification().get(indexFound).getHead());
		n.setText(nsL.getNotification().get(indexFound).getText());
		
		return n;
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addNotification(@PathParam("fridgeid") int fridgeid, Notification n) throws JAXBException, URISyntaxException{
		NotificationsLOCAL nsL = (NotificationsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeid + "/notificationsLOCAL.xml");
		// Nach einer freien Profile-id suchen
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
		notification.setType(n.getType());
		notification.setDate(n.getDate());
		notification.setHead(n.getHead());
		notification.setText(n.getText());
		
		nsL.getNotification().add(notification);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(nsL, "data/fridges/"+ fridgeid + "/notificationsLOCAL.xml");
		
		// Neu erstellte URI in Repsone angeben:
		return Response.created(new URI("fridges/"+fridgeid+"/notifications/"+freeID)).build();
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({ MediaType.APPLICATION_XML})
	public Response createProfileByID(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid, Profile p) throws JAXBException, IOException {
		Data.writeProfile(fridgeid, id,
				p.getName(), p.getBirthdate().toString(), p.getGender(), 
				""+ p.getHeight(), ""+ p.getWeight());
		System.out.println("name: "+p.getName());
		return null;
	}
}
