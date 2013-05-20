package webservice;

import javax.xml.bind.JAXBException;

import JAXBClasses.FrigdeManagerStorage;

public class Data {

	public static FrigdeManagerStorage storage; // MasterObject mit allen lokalen Daten
	
	public static void readXML() throws JAXBException {
		storage = (FrigdeManagerStorage) MyMarshaller.unmarshall("fridgemanagerstorage.xml");
	}
}
