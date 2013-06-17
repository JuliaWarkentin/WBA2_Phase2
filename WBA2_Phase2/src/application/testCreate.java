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
		XmppSession xmpp = new XmppSession("hans69", "hans69");
		xmpp.createNode("expiration");
		xmpp.discoverNodes();
		xmpp.disconnect();
		System.out.println("testCreate - ende");
	}
}
