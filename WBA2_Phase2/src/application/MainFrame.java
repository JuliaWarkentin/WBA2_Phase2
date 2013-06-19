package application;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.*;


public class MainFrame {
	public static int width = 480;
	public static int height = 600;
	
	
	public MainFrame() {
//		MenuPanel m = new MenuPanel();
//		NotificationPanel n = new NotificationPanel();
		TabbedPanel r = new TabbedPanel();
		
		JFrame f = new JFrame("Fridgemanager!");
//		f.getContentPane().add(m);
//		f.getContentPane().add(n);
		f.getContentPane().add(r);
		f.addWindowListener(new WindowClosingAdapter(true));
		f.setLocation(100, 100);
		f.setSize(width+17, height+30); // Addition, da Fensterrahmen Pixel einnimmt
		f.setVisible(true);
		turnonNimbus();
	}
	
	private void turnonNimbus(){
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
