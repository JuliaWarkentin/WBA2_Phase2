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
import jaxbClasses.ProductInformationLOCAL;
import jaxbClasses.ProductType;
import jaxbClasses.ProductTypes;
import jaxbClasses.ProductTypesLOCAL;
import jaxbClasses.Products;
import jaxbClasses.ProductsLOCAL;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;
import jaxbClasses.ProfilesLOCAL;
import jaxbClasses.ProductType.ProductInformation;


@Path ("fridges/{fridgeID}/producttypes/{producttypeID}/products")
public class ProductResources {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Products getProducts(@PathParam("fridgeID") int fridgeID, @PathParam("producttypeID") int producttypeID) throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/productsLOCAL.xml");
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/producttypesLOCAL.xml");
		
		Products ps = new Products();
		Products.Product psp;
		Products.Product.Name pspn;
		for(int i=0; i<psL.getProduct().size(); i++){
			if(psL.getProduct().get(i).getProductTypeID() == producttypeID) {
				psp = new Products.Product();
				pspn = new Products.Product.Name();
				// href zur Produktinstanz für mehr Informationen
				psp.setHref("fridges/"+fridgeID+"/producttypes/"+ psL.getProduct().get(i).getProductTypeID() +"/products/"+ psL.getProduct().get(i).getId());
				// href zum Produkttypen mit produktnamen
				pspn.setHref("fridges/"+fridgeID+"/producttypes/"+psL.getProduct().get(i).getProductTypeID());
				pspn.setValue(getNameFromProductTypesLOCALbyID(ptsL.getProductType(), psL.getProduct().get(i).getProductTypeID()));
				psp.setName(pspn);
				
				psp.setInputdate(psL.getProduct().get(i).getInputDate());
				psp.setState(psL.getProduct().get(i).getState());
				
				ps.getProduct().add(psp);
			}
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
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public Product getProduct(@PathParam("id") int id, @PathParam("fridgeID") int fridgeID, @PathParam("producttypeID") int producttypeID) throws JAXBException {
		ProductsLOCAL psL = (ProductsLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/productsLOCAL.xml");
		ProductTypesLOCAL ptsL = (ProductTypesLOCAL) MyMarshaller.
				unmarshall("data/fridges/"+ fridgeID + "/producttypesLOCAL.xml");
		
		// Liste nach passender id durchsuchen. Index merken
		int indexFound = -1;
		for (int i=0; i<psL.getProduct().size(); i++) {
			 if(psL.getProduct().get(i).getId() == id){
				 indexFound = i;
			 	break;
			 }
		}
		
		// Passt gefundendes Produkt zum produkttypen?
		if (psL.getProduct().get(indexFound).getProductTypeID() != producttypeID) {
			System.out.println("Angefordertes Produkt passt nicht zum Produkttypen");
			return null;
		}
		
		Product p = new Product();
		Product.Name pn = new Product.Name();
		pn.setHref("fridges/"+fridgeID+"/producttypes/"+psL.getProduct().get(indexFound).getProductTypeID());
		pn.setValue(getNameFromProductTypesLOCALbyID(ptsL.getProductType(), psL.getProduct().get(indexFound).getProductTypeID()));
		p.setName(pn);
		p.setInputdate(psL.getProduct().get(indexFound).getInputDate());
		if(psL.getProduct().get(indexFound).getState() == "consumed")
			p.setOutputdate(psL.getProduct().get(indexFound).getOutputDate());
		p.setExpirationdate(psL.getProduct().get(indexFound).getExpirationDate());
		Product.Owner po = new Product.Owner();
		po.setHref("fridges/"+fridgeID+"/profiles/"+psL.getProduct().get(indexFound).getOwnerID());
		po.setValue(getProfileNameByID(psL.getProduct().get(indexFound).getOwnerID(), fridgeID));
		p.setOwner(po);
		p.setState(psL.getProduct().get(indexFound).getState());
		Product.PriceWas pp = new Product.PriceWas();
		pp.setCurrency(psL.getProduct().get(indexFound).getPriceWas().getCurrency());
		pp.setValue(psL.getProduct().get(indexFound).getPriceWas().getValue());
		p.setPriceWas(pp);
		
		return p;
	}
	
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
	}
}
