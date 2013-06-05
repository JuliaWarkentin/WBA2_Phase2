package application;

import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.NodeInformationProvider;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo.Identity;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;

public class testPub {

	/**
	 * @param args
	 * @throws XMPPException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws XMPPException,
			InterruptedException {
		// Verbindung zum XMPP-server herstellen.
		Connection con = new XMPPConnection("localhost");
		con.connect();
		con.login("hans70", "hans70");

		// Pubsubmgr mit dieser Verbindung erstellen
		PubSubManager psm = new PubSubManager(con);
//		System.out.println(psm.discoverNodes("testNode").);;
		discoverServices(con);
		discoverNodes(con);
		
		// Knoten "besorgen" und neues Nachricht(Item) hinzufügen
		LeafNode node = psm.getNode("testNode");
		node.publish(new Item("123abc")); // mit id
		Thread.sleep(10 * 1000);
		node.publish(new Item("124abc")); // mit id
		Thread.sleep(5 * 1000);

		System.out.println("testPub - ende");
		con.disconnect();
	}

	public static void discoverServices(Connection con) throws XMPPException {
		System.out.println("Discovering Services...");
		ServiceDiscoveryManager mgr = ServiceDiscoveryManager.getInstanceFor(con);
		DiscoverItems items = mgr.discoverItems("localhost");
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
		   DiscoverItems.Item i = iter.next();
		   System.out.println(i.toXML());
		}
	}
	
	public static void discoverNodes(Connection con) throws XMPPException {
		System.out.println("Discovering Nodes...");
		ServiceDiscoveryManager mgr = ServiceDiscoveryManager.getInstanceFor(con);
		DiscoverItems items = mgr.discoverItems("pubsub.localhost");
		Iterator<DiscoverItems.Item> iter = items.getItems();
		while (iter.hasNext()) {
		   DiscoverItems.Item i = iter.next();
		   System.out.println(i.getEntityID());
		   System.out.println(i.getNode());
		   System.out.println(i.getName());
		}
	}

}
