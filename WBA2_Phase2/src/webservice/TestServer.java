package webservice;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;;

public class TestServer {
	
	public static void main(String[] args) throws Exception {
		ProfileService.readXML();
		
		String url = (args.length > 0) ? args[0] : "http://localhost:4434";
	    String sec = (args.length > 1) ? args[1] : "60";

	    SelectorThread srv = GrizzlyServerFactory.create(url);

	    System.out.println("URL: " + url);
	    Thread.sleep( 1000 * Integer.parseInt(sec));
	    srv.stopEndpoint();
	}
}
