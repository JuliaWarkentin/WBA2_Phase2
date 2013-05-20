package webservice;

import java.io.File;
import java.io.StringWriter;
import java.util.List;

import javax.ws.rs.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import JAXBClasses.FrigdeManagerStorage;
import JAXBClasses.FrigdeManagerStorage.Profiles.Profile;
import JAXBClasses.FrigdeManagerStorage.*;

import JAXBClasses.ObjectFactory;

@Path ("/profiles")
public class ProfileService {

	@GET 
	@Produces("application/xml")
	public String getProfilePlain(@QueryParam("name") String name) throws JAXBException {
		JAXBClasses.Profile p = getProfileByName(name);
		return MyMarshaller.marshall(p);
	}

	@GET 
	@Produces("text/html")
	public String getProfileQueryPlainHtml(@QueryParam("name") String name) throws JAXBException{
		JAXBClasses.Profile p = getProfileByName(name);
		return MyMarshaller.marshall(p);
	}

	@GET 
	@Produces("text/html")
	@Path("/{id}")
	public String getProfileByIDPlainHtml(@PathParam("id") int id) throws JAXBException{
		return ""+id;
	}

	public JAXBClasses.Profile getProfileByName(String name) {
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
		}
		return null;
	}
}