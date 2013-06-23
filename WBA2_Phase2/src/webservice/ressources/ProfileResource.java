package webservice.ressources;

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

import webservice.MyMarshaller;

import com.sun.jersey.api.NotFoundException;

import jaxbClasses.ObjectFactory;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;

/**
 * Implementiert:
 * 	/profiles		GET, POST
 *  /profiles/{id}  GET, DELETE
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
@Path ("/profiles")
public class ProfileResource {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Profiles getProfiles(@QueryParam("name") String queryName) throws JAXBException {
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.unmarshall("data/profilesLOCAL.xml");
		Profiles ps = new Profiles();
		Profiles.Profile p;
		
		if(queryName == null){ 	// Ohne QueryParam. Liste ungefiltert
			// Liste der Profiles mit Daten ProfilesLOCAL entsprechend der profiles.xsd (REST) zusammenbauen
			for(int i=0; i<psL.getProfile().size(); i++){
				p = new Profiles.Profile();
				p.setHref("/profiles/"+psL.getProfile().get(i).getId()); 	// Hyperlink für mehr Details
				p.setName(psL.getProfile().get(i).getName());				// und Name
				ps.getProfile().add(p);
			}
		}
		else {	// Profile nach Namen ausgeben
			System.out.print("QuerySearch on Profiles: "+queryName);
			for(int i=0; i<psL.getProfile().size(); i++){
				System.out.println(" compared with "+psL.getProfile().get(i).getName());
				if(queryName.equals(psL.getProfile().get(i).getName())) {
					p = new Profiles.Profile();
					p.setHref("/profiles/"+psL.getProfile().get(i).getId()); 	// Hyperlink für mehr Details
					p.setName(psL.getProfile().get(i).getName());				// und Name
					ps.getProfile().add(p);
					System.out.println("Siehst du mich?!");
				}
			}
		}
		return ps;
	}
	
	@GET 
	@Path("/{profileID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Profile getProfileByID(@PathParam("profileID") int profileID) throws JAXBException {
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.unmarshall("data/profilesLOCAL.xml");
		
		// Liste nach passender id durchsuchen. Index merken
		int indexFound = -1;
		for (int i=0; i<psL.getProfile().size(); i++) {
			 if(psL.getProfile().get(i).getId() == profileID){
				 indexFound = i;
			 	break;
			 }
		}
		if(indexFound == -1) {
			System.out.println("Profil nicht gefunden!"); 
			return null; // Server liefert dadurch Statuscode 204, müsste eigentlich 404 sein.
		}
		return createProfile(psL.getProfile().get(indexFound));
	}
	
	private Profile createProfile(ProfilesLOCAL.Profile p){
		// REST-Profil zusammenstellen
		Profile profile = new Profile();
		profile.setName(p.getName());
		profile.setBirthdate(p.getBirthdate());
		profile.setGender(p.getGender());
		profile.setHeight(p.getHeight());
		profile.setWeight(p.getWeight());
		return profile;
	}
	
	public static String getProfileNamebyID(int profileID) throws JAXBException {
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.unmarshall("data/profilesLOCAL.xml");
		for(int i=0; i<psL.getProfile().size(); i++) {
			if(profileID == psL.getProfile().get(i).getId()) {
				return psL.getProfile().get(i).getName();
			}
		}
		System.out.println("getProfileNamebyID failed...");
		return null;
	}
	
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addProfile(@PathParam("fridgeID") int fridgeID, Profile p) throws JAXBException, URISyntaxException{
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.unmarshall("data/profilesLOCAL.xml");
		
		// Nach einer freien Profile-id suchen
		int freeID = -1; boolean found;
		for(int i=1; i<=50 && freeID==-1; i++){
			found = true;
			for(jaxbClasses.ProfilesLOCAL.Profile profile : psL.getProfile()){
				if(profile.getId() == i) { // id belegt?
					found = false;
					break;
				}
			}
			if(found) // id noch frei?
				freeID = i; // übernehmen
		}
		
		// Neues Profil anlegen
		jaxbClasses.ProfilesLOCAL.Profile profile = new jaxbClasses.ProfilesLOCAL.Profile();
		profile.setId(freeID);
		profile.setName(p.getName());
		profile.setBirthdate(p.getBirthdate());
		profile.setGender(p.getGender());
		profile.setHeight(p.getHeight());
		profile.setWeight(p.getWeight());
		
		psL.getProfile().add(profile);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(psL, "data/profilesLOCAL.xml");
		
		// Neu erstellte URI im Response angeben:
		return Response.created(new URI("/profiles/"+freeID)).build();
	}
	/*
	@DELETE
	@Path("/{profileID}")
	public void deleteProfile(@PathParam("profileID") int profileID, @PathParam("fridgeID") int fridgeID) throws JAXBException {
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/profilesLOCAL.xml");
		
		//Suche Profil
		boolean found = false;
		for(int i=0; i<psL.getProfile().size(); i++){
			if(psL.getProfile().get(i).getId() == profileID) { // gefunden?
				found = true;
				psL.getProfile().remove(i); // löschen
				break;
			}
		}
		if(!found) { // Profil existiert nicht?
			throw new NotFoundException("Profile not found");
		}
		
		// Änderung übernehmen und speichern.
		MyMarshaller.marshall(psL, "data/fridges/"+ fridgeID + "/profilesLOCAL.xml");
	}
	
	
	*/
}
