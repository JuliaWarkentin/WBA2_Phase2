package application;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;

public class testCreate {
	
	/**
	 * @param args
	 * @throws XMPPException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws XMPPException, InterruptedException {
		// Verbindung zum XMPP-server herstellen.
		Connection con = new XMPPConnection("localhost");
		con.connect();
		con.login("hans69", "hans69");
		
		// Pubsubmgr mit dieser Verbindung erstellen
		PubSubManager mgr = new PubSubManager(con);

		// Knoten erstellen und konfigurieren
		LeafNode leaf = mgr.createNode("testNode2");
		ConfigureForm form = new ConfigureForm(FormType.submit);
		form.setAccessModel(AccessModel.open);
		form.setDeliverPayloads(false);
		form.setNotifyRetract(true);
		form.setPersistentItems(true);
		form.setPublishModel(PublishModel.open);
		leaf.sendConfigurationForm(form);
		
		Thread.sleep(10 * 1000);
		System.out.println("testCreate - ende");
		con.disconnect();
	}
}
