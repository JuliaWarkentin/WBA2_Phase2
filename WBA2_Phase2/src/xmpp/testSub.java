package xmpp;

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

import application.ExpirationsNodeListener;


public class testSub {
	public static void main(String[] args) throws InterruptedException {
		Connection.DEBUG_ENABLED = true;
		
		XMPPSession xmpp = new XMPPSession("hans71", "hans71");
		xmpp.subToNode("expiration", new ExpirationsNodeListener());
		Thread.sleep(20 * 1000);
		xmpp.disconnect();
		
		System.out.println("testSub - ende");
		
	}
}
