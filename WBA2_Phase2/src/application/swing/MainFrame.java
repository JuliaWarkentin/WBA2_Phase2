package application.swing;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
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
	public static final int width = 480, height =600,
			posX = 100, posY = 100;
	public static Color colorError = new Color(150,0,0);
	public static Color colorSuccess = new Color(0,150,0);
	
	public static JFrame frame;
	public MainFrame() {
		setSwingDesign(false); // bitte true setzen, falls Design nicht gefunden wird oder verkehrt aussieht. 
		TabbedPanel r = new TabbedPanel();
		
		frame = new JFrame("Fridgemanager!");
		frame.getContentPane().add(r);
		frame.addWindowListener(new WindowClosingAdapter(true));
		frame.setLocation(posX, posY);
		frame.setSize(width+17, height+30); // Addition, da Fensterrahmen Pixel einnimmt
		frame.setVisible(true);
	}
	
	private void setSwingDesign(boolean crossPlatform) {
		if (!crossPlatform) {
			try { 
				for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) { // Schickes "Nimbus" Design
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (Exception e) {}
		}else {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception ex) {}
		}
	}
}
