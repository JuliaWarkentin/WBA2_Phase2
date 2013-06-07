package application;

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
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;

public class XmppSession {
	private String host = "localhost";
	private String user;
	private Connection con;
	private PubSubManager psm;
	private ServiceDiscoveryManager mgr;
	
	public XmppSession(String user, String passw) {
		// Verbindung zum XMPP-server herstellen.
		this.user = user;
		con = new XMPPConnection(host);
		try {
			con.connect();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); System.out.println("Connection failed");
		}
		try {
			con.login(user, passw);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); System.out.println("login failed");
		}
		psm = new PubSubManager(con);
		mgr = new ServiceDiscoveryManager(con);
	}
	
	public void discoverServices() throws XMPPException {
		System.out.println("Discovering Services...");
		mgr = ServiceDiscoveryManager.getInstanceFor(con);
		DiscoverItems items = mgr.discoverItems(host);
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
		   DiscoverItems.Item i = iter.next();
		   System.out.println(i.toXML());
		}
	}
	
	public void discoverNodes() {
		System.out.println("Discovering Nodes...");
		ServiceDiscoveryManager mgr = ServiceDiscoveryManager.getInstanceFor(con);
		DiscoverItems items = null;
		try {
			items = mgr.discoverItems("pubsub."+host);
		} catch (XMPPException e) {
			e.printStackTrace(); System.out.println("Discover failed");
		}
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
		   DiscoverItems.Item i = iter.next();
		   System.out.println(i.getEntityID());
		   System.out.println(i.getNode());
		   System.out.println(i.getName());
		}
	}
	
	public List<String> getNodes() {
		ServiceDiscoveryManager mgr = ServiceDiscoveryManager.getInstanceFor(con);
		DiscoverItems items = null;
		try {
			items = mgr.discoverItems("pubsub."+host);
		} catch (XMPPException e) {
			e.printStackTrace(); System.out.println("Discover failed");
		}
		List<String> nodeList = new ArrayList<String>();
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
		   DiscoverItems.Item i = iter.next();
		   nodeList.add(i.getNode());
		}
		return nodeList;
	}
	
	public void createNode(String nodeID) throws XMPPException {
		// Knoten erstellen und konfigurieren
		LeafNode leaf = psm.createNode(nodeID);
		ConfigureForm form = new ConfigureForm(FormType.submit);
		form.setAccessModel(AccessModel.open);
		form.setDeliverPayloads(false);
		form.setNotifyRetract(true);
		form.setPersistentItems(true);
		form.setPublishModel(PublishModel.open);
		leaf.sendConfigurationForm(form);
	}
	
	public void pubItemInNode(String nodeID, int itemID) throws XMPPException {
		// Knoten "besorgen" und neues Nachricht(Item) hinzufügen
		LeafNode node = psm.getNode("testNode");
		node.publish(new Item("123abc")); // mit id
	}
	
	public void subToNode(String nodeID) throws XMPPException {
		// Knoten "besorgen" und abonnieren
		LeafNode node = psm.getNode(nodeID);
		node.addItemEventListener(new ItemEventCoordinator());
		node.subscribe(user+"@localhost");
	}
	
	public void disconnect() {
		con.disconnect();
	}
}
