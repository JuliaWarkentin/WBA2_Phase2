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
	
	public static FrigdeManagerStorage storage; // MasterObject mit allen lokalen Daten.
	
	public static void readXML() throws JAXBException{
		// Unmarshall
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		storage  =  (FrigdeManagerStorage) unmarshaller.unmarshal(new File("fridgemanagerstorage.xml"));
	}
	
	public String writeXML() {
		return null;
	}
	
	public JAXBClasses.Profile getProfileByName(String name) {
		JAXBClasses.Profile p = new JAXBClasses.Profile();
		// Liste der Profiles auslesen
		Profiles profiles = storage.getProfiles();
		List<JAXBClasses.FrigdeManagerStorage.Profiles.Profile> list = profiles.getProfile();
		// Liste nach übergebenem name durchsuchen
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
	
	@GET @Produces("application/xml")
	public String getProfilePlain(@QueryParam("name") String name) throws JAXBException {
		JAXBClasses.Profile p = getProfileByName(name);
				
		JAXBContext jaxbContext = JAXBContext.newInstance(JAXBClasses.Profile.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marshaller.marshal(p, sw);
		
		String s = sw.toString();
		System.out.println(s);
		return s;
	}
	
	@GET @Produces("text/html")
	public String getProfilePlainHtml(@QueryParam("name") String name) throws JAXBException{
		JAXBClasses.Profile p = getProfileByName(name);
				
		JAXBContext jaxbContext = JAXBContext.newInstance(JAXBClasses.Profile.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter sw = new StringWriter();
		marshaller.marshal(p, sw);
		
		String xml = sw.toString();
		String s = "<html><head><title>Profile: "+ name + "</title></head><body>\n";
		s = s.concat("<h1>GET</h1>");
		s = s.concat("<pre><code>"+xml+"</code></pre>");
		s = s.concat("</body></html>");
		return s;
	}
}
