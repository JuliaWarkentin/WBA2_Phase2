package webservice;

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

import com.sun.jersey.api.NotFoundException;

import jaxbClasses.Fridge;
import jaxbClasses.Fridges;
import jaxbClasses.FridgesLOCAL;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.ProfilesLOCAL;

@Path ("/fridges")
public class FridgeResource {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Fridges getFridges() throws JAXBException, IOException {
		System.out.println("getFridges");
		FridgesLOCAL fsL = (FridgesLOCAL) MyMarshaller.unmarshall("data/fridgesLOCAL.xml");
		
		// Fridgeliste entsprechend der fridges.xsd erstellen
		Fridges fs = new Fridges();
		for(int i=0; i<fsL.getFridge().size();i++) {
			Fridges.Fridge f = new Fridges.Fridge();
			f.setHref("/fridges/"+fsL.getFridge().get(i).getId());
			f.setName(fsL.getFridge().get(i).getName());
			fs.getFridge().add(f);
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
		System.out.println("getFridge");
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
				String state = ProductResources.getProductStatebyID(pdsL.getProduct().get(j).getId());
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
		f.setProductTypes(pts);
		return f;
	}
}
