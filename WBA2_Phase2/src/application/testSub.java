package application;

import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;

public class testSub {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws XMPPException, InterruptedException {
		Connection.DEBUG_ENABLED = true;
		// Verbindung zum XMPP-server herstellen.
		Connection con = new XMPPConnection("localhost");
		con.connect();
		con.login("hans71", "hans71");

		// Pubsubmgr mit dieser Verbindung erstellen
		PubSubManager mgr = new PubSubManager(con);

		// Knoten "besorgen" und abonnieren
		LeafNode node = mgr.getNode("testNode");
		
		node.addItemEventListener(new ItemEventCoordinator());
		node.subscribe("hans71@localhost");
		
		Thread.sleep(20 * 1000);
		System.out.println("testSub - ende");
		con.disconnect();
	}
	
	public static void printNode(LeafNode node) throws XMPPException{
		System.out.println("\nKnotenID: "+ node.getId() +"     Size: "+node.getItems().size());
		System.out.print("Items: ");
		for(Item item : node.getItems()){
			System.out.print(item.getId() + " | ");
		}
		System.out.println();
	}


}
