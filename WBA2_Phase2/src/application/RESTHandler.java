package application;

import java.util.Arrays;

import jaxbClasses.Fridge;
import jaxbClasses.Fridges;
import jaxbClasses.Notification;
import jaxbClasses.Notifications;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;

/**
 * Übernimmt sämtliche REST-Aufrufe. Bietet Funktionen an, die Informationen 
 * in diversen Datenformaten zurückgeben. (Nicht nur JAXB-Objekte)
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public final class RESTHandler {
	private RESTHandler() {} // Statische Klasse
	
	/**
	 * @param profileID Filter (queryParam)
	 * @return Liste der Kühlschranknamen zu einem Profil
	 */
	public static String[] getFridges(int profileID) {
		// GET - .../fridges/?profileid={id} (Filter auf ein Profil)
		String url = "http://" + Client.host + ":4434/fridges/?profileid="+profileID;
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		Fridges fs = Client.wrs.accept("application/xml").get(Fridges.class);
		
		String[] names = new String[fs.getFridge().size()];
		for (int i = 0; i < fs.getFridge().size(); i++) {
			names[i] = fs.getFridge().get(i).getName();
		}
		System.out.println("getFridges returns: "+Arrays.toString(names));
		return names;
	}
	
	/**
	 * @param profileID Filter (queryParam)
	 * @return Liste der Kühlschrank-ID´s zu einem Profil
	 */
	public static int[] getFridgesIDs(int profileID) {
		// GET - .../fridges/?profileid={id} (Filter auf ein Profil)
		String url = "http://" + Client.host + ":4434/fridges/?profileid="+profileID;
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		Fridges fs = Client.wrs.accept("application/xml").get(Fridges.class);
		
		int[] fridgeIDs = new int[fs.getFridge().size()];
		for (int i = 0; i < fs.getFridge().size(); i++) {
			// ID aus Referenz entnehmen
			String href = fs.getFridge().get(i).getHref();
			fridgeIDs[i] = Integer.parseInt(href.substring(href.length()-1)); 
		}
		return fridgeIDs;
	}
	
	public static String[][] getFridgeTableData(int fridgeID) {
		// GET - .../fridges/{id}
		String url = "http://"+Client.host+":4434/fridges/"+fridgeID;
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
	    Fridge f = Client.wrs.accept("application/xml").get(Fridge.class);
	    
	    // Daten auf Tabelle abbilden
	    String[][] data = new String[f.getProductTypes().getProductType().size()][2];
	    for(int i=0; i<f.getProductTypes().getProductType().size(); i++) {
	    	data[i][0] = f.getProductTypes().getProductType().get(i).getName();
	    	data[i][1] = f.getProductTypes().getProductType().get(i).getStockData().getCurrentStock()+"";
	    }
	    System.out.println("getTableData (fridgeID="+fridgeID+") returns: "+Arrays.deepToString(data));
		return data;
	}
	
	/**
	 * @return Liste aller Profilenamen
	 */
	public static String[] getProfiles() {
		// GET - .../profiles
		String url = "http://" + Client.host + ":4434/profiles";
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		Profiles ps = Client.wrs.accept("application/xml").get(Profiles.class);

		String[] names = new String[ps.getProfile().size()];
		for (int i = 0; i < ps.getProfile().size(); i++) {
			names[i] = ps.getProfile().get(i).getName();
		}
		System.out.println("getProfiles returned: "+Arrays.toString(names));
		return names;
	}
	
	/**
	 * @return Liste aller ProfilIDs
	 */
	public static int[] getProfileIDs() {
		// GET - .../profiles
		String url = "http://" + Client.host + ":4434/profiles";
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		Profiles ps = Client.wrs.accept("application/xml").get(Profiles.class);

		int[] IDs = new int[ps.getProfile().size()];
		for (int i = 0; i < ps.getProfile().size(); i++) {
			// ID aus Referenz entnehmen
			IDs[i] = getID(ps.getProfile().get(i).getHref()); 
		}
		return IDs;
	}
	
	/**
	 * @param profileID
	 * @return JAXB-Instanz eines Profils
	 */
	public static Profile getProfile(int profileID) {
		// GET - .../profiles/{id}
		String url = "http://" + Client.host + ":4434/profiles/" + profileID;
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		return Client.wrs.accept("application/xml").get(Profile.class);
	}
	
	/**
	 * @return Liste aller NorificationIDs für ein Profil
	 */
	public static int[] getNotificationIDs() {
		// GET - .../notifications
		String url = "http://"+Client.host+":4434/notifications";
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
	    Notifications ns = Client.wrs.accept("application/xml").get(Notifications.class);
	    
	    int[] IDs = new int[ns.getNotification().size()];
	    for(int i=0; i<ns.getNotification().size(); i++) {
	    	// ID aus hyperlink entnehmen
	    	IDs[i] = getID(ns.getNotification().get(i).getHref()); 
	    }
		return IDs;
	}
	
	public static String[][] getNotificationTableData() {
		// GET - .../notifications
		String url = "http://"+Client.host+":4434/notifications";
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
	    Notifications ns = Client.wrs.accept("application/xml").get(Notifications.class);
	    
	    // Daten auf Tabelle abbilden
	    String[][] data = new String[ns.getNotification().size()][3];
	    for(int i=0; i<ns.getNotification().size(); i++) {
	    	data[i][0] = ns.getNotification().get(i).getType();
	    	data[i][1] = ns.getNotification().get(i).getHead();
	    	data[i][2] = ns.getNotification().get(i).getDate().toString();
	    }
	    System.out.println("getNotificationTableDataArrays returned: " +Arrays.deepToString(data));
		return data;
	}
	
	public static String getNotificationText(int notifiID) {
		// GET - .../notifications/{id}
		String url = "http://"+Client.host+":4434/notifications/"+notifiID;
	    System.out.println("URL: " + url);
	    Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
	   
	    return Client.wrs.accept("application/xml").get(Notification.class).getText();
	}
	
	/**
	 * @param href im Format: .../xxx/{id}
	 * @return id aus dem übergebenem Hyperlink
	 */
	private static int getID(String href) {
		return Integer.parseInt(href.substring(href.lastIndexOf("/")+1));
	}
}
