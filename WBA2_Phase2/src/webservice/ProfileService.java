package webservice;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
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
	public JAXBClasses.Profiles getProfilePlain(@PathParam("fridgeid") int fridgeid, @QueryParam("name") String name) throws JAXBException, IOException, DatatypeConfigurationException {
		return Data.readProfiles(fridgeid);
	}
	
	@GET 
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public JAXBClasses.Profile getProfileByID(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid) throws JAXBException, IOException, DatatypeConfigurationException {
		return Data.readProfileByID(fridgeid, id);
	}
	
	@PUT
	@Path("/{id}")
	@Consumes({ MediaType.APPLICATION_XML})
	public ResponseBuilder createProfileByID(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid, Profile p) throws JAXBException, IOException {
		Data.writeProfile(fridgeid, id,
				p.getName(), p.getBirthdate().toString(), p.getGender(), 
				""+p.getHeight(), ""+p.getWeight());
		return Response.status(201);
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
