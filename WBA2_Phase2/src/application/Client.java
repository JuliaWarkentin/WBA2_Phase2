package application;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.*;

public class Client {
	public static void main(String args[]) throws XMPPException, InterruptedException {
		XmppSession xmpp = new XmppSession("hans69", "hans69");
		xmpp.discoverNodes();
		
		turnonNimbus();
		MenuPanel m = new MenuPanel();
		NotificationPanel n = new NotificationPanel(xmpp);
		
		JFrame f = new JFrame("Fridgemanager!");
		f.getContentPane().add(m);
		f.getContentPane().add(n);
		f.getContentPane().add(new JLabel("asd"));
		f.addWindowListener(new WindowClosingAdapter(true));
		f.setLocation(100, 100);
		f.setSize(600, 400);
		f.setVisible(true);
	}
	
	public static void turnonNimbus(){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
