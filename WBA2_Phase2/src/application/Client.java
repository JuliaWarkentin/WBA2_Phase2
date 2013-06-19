package application;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.*;

import com.sun.jersey.api.client.WebResource;

import xmpp.XMPPSession;

public class Client {
	public static XMPPSession xmpp;
	public static String host = "localhost";
	public static String portREST = "";
	public static String portXMPP = "";
	public static String user = "hans71";
	public static String passw = "hans71";
	public static int fridgeID = 1;
	public static  WebResource wrs;
	
	public Client() {
		xmpp = new XMPPSession("hans70", "hans70");
		xmpp.discoverNodes();
		xmpp.subToNode("expiration", new ExpirationsNodeListener());
		
		MainFrame mf = new MainFrame();
	}
}
