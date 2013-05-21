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

import JAXBClasses.FrigdeManagerStorage;
import JAXBClasses.Profile;

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
}