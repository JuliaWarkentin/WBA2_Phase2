package webservice.ressources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import webservice.MyMarshaller;

import com.sun.jersey.api.NotFoundException;

import jaxbClasses.ProductType;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;

/**
 * Implementiert:
 * 	/producttypes		GET, POST
 *  /producttypes/{id}  GET
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
@Path ("/producttypes")
public class ProducttypeResource {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public ProductTypes getProducttypes() throws JAXBException {
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.unmarshall("data/producttypesLOCAL.xml");
		
		// Liste der Produkttypen mit Daten ProductTypesLOCAL entsprechend der producttypes.xsd (REST) zusammenbauen
		ProductTypes pts = new ProductTypes();
		ProductTypes.ProductType pt;
		for(int i=0; i<ptsL.getProductType().size(); i++){
			pt = new ProductTypes.ProductType();
			pt.setHref("/producttypes/"+ptsL.getProductType().get(i).getId()); 	// Hyperlink für mehr Details
			pt.setName(ptsL.getProductType().get(i).getName());					// und Name
			pts.getProductType().add(pt);
		}
		return pts;
	}
	
	@GET 
	@Path("/{producttypeID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public ProductType getProducttype(@PathParam("producttypeID") int producttypeID) throws JAXBException  {
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.unmarshall("data/producttypesLOCAL.xml");
		
		// Liste nach passender id durchsuchen. Index merken
		int indexFound = -1;
		for (int i=0; i<ptsL.getProductType().size(); i++) {
			 if(producttypeID ==ptsL.getProductType().get(i).getId()) {
				 indexFound = i;
			 	break;
			 }
		}
		System.out.println(ptsL.getProductType().get(indexFound).getName());
		
		ProductType pt = new ProductType();
		ProductTypesLOCAL.ProductType ptL = new ProductTypesLOCAL.ProductType();
		
		// REST-ProductType erstellen
		return createProductType(ptsL.getProductType().get(indexFound));
	}
	
	private ProductType createProductType(ProductTypesLOCAL.ProductType ptL) {
		// Namen und Beschreibung
		ProductType pt = new ProductType();
		pt.setName(ptL.getName());
		pt.setDescription(ptL.getDescription());
		// Gewicht
		ProductType.Weight w = new ProductType.Weight();
		w.setValue(ptL.getWeight().getValue());
		w.setUnit(ptL.getWeight().getUnit());
		pt.setWeight(w);
		// Zutaten
		ProductType.Ingredients ingredients = new ProductType.Ingredients();
		ingredients.getIngredient().addAll(ptL.getIngredients().getIngredient());
		pt.setIngredients(ingredients);
		// Nährwerte
		ProductType.Nutrients nutrients = new ProductType.Nutrients();
		ProductType.Nutrients.CaloricValue caloricValue = new ProductType.Nutrients.CaloricValue();
		caloricValue.setUnit(ptL.getNutrients().getCaloricValue().getUnit()); 
		caloricValue.setValue(ptL.getNutrients().getCaloricValue().getValue());
		nutrients.setCaloricValue(caloricValue);
		nutrients.setProtein(ptL.getNutrients().getProtein());
		nutrients.setCarbohydrates(ptL.getNutrients().getCarbohydrates());
		nutrients.setOfwichsugar(ptL.getNutrients().getOfwichsugar());
		nutrients.setFat(ptL.getNutrients().getFat());
		nutrients.setOfwichsaturates(ptL.getNutrients().getOfwichsaturates());
		nutrients.setRoughage(ptL.getNutrients().getRoughage());
		nutrients.setSodium(ptL.getNutrients().getSodium());
		pt.setNutrients(nutrients);
		// Preis und Barcode
		pt.setBarcode(pt.getBarcode());
		return pt;
	}
	
	public static String getProducttypeNamebyID(int producttypeID) throws JAXBException {
		ProductTypesLOCAL ptL = (ProductTypesLOCAL) MyMarshaller.unmarshall("data/producttypesLOCAL.xml");
		for(int i=0; i<ptL.getProductType().size(); i++) {
			if(producttypeID == ptL.getProductType().get(i).getId()) {
				return ptL.getProductType().get(i).getName();
			}
		}
		System.out.println("getProducttypeNamebyID failed...");
		return null;
	}
	
	/*
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addProducttype(ProductType pt) throws JAXBException, URISyntaxException{
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.unmarshall("data/producttypesLOCAL.xml");
		
		// Nach einer freien Prooducttype-id suchen
		int freeID = -1; boolean found;
		for(int i=1; i<=50 && freeID==-1; i++){
			found = true;
			for(ProductTypesLOCAL.ProductType  producttype: ptsL.getProductType()){
				if(producttype.getId() == i) { // id belegt?
					found = false;
					break;
				}
			}
			if(found) // id noch frei?
				freeID = i; // übernehmen
		}
		
		// Neues Producttype anlegen (Produktinformationen werden ignoriert)
		ProductTypesLOCAL.ProductType producttype = new jaxbClasses.ProductTypesLOCAL.ProductType();
		producttype.setId(freeID);
		producttype.setName(pt.getProductInformation().getName());
		ProductTypesLOCAL.ProductType.StockData stockdata = new ProductTypesLOCAL.ProductType.StockData();
		stockdata.setMinstock(pt.getStockData().getMinstock());
		stockdata.setStock(pt.getStockData().getStock());
		producttype.setStockData(stockdata);
		
		ptsL.getProductType().add(producttype);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(ptsL, "data/fridges/"+ fridgeID + "/producttypesLOCAL.xml");
		
		// Neu erstellte URI in Repsone angeben:
		return Response.created(new URI("fridges/"+fridgeID+"/profiles/"+freeID)).build();
	}
	/*
	@DELETE
	@Path("/{producttypeID}")
	public void deleteProducttype(@PathParam("producttypeID") int producttypeID, @PathParam("fridgeID") int fridgeID) throws JAXBException {
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/producttypesLOCAL.xml");
		
		//Suche Produkttyp
		boolean found = false;
		for(int i=0; i<ptsL.getProductType().size(); i++){
			if(ptsL.getProductType().get(i).getId() == producttypeID) { // gefunden?
				found = true;
				ptsL.getProductType().remove(i); // löschen
				break;
			}
		}
		if(!found) { // Produkttyp existiert nicht?
			throw new NotFoundException("Producttyp not found");
		}
		
		// Änderung übernehmen und speichern.
		MyMarshaller.marshall(ptsL, "data/fridges/"+ fridgeID + "/producttypesLOCAL.xml");
	}
	
	
	@PUT
	@Path("/{producttypeID}")
	@Consumes({ MediaType.APPLICATION_XML})
	public ResponseBuilder createProfileByID(@PathParam("producttypeID") int producttypeID, @PathParam("fridgeID") int fridgeID, Profile p) throws JAXBException, IOException {
		Data.writeProfile(fridgeID, producttypeID,
				p.getName(), p.getBirthdate().toString(), p.getGender(), 
				""+p.getHeight(), ""+p.getWeight());
		return Response.status(201);
	}
	
	
	
	private ProductType getProducttype(int producttypeID, int fridgeID) throws JAXBException {
		return (ProductType) MyMarshaller.unmarshall("data/fridges/"+ fridgeID + "/producttypes/"+ producttypeID);
	}
	*/
}
