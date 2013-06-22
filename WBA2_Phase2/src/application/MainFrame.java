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

/**
 * Startpunkt für alle weiteren Swing-Elemente, welche in dieses Fenster plaziert werden.
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class MainFrame {
	public static final int width = 480;
	public static final int height = 600;
	
	public MainFrame() {
		turnonNimbus();
		TabbedPanel r = new TabbedPanel();
		
		JFrame f = new JFrame("Fridgemanager!");
		f.getContentPane().add(r);
		f.addWindowListener(new WindowClosingAdapter(true));
		f.setLocation(100, 100);
		f.setSize(width+17, height+30); // Addition, da Fensterrahmen Pixel einnimmt
		f.setVisible(true);
		
	}
	
	private void turnonNimbus(){
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) { e.printStackTrace();
		} catch (InstantiationException e) { e.printStackTrace();
		} catch (IllegalAccessException e) { e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) { e.printStackTrace();
		}
	}
}
