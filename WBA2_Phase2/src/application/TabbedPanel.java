package application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Vereint die drei Oberflächen: FridgePanel, NotificatonPanel und ProfilePanel
 * in einem TabbedPane. Zusätzlich ein Label, das anzeigt welches Profil zurzeit 
 * ausgewählt ist.
 * 
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class TabbedPanel extends JPanel {
	private JTabbedPane tp = new JTabbedPane();
	private FridgePanel f = new FridgePanel();
	private NotificationPanel n = new NotificationPanel();
	private ProfilePanel p = new ProfilePanel();
	
	public static JLabel loginLabel = new JLabel(); 

	public TabbedPanel() {
		setLayout(null);
		tp.setLocation(0, 0);
		tp.setSize(MainFrame.width, MainFrame.height);
		tp.addTab("Fridges", f);
		tp.addTab("Notifications", n);
		tp.addTab("Profiles", p);
		add(tp, null);
		loginLabel.setLocation(250, 0);
		loginLabel.setSize(MainFrame.width-250, 20);
		add(loginLabel);
	}
}
