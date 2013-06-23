package application;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.*;

import application.swing.MainFrame;

import com.sun.jersey.api.client.WebResource;

import xmpp.XMPPSession;

public class Client {
	public static XMPPSession xmpp;
	public static String host = "localhost";
	public static WebResource wrs;
	
	public static int profileID = 5;
	public static XMLGregorianCalendar currentDate;
	
	public Client()  {
		xmpp = new XMPPSession("hans70", "hans70");
		xmpp.discoverNodes();
		xmpp.subToNode("expiration", new ExpirationsNodeListener());
		
		try { 
			currentDate = DatatypeFactory.newInstance().newXMLGregorianCalendar("2000-01-01");
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace(); System.out.println("Error initializing currentDate...");
		}
		
		MainFrame mf = new MainFrame();
	}
}
