package xmpp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.SimplePayload;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
/**
 * Klasse um den Zugriff auf XMPP zu erleichtern.
 * Jeder User erstellet eine eigene Session.
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class XMPPSession {
	private String user;
	private Connection con;
	private PubSubManager psm;
	private ServiceDiscoveryManager sdm;
	
	/**
	 * Verbindung zum XMPP Server aufbauen
	 * User und Passwort müssen den auf dem XMPP-Server enstprechen
	 * 
	 * @param user
	 * @param passw
	 */
	public XMPPSession(String user, String passw) {
		this.user = user;
		con = new XMPPConnection(XMPPData.host);
		try {
			con.connect();
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("Connection failed");
		}
		try {
			con.login(user, passw);
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("Login failed");
		}
		psm = new PubSubManager(con);
		sdm = new ServiceDiscoveryManager(con);
	}

	public void discoverServices() {
		System.out.println("Discovering Services...");
		sdm = ServiceDiscoveryManager.getInstanceFor(con);
		DiscoverItems items = null;
		try {
			items = sdm.discoverItems(XMPPData.host);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
			DiscoverItems.Item i = iter.next();
			System.out.println(i.toXML());
		}
	}

	public void discoverNodes() {
		System.out.println("Discovering Nodes...");
		sdm = ServiceDiscoveryManager.getInstanceFor(con);
		DiscoverItems items = null;
		try {
			items = sdm.discoverItems("pubsub." + XMPPData.host);
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("Discover failed");
		}
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
			DiscoverItems.Item i = iter.next();
			System.out.println(i.getEntityID());
			System.out.println(i.getNode());
			System.out.println(i.getName());
		}
	}

	/**
	 * @return Liste aller NodeID´s
	 */
	public List<String> getNodes() {
		ServiceDiscoveryManager mgr = ServiceDiscoveryManager
				.getInstanceFor(con);
		DiscoverItems items = null;
		try {
			items = mgr.discoverItems("pubsub." + XMPPData.host);
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("Discover failed");
		}
		List<String> nodeList = new ArrayList<String>();
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
			DiscoverItems.Item i = iter.next();
			nodeList.add(i.getNode());
		}
		return nodeList;
	}

	public void createNode(String nodeID) {
		// Knoten erstellen und konfigurieren
		LeafNode leaf = getNode(nodeID);
		ConfigureForm form = new ConfigureForm(FormType.submit);
		form.setAccessModel(AccessModel.open);
		form.setDeliverPayloads(true);
		form.setNotifyRetract(true);
		form.setPersistentItems(true);
		form.setPublishModel(PublishModel.open);
		try {
			leaf.sendConfigurationForm(form);
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("sendConfigurationForm failed");
		}
	}

	public void pubItemInNode(String nodeID, String payload) {
		// Knoten "besorgen" und Item hinzufügen
		LeafNode node = getNode(nodeID);
		SimplePayload simplePayload = new SimplePayload(nodeID, "pubsub:"+nodeID, payload);
		PayloadItem<SimplePayload> item = new PayloadItem<SimplePayload>("expiration" + System.currentTimeMillis(), simplePayload);
		node.publish(item);
		try {
			System.out.println("Neues Item im Knoten: "+nodeID+" veröffentlicht. Count: "+node.getItems().size());
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("Fehler beim publishen");
		}
	}

	public void subToNode(String nodeID, ItemEventListener<Item> listener) {
		// Knoten "besorgen" und abonnieren
		LeafNode node = getNode(nodeID);
		node.addItemEventListener(listener);
		try {
			node.subscribe(user+ "@" +XMPPData.host);
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("subToNode failed");
		}
	}
	
	public LeafNode getNode(String nodeID) {
		try {
			return psm.getNode(nodeID);
		} catch (XMPPException e) {
			e.printStackTrace();
			System.out.println("Knoten: "+nodeID+" konnte nicht geladen werden");
			return null;
		}
	}

	public void disconnect() {
		con.disconnect();
	}
}
