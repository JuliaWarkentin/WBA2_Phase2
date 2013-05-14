package webservice;

import java.io.File;
import java.util.List;

import javax.ws.rs.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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
		System.out.println(storage.getProfiles().getProfile().get(0).getName());
	}
	
	@GET @Produces("text/plain")
	public String getProfilePlain(@QueryParam("name") String name){
		//return "LOL";
		String s = "empty";
		// Liste der Profiles auslesen
		Profiles profiles = storage.getProfiles();
		List<Profile> list = profiles.getProfile();
		// Liste nach übergebenem name durchsuchen
		for (Profile profile : list) {
			System.out.println(profile.getName() + " | " + name);
			if(profile.getName().equals(name)){
				// Gefunden. Stelle Datenpaket zusammen, konform zu profile.xsd
				
				//s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
				//s.concat("<p:profile xmlns:p=\"http://meinnamespace.meinefirma.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://meinnamespace.meinefirma.de profile.xsd \">\n");
				s = ("<p:name>"+ profile.getName() +"</p:name>");
			}
		}
		return s;
	}
	
	@GET @Produces("text/html")
	public String getProfilePlainHtml(@QueryParam("name") String name){
		return "<html><title>text/html</title><body><h2>Html: Hallo " + name + "</h2></body></html>";
	}
}
