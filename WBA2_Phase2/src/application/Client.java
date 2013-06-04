package application;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.*;

public class Client {
	public static void main(String args[]) throws XMPPException,
			InterruptedException {
		NewProfileWindow npw = new NewProfileWindow(1, 1);
		npw.setSize(300, 400);
		npw.setVisible(true);
	}
}
