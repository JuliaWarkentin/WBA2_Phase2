package webservice;

import javax.ws.rs.*;

@Path ("/helloworld")
public class ProfileService {
	
	
	
	@GET @Produces("text/plain")
	public String getProfilePlain(@QueryParam("name") String name){
		return "Hallo " +name;
	}
	
	@GET @Produces("text/html")
	public String getProfilePlainHtml(@QueryParam("name") String name){
		return "<html><title>text/html</title><body><h2>Html: Hallo " + name + "</h2></body></html>";
	}
}
