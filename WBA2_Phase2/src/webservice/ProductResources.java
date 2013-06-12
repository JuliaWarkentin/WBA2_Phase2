package webservice;

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

import com.sun.jersey.api.NotFoundException;

import jaxbClasses.CurrencyAttr;
import jaxbClasses.Product;
import jaxbClasses.ProductType;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Products;
import jaxbClasses.ProductsLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;


@Path ("/products")
public class ProductResources {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Products getProducts() throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.unmarshall("data/productsLOCAL.xml");
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.unmarshall("data/producttypesLOCAL.xml");
		
		Products ps = new Products();
		Products.Product psp;
		for(int i=0; i<psL.getProduct().size(); i++){
			psp = new Products.Product();
			psp.setHref("/products/"+psL.getProduct().get(i).getId()); // Hyperlink zum Produkt
			Products.Product.ProductType pt = new Products.Product.ProductType();
			int ptID = psL.getProduct().get(i).getProductType().getId();
			pt.setHref("/producttypes/"+ptID); // Hyperlink zum zugehörigen Produkttyp
			pt.setName(getNameFromProductTypesLOCALbyID(ptsL.getProductType(), ptID));
			psp.setProductType(pt);
			psp.setState(psL.getProduct().get(i).getState());
			ps.getProduct().add(psp);
		}
		return ps;
	}
	
	private String getNameFromProductTypesLOCALbyID(List<ProductTypesLOCAL.ProductType> list, int id) {
		for(ProductTypesLOCAL.ProductType pt : list){
			if(pt.getId() == id)
				return pt.getName();
		}
		System.out.println("getNameFromProductTypesLOCALbyID fehlgeschlagen!");
		return null;
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
		int indexFound = -1;
		for (int i=0; i<psL.getProduct().size(); i++) {
			 if(psL.getProduct().get(i).getId() == productID){
				 pL = psL.getProduct().get(i);
				 indexFound = i;
			 	break;
			 }
		}
		
		Product p = new Product();
		Product.ProductType pt = new Product.ProductType();
		pt.setHref("/producttypes/"+pL.getProductType().getId());
		pt.setName(getProducttypeNamebyID(ptsL, pL.getProductType().getId()));
		p.setProductType(pt);
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
	
	
	/*
	@POST
	@Consumes({ MediaType.APPLICATION_XML})
	public Response addProduct(@PathParam("fridgeID") int fridgeID, @PathParam("producttypeID") int producttypeID, Product p) throws JAXBException, URISyntaxException{
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/productsLOCAL.xml");
		
		// Nach einer freien Product-id suchen
		int freeID = -1; boolean found;
		for(int i=1; i<=50 && freeID==-1; i++){
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
		
		// Neues Product anlegen
		ProductsLOCAL.Product product = new ProductsLOCAL.Product();
		product.setId(freeID);
		product.setProductTypeID(producttypeID);
		product.setInputDate(p.getInputdate());
		product.setExpirationDate(p.getExpirationdate());
		String url = p.getOwner().getHref();
		product.setOwnerID(Integer.parseInt(url.substring(url.length()-1)));	// aus Hyperlink OwnerID bestimmen
		product.setState("inside"); 	// Eine neu angelegte Product-Instanz immer erst "inside"
		ProductsLOCAL.Product.PriceWas priceWas = new ProductsLOCAL.Product.PriceWas();
		priceWas.setCurrency(CurrencyAttr.EUR);
		priceWas.setValue(1.39f);
		product.setPriceWas(priceWas);
		psL.getProduct().add(product);
		
		// Daten auf Platte speichern
		MyMarshaller.marshall(psL, "data/fridges/"+ fridgeID + "/productsLOCAL.xml");
		
		// Neu erstellte URI in Response angeben:
		return Response.created(new URI("fridges/"+fridgeID+"/producttype/"+producttypeID+"/products/"+freeID)).build();
	}
	
	@DELETE
	@Path("/{productID}")
	public void deleteProduct(@PathParam("productID") int productID, @PathParam("producttypeID") int producttypeID, @PathParam("fridgeID") int fridgeID) throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/productsLOCAL.xml");
		
		//Suche Produkt
		boolean found = false;
		for(int i=0; i<psL.getProduct().size(); i++){
			if(psL.getProduct().get(i).getId() == productID && 
					psL.getProduct().get(i).getProductTypeID() == producttypeID) { // gefunden?
				found = true;
				psL.getProduct().remove(i); // löschen
				break;
			}
		}
		if(!found) { // Product gefunden?
			throw new NotFoundException("Product not found");
		}
		
		// Änderung übernehmen und speichern.
		MyMarshaller.marshall(psL, "data/fridges/"+ fridgeID + "/productsLOCAL.xml");
	}
	
	private String getProfileNameByID(int profileid, int fridgeID) throws JAXBException{
		ProfilesLOCAL psL = (ProfilesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/profilesLOCAL.xml");
		for(ProfilesLOCAL.Profile p : psL.getProfile()){
			if(p.getId() == profileid)
				return p.getName();
		}
		System.out.println("getProfileNameByID fehlgeschlagen!");
		return null;
	}*/
}
