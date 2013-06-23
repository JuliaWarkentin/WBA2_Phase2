package webservice.ressources;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

import webservice.Helper;
import webservice.MyMarshaller;

import com.sun.jersey.api.NotFoundException;

import jaxbClasses.CurrencyAttr;
import jaxbClasses.FridgesLOCAL;
import jaxbClasses.Product;
import jaxbClasses.ProductType;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Products;
import jaxbClasses.ProductsLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;

/**
 * Implementiert:
 * 	/products			GET, POST
 *  /products/{id}  	GET, DELETE
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
@Path ("/products")
public class ProductResource {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Products getProducts(@QueryParam("profileid") int profileID) throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.unmarshall("data/productsLOCAL.xml");
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.unmarshall("data/producttypesLOCAL.xml");
		
		Products ps = new Products();
		Products.Product psp;
		for(int i=0; i<psL.getProduct().size(); i++){
			psp = new Products.Product();
			psp.setHref("/products/"+psL.getProduct().get(i).getId()); // Hyperlink zum Produkt
			Products.Product.ProductType pt = new Products.Product.ProductType();
			int producttypeID = psL.getProduct().get(i).getProductType().getId();
			pt.setHref("/producttypes/"+producttypeID); // Hyperlink zum zugehörigen Produkttyp
			pt.setName(ProducttypeResource.getProducttypeNamebyID(producttypeID));
			Products.Product.InFridge fridge = new Products.Product.InFridge();
			int fridgeID = psL.getProduct().get(i).getFridge().getId();
			fridge.setHref("/fridges/"+fridgeID);
			fridge.setName(FridgeResource.getFridgeNamebyID(fridgeID));
			psp.setInFridge(fridge);
			psp.setProductType(pt);
			psp.setState(psL.getProduct().get(i).getState());
			ps.getProduct().add(psp);
		}
		return ps;
	}
	
	@GET
	@Path("/{productID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Product getProduct(@PathParam("productID") int productID) throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.unmarshall("data/productsLOCAL.xml");
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.unmarshall("data/producttypesLOCAL.xml");
		ProfilesLOCAL pfsL = (ProfilesLOCAL) MyMarshaller.unmarshall("data/profilesLOCAL.xml");
		
		// Liste nach passender id durchsuchen. Index merken
		ProductsLOCAL.Product pL = new ProductsLOCAL.Product();
		for (int i=0; i<psL.getProduct().size(); i++) {
			 if(psL.getProduct().get(i).getId() == productID){
				 pL = psL.getProduct().get(i);
			 	break;
			 }
		}
		
		Product p = new Product();
		Product.ProductType pt = new Product.ProductType();
		pt.setHref("/producttypes/"+pL.getProductType().getId());
		pt.setName(getProducttypeNamebyID(ptsL, pL.getProductType().getId()));
		p.setProductType(pt);
		Product.InFridge f = new Product.InFridge();
		f.setHref("/fridges/"+pL.getFridge().getId());
		f.setName(FridgeResource.getFridgeNamebyID(pL.getFridge().getId()));
		p.setInFridge(f);
		p.setInputdate(pL.getInputDate());
		if(pL.getState() == "consumed")
			p.setOutputdate(pL.getOutputDate());
		p.setExpirationdate(pL.getExpirationDate());
		Product.Owner o = new Product.Owner();
		Product.Owner.Profile op = new Product.Owner.Profile();
		op.setHref("/profiles/"+pL.getProfile().getId());
		op.setName(getProfileNamebyID(pfsL, pL.getProfile().getId()));
		o.setProfile(op);
		p.setOwner(o);
		p.setState(pL.getState());
		Product.PriceWas pw = new Product.PriceWas();
		pw.setCurrency(pL.getPriceWas().getCurrency());
		pw.setValue(pL.getPriceWas().getValue());
		p.setPriceWas(pw);
		
		return p;
	}
	
	public static String getProductStatebyID(int productID) throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.unmarshall("data/productsLOCAL.xml");
		for(int i=0; i<psL.getProduct().size(); i++) {
			if(productID == psL.getProduct().get(i).getId()) {
				return psL.getProduct().get(i).getState();
			}
		}
		System.out.println("getProductStatebyID failed...");
		return null;
	}
	
	private String getProfileNamebyID(ProfilesLOCAL pfsL, int id) {
		for(int i=0; i<pfsL.getProfile().size(); i++) {
			if(id == pfsL.getProfile().get(i).getId()) {
				return pfsL.getProfile().get(i).getName();
			}
		}
		System.out.println("getProfileNamebyID failed...");
		return null;
	}
	
	private String getProducttypeNamebyID(ProductTypesLOCAL ptsL, int id) {
		for(int i=0; i<ptsL.getProductType().size(); i++) {
			if(id == ptsL.getProductType().get(i).getId()) {
				return ptsL.getProductType().get(i).getName();
			}
		}
		System.out.println("getProducttypeNamebyID failed...");
		return null;
	}
	
	private int maxProducts = 50; // Maximale Anzahl von Produkten die insgesamt angelegt werden können
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addProduct(Product p) throws JAXBException, URISyntaxException{
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.unmarshall("data/productsLOCAL.xml");
		FridgesLOCAL fsL = (FridgesLOCAL) MyMarshaller.unmarshall("data/fridgesLOCAL.xml");
		// Nach einer freien Product-id suchen
		int freeID = -1; boolean found; String href;
		for(int i=1; i<=maxProducts && freeID==-1; i++){
			found = true;
			for(ProductsLOCAL.Product  product: psL.getProduct()){
				if(product.getId() == i) { // id belegt?
					found = false;
					break;
				}
			}
			if(found) // id noch frei?
				freeID = i; // übernehmen
		}
		if(freeID == -1) { // Keine freie id´s gefunden
			return Response.serverError().build();
		}

		// id´s aus Referenz entnehmen
		int inFridgeID = Helper.getID(p.getInFridge().getHref());
		int producttypeID = Helper.getID(p.getProductType().getHref());
		
		// Referenz auf das Produkt in entspechenden Kühlschrank speichern
		// -> Kühlschrank suchen
		for(int i=0; i < fsL.getFridge().size(); i++) {
			if(inFridgeID == fsL.getFridge().get(i).getId()) {
				// Kühlschrank gefunden. Nun Suche Produkttyp
				for(int j=0; j < fsL.getFridge().get(i).getProductTypes().getProductType().size(); j++) {
					if(producttypeID == fsL.getFridge().get(i).getProductTypes().getProductType().get(j).getId()) {
						// Produkttype gefunden. Füge Produkt-ID hinzu!
						FridgesLOCAL.Fridge.ProductTypes.ProductType.Products.Product prdct = new FridgesLOCAL.Fridge.ProductTypes.ProductType.Products.Product();
						prdct.setId(freeID);
						fsL.getFridge().get(i).getProductTypes().getProductType().get(j).getProducts().getProduct().add(prdct);
					}
				}
			}
		}
		
		// Neues Produkt anlegen
		ProductsLOCAL.Product product = new ProductsLOCAL.Product();
		product.setId(freeID);
		ProductsLOCAL.Product.ProductType pt = new ProductsLOCAL.Product.ProductType();
		pt.setId(producttypeID);
		product.setProductType(pt);
		
		ProductsLOCAL.Product.Fridge f = new ProductsLOCAL.Product.Fridge();
		href = p.getInFridge().getHref();
		f.setId(inFridgeID);
		product.setFridge(f);
		
		product.setInputDate(p.getInputdate());
		product.setExpirationDate(p.getExpirationdate());
		
		ProductsLOCAL.Product.Profile profile = new ProductsLOCAL.Product.Profile();
		href = p.getOwner().getProfile().getHref();
		profile.setId(Helper.getID(href));
		product.setProfile(profile);	// aus Hyperlink profileID bestimmen
		product.setState("inside"); 	// Eine neu angelegte Product-Instanz immer erst "inside"
		ProductsLOCAL.Product.PriceWas priceWas = new ProductsLOCAL.Product.PriceWas();
		priceWas.setCurrency(p.getPriceWas().getCurrency());
		priceWas.setValue(p.getPriceWas().getValue());
		product.setPriceWas(priceWas);
		psL.getProduct().add(product);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(psL, "data/productsLOCAL.xml");
		MyMarshaller.marshall(fsL, "data/fridgesLOCAL.xml");
		
		// Neu erstellte URI in Response angeben:
		return Response.created(new URI("/products/"+freeID)).build();
	}
	
	
	@DELETE
	@Path("/{productID}")
	public void deleteProduct(@PathParam("productID") int productID) throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.unmarshall("data/productsLOCAL.xml");
		
		//Suche Produkt
		boolean found = false;
		for(int i=0; i<psL.getProduct().size(); i++){
			if(psL.getProduct().get(i).getId() == productID) { // gefunden?
				found = true;
				psL.getProduct().remove(i); // löschen
				break;
			}
		}
		if(!found) { // Product gefunden?
			throw new NotFoundException("Product not found");
		}
		
		// Änderung übernehmen und speichern.
		MyMarshaller.marshall(psL, "data/productsLOCAL.xml");
	}
}
