package webservice;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

import jaxbClasses.ProductInformationLOCAL;
import jaxbClasses.ProductType;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProductType.ProductInformation;


@Path ("fridges/{fridgeid}/producttypes")
public class ProducttypeService {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public ProductTypes getProducttypes(@PathParam("fridgeid") int fridgeid) throws JAXBException {
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeid + "/producttypesLOCAL.xml");
		
		// Liste der Produkttypen mit Informationen aus ProductTypesLOCAL entsprechend der producttype.xsd zusammenbauen
		ProductTypes pts = new ProductTypes();
		ProductTypes.Producttype pt;
		for(int i=0; i<ptsL.getProductType().size(); i++){
			pt = new ProductTypes.Producttype();
			pt.setHref("fridges/"+fridgeid+"/producttypes/"+ptsL.getProductType().get(i).getId());
			pt.setName(ptsL.getProductType().get(i).getName());
			pt.setStock(ptsL.getProductType().get(i).getStockData().getStock());
			pts.getProducttype().add(pt);
		}
		return pts;
	}
	
	@GET 
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public ProductType getProducttypeByID(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid) throws JAXBException  {
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeid + "/producttypesLOCAL.xml");
		
		// Liste nach passender id durchsuchen. Index merken
		int indexFound = -1;
		for (int i=0; i<ptsL.getProductType().size(); i++) {
			 if(ptsL.getProductType().get(i).getId() == id){
				 indexFound = i;
			 	break;
			 }
		}
		System.out.println(ptsL.getProductType().get(indexFound).getName());
		
		// Mittels Produktnamen Produkinformationen beziehen
		ProductInformationLOCAL piL = (ProductInformationLOCAL) MyMarshaller.
				unmarshall("data/productinformation/" + ptsL.getProductType().get(indexFound).getName() + ".xml");
		
		// REST-ProductType erstellen
		return createProductType(ptsL.getProductType().get(indexFound), piL);
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
	
	private ProductType createProductType(ProductTypesLOCAL.ProductType ptL, ProductInformationLOCAL piL) {
		
		// Namen und Beschreibung
		ProductType pt = new ProductType();
		ProductType.ProductInformation pi = new ProductType.ProductInformation();
		pi.setName(piL.getName());
		pi.setDescription(piL.getDescription());
		
		// Zutaten
		ProductType.ProductInformation.Ingredients ingredients = new ProductType.ProductInformation.Ingredients();
		ingredients.getIngredient().addAll(piL.getIngredients().getIngredient());
		pi.setIngredients(ingredients);
		
		// Nährwerte
		ProductType.ProductInformation.Nutrients nutrients = new ProductType.ProductInformation.Nutrients();
		ProductType.ProductInformation.Nutrients.CaloricValue caloricValue = new ProductType.ProductInformation.Nutrients.CaloricValue();
		caloricValue.setUnit(piL.getNutrients().getCaloricValue().getUnit()); 
		caloricValue.setValue(piL.getNutrients().getCaloricValue().getValue());
		nutrients.setCaloricValue(caloricValue);
		nutrients.setProtein(piL.getNutrients().getProtein());
		nutrients.setCarbohydrates(piL.getNutrients().getCarbohydrates());
		nutrients.setOfwichsugar(piL.getNutrients().getOfwichsugar());
		nutrients.setFat(piL.getNutrients().getFat());
		nutrients.setOfwichsaturates(piL.getNutrients().getOfwichsaturates());
		nutrients.setRoughage(piL.getNutrients().getRoughage());
		nutrients.setSodium(piL.getNutrients().getSodium());
		pi.setNutrients(nutrients);
		
		// Bestand
		ProductType.StockData stockdata = new ProductType.StockData();
		stockdata.setStock(ptL.getStockData().getStock());
		stockdata.setMinstock(ptL.getStockData().getMinstock());
		
		pt.setProductInformation(pi);
		pt.setStockData(stockdata);
		return pt;
		
	}
	
	private ProductType getProducttype(int id, int fridgeid) throws JAXBException {
		return (ProductType) MyMarshaller.unmarshall("data/fridges/"+ fridgeid + "/producttypes/"+ id);
	}
	
}
