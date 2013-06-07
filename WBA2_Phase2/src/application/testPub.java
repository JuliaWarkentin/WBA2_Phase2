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
		
		// Knoten "besorgen" und neues Nachricht(Item) hinzufügen
		LeafNode node = psm.getNode("testNode");
		node.publish(new Item("123abc")); // mit id
		Thread.sleep(10 * 1000);
		node.publish(new Item("124abc")); // mit id
		Thread.sleep(5 * 1000);

		System.out.println("testPub - ende");
		con.disconnect();
	}
}
