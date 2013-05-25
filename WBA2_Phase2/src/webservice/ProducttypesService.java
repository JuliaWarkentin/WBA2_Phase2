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

import JAXBClasses.ProductType;
import JAXBClasses.Profile;

@Path ("fridges/{fridgeid}/producttypes")
public class ProducttypesService {
	
	@GET 
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public ProductType getProducttypes(@PathParam("fridgeid") int fridgeid) {
		return null;
	}
	
	@GET 
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.TEXT_XML })
	public ProductType getProducttypeByID(@PathParam("id") int id, @PathParam("fridgeid") int fridgeid) throws JAXBException  {
		return (ProductType) MyMarshaller.unmarshall("data/fridges/"+ fridgeid + "/producttypes/"+ id + ".xml");
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
	
	private ProductType getProducttype(int id, int fridgeid) throws JAXBException {
		return (ProductType) MyMarshaller.unmarshall("data/fridges/"+ fridgeid + "/producttypes/"+ id);
	}
}
