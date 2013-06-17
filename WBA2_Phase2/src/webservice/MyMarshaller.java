package webservice;

import java.io.File;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import jaxbClasses.ObjectFactory;

/**
 * Erleichtert das (un)marshallen und verkürzt den Zugriff auf 
 * eine Zeile
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class MyMarshaller {
	/**
	 * 
	 * @param o Zu "marshallendes" Objekt
	 * @param path Dateipfad
	 */
	public static void marshall(Object o, String path) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(o, new File(path));
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("Fehler beim marshallen");
		}
	}
	/**
	 * 
	 * @param path Dateipfad
	 * @return Gibt JAXB-Instanz zurück
	 */
	public static Object unmarshall(String path) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			return unmarshaller.unmarshal(new File(path));
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println("Fehler beim unmarshallen");
			return null;
		}
	}
}
