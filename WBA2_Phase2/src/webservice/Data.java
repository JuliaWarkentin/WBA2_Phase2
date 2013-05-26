package webservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import jaxbClasses.Profile;
import jaxbClasses.Profiles;


public class Data {

	public static FileWriter writer;
	public static BufferedReader reader;
	
	// 
	public static void writeProfile(int fridgeid, int id, 
			String name, 
			String birthdate,
			String gender,
			String height,
			String weight) throws IOException {
		
		String path = "data/fridges/"+ fridgeid +"/profiles"; // Verzeichnis
	
		for(;;) {
			try {
				writer = new FileWriter(path+"/"+id+".txt");
				break;
			} catch (IOException e) {
				// Verzeichnis nicht gefunden: --> Verzeichnis erstellen
				File dir = new File(path);
				System.out.println("Schreibrecht?: "+dir.canWrite());
				if(dir.mkdir())
					System.out.println("Verzeichnis erstellt: "+ path);
			}
		}
		writer.write(name); 
		writer.append(System.getProperty("line.separator"));
		writer.write(birthdate); 
		writer.append(System.getProperty("line.separator"));
		writer.write(gender); 
		writer.append(System.getProperty("line.separator"));
		writer.write(height); 
		writer.append(System.getProperty("line.separator"));
		writer.write(weight); 
		writer.append(System.getProperty("line.separator"));
		writer.close();
	}
	
	public static Profile readProfileByID(int fridgeid, int id) throws IOException, DatatypeConfigurationException {
		reader = new BufferedReader(new FileReader("data/fridges/"+fridgeid+"/profiles/"+id+".txt"));
		Profile p = new Profile();
		p.setName(reader.readLine());
		p.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(reader.readLine()));
		p.setGender(reader.readLine());
		p.setHeight(Float.parseFloat(reader.readLine()));
		p.setWeight(Float.parseFloat(reader.readLine()));
		return p;
	}
	
	public static Profiles readProfiles(int fridgeid) throws IOException, DatatypeConfigurationException {
		Profiles profiles = new Profiles();
		String[] list = getFilelist("data/fridges/"+fridgeid+"/profiles");
		int[] profileIDs = new int[list.length];
		for(int i=0; i<list.length; i++) {
			profileIDs[i] = Integer.parseInt(list[i].substring(0, list[i].lastIndexOf('.')));
			Profile p = readProfileByID(fridgeid, profileIDs[i]);
			Profiles.Profile p1 = new Profiles.Profile();
			Profiles.Profile.Name name = new Profiles.Profile.Name();
			name.setValue(p.getName());
			name.setHref(TestServer.url+"/fridges/"+fridgeid+"/profiles/"+profileIDs[i]);
			p1.setName(name);
			profiles.getProfile().add(p1);
		}
		return profiles;
	}
	
	public static String[] getFilelist(String path) {
		File dir = new File(path);
		return dir.list();
	}
}