package webservice.ressources;

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

import webservice.Helper;
import webservice.MyMarshaller;

import com.sun.jersey.api.NotFoundException;

import jaxbClasses.Fridge;
import jaxbClasses.Fridges;
import jaxbClasses.FridgesLOCAL;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.ProfilesLOCAL;

/**
 * Implementiert:
 * 	/fridges			GET, POST
 *  /fridges/{id}  		GET, PUT, DELETE
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
@Path ("/fridges")
public class FridgeResource {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Fridges getFridges(@QueryParam("profileid") int profileID) {
		FridgesLOCAL fsL = (FridgesLOCAL) MyMarshaller.unmarshall("data/fridgesLOCAL.xml");
		
		System.out.println("fasdhfkljasdhfklajsdhfklajsdhf: "+profileID);
		
		// Fridgeliste entsprechend der fridges.xsd erstellen
		Fridges fs = new Fridges();
		if(profileID <= 0) { // Alle Kühlschränke ausgeben
			for(int i=0; i<fsL.getFridge().size();i++) {
				Fridges.Fridge f = new Fridges.Fridge();
				f.setHref("/fridges/"+fsL.getFridge().get(i).getId());
				f.setName(fsL.getFridge().get(i).getName());
				fs.getFridge().add(f);
			}
		} else { // Kühlschränkte nach profileID filtern (
			for(int i=0; i<fsL.getFridge().size();i++) {
				// Alle Profile für jeden Kühlschrank durchgehen
				for(int j=0; j<fsL.getFridge().get(i).getProfiles().getProfile().size(); j++) {
					if(profileID == fsL.getFridge().get(i).getProfiles().getProfile().get(j).getId()) {
						Fridges.Fridge f = new Fridges.Fridge();
						f.setHref("/fridges/"+fsL.getFridge().get(i).getId());
						f.setName(fsL.getFridge().get(i).getName());
						fs.getFridge().add(f);
					}
				}
			}
		}
		return fs;
	}
	
	public static String getFridgeNamebyID(int fridgeID) throws JAXBException {
		FridgesLOCAL fsL = (FridgesLOCAL) MyMarshaller.unmarshall("data/fridgesLOCAL.xml");
		for(int i=0; i<fsL.getFridge().size(); i++) {
			if(fridgeID == fsL.getFridge().get(i).getId()) {
				return fsL.getFridge().get(i).getName();
			}
		}
		System.out.println("getFridgeNamebyID failed...");
		return null;
	}
	
	@GET 
	@Path("/{fridgeID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Fridge getFridge(@PathParam("fridgeID") int fridgeID) throws JAXBException, IOException, DatatypeConfigurationException {
		FridgesLOCAL fsL = (FridgesLOCAL) MyMarshaller.unmarshall("data/fridgesLOCAL.xml");
		
		// Lokale Fridgeliste nach passender id durchsuchen. 
		FridgesLOCAL.Fridge fL = new FridgesLOCAL.Fridge();
		for (int i=0; i<fsL.getFridge().size(); i++) {
			 if(fsL.getFridge().get(i).getId() == fridgeID){
				 fL = fsL.getFridge().get(i); // Fridge herausnehmen
				 break;
			 }
		}
		
		// Fridge-objekt nach fridge.xsd erstellen
		Fridge f = new Fridge();
		f.setName(fL.getName());
		
		// -> Profiles
		Fridge.Profiles ps = new Fridge.Profiles();
		for(int i=0; i<fL.getProfiles().getProfile().size(); i++) {
			int profileID = fL.getProfiles().getProfile().get(i).getId();
			Fridge.Profiles.Profile p = new Fridge.Profiles.Profile();
			p.setHref("/profiles/"+profileID);
			p.setName(ProfileResource.getProfileNamebyID(profileID));
			ps.getProfile().add(p);
		}
		f.setProfiles(ps);
		
		// -> Producttypes
		Fridge.ProductTypes pts = new Fridge.ProductTypes();
		if(fL.getProductTypes() != null) {
			for(int i=0; i<fL.getProductTypes().getProductType().size(); i++) {
				int producttypeID = fL.getProductTypes().getProductType().get(i).getId();
				Fridge.ProductTypes.ProductType pt = new Fridge.ProductTypes.ProductType();
				pt.setHref("/producttypes/"+producttypeID);
				pt.setName(ProducttypeResource.getProducttypeNamebyID(producttypeID));
				
				
				// -> Products
				Fridge.ProductTypes.ProductType.Products pds = new Fridge.ProductTypes.ProductType.Products();
				FridgesLOCAL.Fridge.ProductTypes.ProductType.Products pdsL = new FridgesLOCAL.Fridge.ProductTypes.ProductType.Products();
				pdsL = fL.getProductTypes().getProductType().get(i).getProducts();
				int currentStock = 0;
				for(int j=0; j<pdsL.getProduct().size(); j++) {
					Fridge.ProductTypes.ProductType.Products.Product p = new Fridge.ProductTypes.ProductType.Products.Product();
					p.setHref("/products/"+pdsL.getProduct().get(j).getId());
					String state = ProductResource.getProductStatebyID(pdsL.getProduct().get(j).getId());
					p.setState(state);
					if(state.equals("inside")) 
						currentStock++;
					pds.getProduct().add(p);
					
				}
				pt.setProducts(pds);
				
				// -> StockData
				FridgesLOCAL.Fridge.ProductTypes.ProductType.StockData stockL = new FridgesLOCAL.Fridge.ProductTypes.ProductType.StockData();
				stockL = fL.getProductTypes().getProductType().get(i).getStockData();
				Fridge.ProductTypes.ProductType.StockData stock = new Fridge.ProductTypes.ProductType.StockData();
				stock.setCurrentStock(currentStock);
				stock.setMinStock(stockL.getMinStock());
				stock.setMaxStock(stockL.getMaxStock());
				pt.setStockData(stock);
							
			
				pts.getProductType().add(pt);
			}
		}
		f.setProductTypes(pts);
		return f;
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addFridge(Fridge f) throws JAXBException, URISyntaxException{
		FridgesLOCAL fsL = (FridgesLOCAL) MyMarshaller.unmarshall("data/fridgesLOCAL.xml");
		
		// Nach einer freien Fridge-id suchen
		int freeID = -1; boolean found;
		for(int i=1; i<=50 && freeID==-1; i++){
			found = true;
			for(FridgesLOCAL.Fridge fridge : fsL.getFridge()){
				if(fridge.getId() == i) { // id belegt?
					found = false;
					break;
				}
			}
			if(found) // id noch frei?
				freeID = i; // übernehmen
		}
		
		// Kühlschrank anlegen. (Name und Profil des Erstellers. Weitere Profile und Produkte über PUT)
		FridgesLOCAL.Fridge fridge = new FridgesLOCAL.Fridge();
		fridge.setId(freeID);
		fridge.setName(f.getName());
		
		FridgesLOCAL.Fridge.Profiles ps = new FridgesLOCAL.Fridge.Profiles();
		FridgesLOCAL.Fridge.Profiles.Profile p = new FridgesLOCAL.Fridge.Profiles.Profile();
		String href = f.getProfiles().getProfile().get(0).getHref();
		p.setId(Helper.getID(href)); // ID aus href beziehen
		ps.getProfile().add(p);
		fridge.setProfiles(ps);
		
		fsL.getFridge().add(fridge);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(fsL, "data/fridgesLOCAL.xml");
		
		// Neu erstellte URI im Response angeben:
		return Response.created(new URI("/fridges/"+freeID)).build();
	}
	
	@PUT
	@Path("/{fridgeID}")
	@Consumes({ MediaType.APPLICATION_XML})
	public Response updateFridge(@PathParam("fridgeID") int fridgeID, Fridge f) throws JAXBException, URISyntaxException{
		FridgesLOCAL fsL = (FridgesLOCAL) MyMarshaller.unmarshall("data/fridgesLOCAL.xml");
		
		//Suche Fridge
		FridgesLOCAL.Fridge fL = new FridgesLOCAL.Fridge();
		boolean found = false;
		int indexFound = -1;
		for(int i=0; i<fsL.getFridge().size(); i++){
			if(fsL.getFridge().get(i).getId() == fridgeID && 
					fsL.getFridge().get(i).getId() == fridgeID) { // gefunden?
				found = true; indexFound = i;
				fL = fsL.getFridge().get(i); // Fridge herausnehmen
				break;
			}
		}
		if(!found) {
			throw new NotFoundException("Fridge not found");
		}
		
		// FridgeLOCAL erstellen aus übergebenen Fridge und der FridgeID;
		FridgesLOCAL.Fridge fridge = createFridgeLOCAL(fsL, f, fridgeID);
		
		// Fridge aktualisieren (Ersetze Kühlschrank durch Client´s übergebenen)
		fsL.getFridge().set(indexFound, fridge);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(fsL, "data/fridgesLOCAL.xml");
		
		// URI im Response angeben:
		return Response.created(new URI("/fridges/"+fridgeID)).build();
	}
	
	private FridgesLOCAL.Fridge createFridgeLOCAL(FridgesLOCAL fsL, Fridge f, int fridgeID) {
		FridgesLOCAL.Fridge fridge = new FridgesLOCAL.Fridge();
		fridge.setId(fridgeID);
		fridge.setName(f.getName());
		
		// -> Profile
		FridgesLOCAL.Fridge.Profiles ps = new FridgesLOCAL.Fridge.Profiles();
		FridgesLOCAL.Fridge.Profiles.Profile p;
		for(int i=0; i<f.getProfiles().getProfile().size(); i++) {
			p = new FridgesLOCAL.Fridge.Profiles.Profile();
			String href = f.getProfiles().getProfile().get(i).getHref();
			p.setId(Helper.getID(href)); // ID aus href beziehen
			ps.getProfile().add(p);
		}
		fridge.setProfiles(ps);
		
		// -> Produkttypen
		FridgesLOCAL.Fridge.ProductTypes pts = new FridgesLOCAL.Fridge.ProductTypes();
		FridgesLOCAL.Fridge.ProductTypes.ProductType pt;
		FridgesLOCAL.Fridge.ProductTypes.ProductType.StockData sd;
		for(int i=0; i<f.getProductTypes().getProductType().size(); i++) {
			pt = new FridgesLOCAL.Fridge.ProductTypes.ProductType();
			String href = f.getProductTypes().getProductType().get(i).getHref();
			pt.setId(Helper.getID(href));
			
			sd = new FridgesLOCAL.Fridge.ProductTypes.ProductType.StockData();
			sd.setMinStock(f.getProductTypes().getProductType().get(i).getStockData().getMinStock());
			sd.setMaxStock(f.getProductTypes().getProductType().get(i).getStockData().getMaxStock());
			pt.setStockData(sd);
			
			// -> Produkt(instanzen)
			FridgesLOCAL.Fridge.ProductTypes.ProductType.Products pds = new FridgesLOCAL.Fridge.ProductTypes.ProductType.Products();
			FridgesLOCAL.Fridge.ProductTypes.ProductType.Products.Product product;
			for(int j=0; j<f.getProductTypes().getProductType().get(i).getProducts().getProduct().size(); j++) {
				product = new FridgesLOCAL.Fridge.ProductTypes.ProductType.Products.Product();
				href = f.getProductTypes().getProductType().get(i).getProducts().getProduct().get(j).getHref();
				product.setId(Helper.getID(href));
				pds.getProduct().add(product);
			}
			pt.setProducts(pds);
			pts.getProductType().add(pt);
		}
		fridge.setProductTypes(pts);
		return fridge;
	}
}
