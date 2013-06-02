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

import jaxbClasses.ObjectFactory;
import jaxbClasses.ProductInformationLOCAL;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;



@Path ("fridges/{fridgeID}/profiles")
public class ProfileResources {

	// Ressource: /fridges/{id}/profiles
	//--------------------------------------------
	// Gibt die Liste aller Profiles aus, die zu einem Kühlschrank{id} gehören
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Profiles getProfilesPlain(@PathParam("fridgeID") int fridgeID, @QueryParam("name") String name) throws JAXBException, IOException, DatatypeConfigurationException {
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/profilesLOCAL.xml");
		
		// Liste der Profiles aus ProfilesLOCAL ensprechend der profiles.xsd (REST) zusammenbauen
		Profiles pts = new Profiles();
		Profiles.Profile p;
		for(int i=0; i<psL.getProfile().size(); i++){
			p = new Profiles.Profile();
			Profiles.Profile.Name n = new Profiles.Profile.Name();
			n.setHref("fridges/"+fridgeID+"/profiles/"+psL.getProfile().get(i).getId());
			n.setValue(psL.getProfile().get(i).getName());
			p.setName(n);
			pts.getProfile().add(p);
		}
		return pts;
	}
	
	@GET 
	@Path("/{profileID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Profile getProfileByID(@PathParam("profileID") int profileID, @PathParam("fridgeID") int fridgeID) throws JAXBException, IOException, DatatypeConfigurationException {
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/profilesLOCAL.xml");
		
		// Liste nach passender id durchsuchen. Index merken
		int indexFound = -1;
		for (int i=0; i<psL.getProfile().size(); i++) {
			 if(psL.getProfile().get(i).getId() == profileID){
				 indexFound = i;
			 	break;
			 }
		}
		System.out.println(psL.getProfile().get(indexFound).getName());
		
		return createProfile(psL.getProfile().get(indexFound));
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addProfile(@PathParam("fridgeID") int fridgeID, Profile p) throws JAXBException, URISyntaxException{
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/profilesLOCAL.xml");
		
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
		MyMarshaller.marshall(psL, "data/fridges/"+ fridgeID + "/profilesLOCAL.xml");
		
		// Neu erstellte URI in Repsone angeben:
		return Response.created(new URI("fridges/"+fridgeID+"/profiles/"+freeID)).build();
	}
	
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
	
	private Profile createProfile(ProfilesLOCAL.Profile p){
		// REST-Profile zusammenstellen
		Profile profile = new Profile();
		profile.setName(p.getName());
		profile.setBirthdate(p.getBirthdate());
		profile.setGender(p.getGender());
		profile.setHeight(p.getHeight());
		profile.setWeight(p.getWeight());
		return profile;
	}
}
