package webservice;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;


import JAXBClasses.ObjectFactory;
import JAXBClasses.Profile;

@Path ("fridges/{fridgeid}/profiles")
public class ProfileService {

	// Ressource: /fridges/{id}/profiles
	//--------------------------------------------
	// Gibt die Liste aller Profiles aus, die zu einem Kühlschrank{id} gehören
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public JAXBClasses.Profile getProfilePlain(@QueryParam("name") String name) throws JAXBException {
		JAXBClasses.Profile p = getProfileByName(name);
		return p;
	}
	
	@GET 
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public JAXBClasses.Profile getProfileByID(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid) throws JAXBException, IOException, DatatypeConfigurationException {
		System.out.println(fridgeid+" | "+id);
		return Data.readProfileByID(fridgeid, id);
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({ MediaType.APPLICATION_XML})
	//@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public void createProfileByID(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid, Profile p) throws JAXBException, DatatypeConfigurationException, IOException {
		// Profil bereits vorhanden?
		// --> nun updaten? oder verweigern?
		System.out.println(fridgeid+" | "+id);
		try {
			// Profile existiert bereits?
			Data.readProfileByID(fridgeid, id);
			System.out.println("Profiles exists");
			// --> updaten?
		} catch (IOException e) {
			//e.printStackTrace();
			// Profil konnte nicht geladen werden
			// --> neues Profil erstellen
			Data.writeProfile(fridgeid, id,
					p.getName(), p.getBirthdate().toString(), p.getGender(), 
					""+p.getHeight(), ""+p.getWeight());
		}
	}
	
	public JAXBClasses.Profile getProfileByName(String name) {
		/*
		// Liste der Profiles auslesen
		Profiles profiles = Data.storage.getProfiles();
		List<JAXBClasses.FrigdeManagerStorage.Profiles.Profile> list = profiles.getProfile();
		// Liste nach übergebenem name durchsuchen
		JAXBClasses.Profile p = new JAXBClasses.Profile();
		for (JAXBClasses.FrigdeManagerStorage.Profiles.Profile profile : list) {
			if(profile.getName().equals(name)){
				// Gefunden. Erstelle Profile.
				p.setName(profile.getName());
				p.setGender(profile.getGender());
				p.setBirthdate(profile.getBirthdate());
				p.setHeight(profile.getHeight());
				p.setWeight(profile.getWeight());
				return p;
			}
		}*/
		return null;
	}
}
