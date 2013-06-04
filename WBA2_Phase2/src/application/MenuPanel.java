package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MenuPanel extends JPanel implements ActionListener{
	
	JButton buttonProducts = new JButton("Products");
	JButton buttonNotifi = new JButton("Notifications");
	JButton buttonProfiles = new JButton("Profiles");
	
	public MenuPanel() {
		setLayout(null);
		setSize(150, 400);
//		setBackground(Color.red);
		
		int x = 10, y = 45, yOffset = 20;
		int width = 110, height = 30;
		buttonProducts.setBounds(x, 0*y+yOffset, width, height);
		buttonNotifi.setBounds(x, 1*y+yOffset, width, height);
		buttonProfiles.setBounds(x, 2*y+yOffset, width, height);
		
		buttonNotifi.addActionListener(this);
		buttonProducts.addActionListener(this);
		buttonProfiles.addActionListener(this);
		
		add(new JLabel("Options:"));
		add(buttonNotifi);
		add(buttonProducts);
		add(buttonProfiles);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		
	}
	
	public static void main(String[] args) {
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
		MenuPanel m = new MenuPanel();
		NotificationPanel n = new NotificationPanel();
		
		JFrame f = new JFrame("Fridgemanager!");
		f.getContentPane().add(m);
		f.getContentPane().add(n);
		f.getContentPane().add(new JLabel("asd"));
		f.addWindowListener(new WindowClosingAdapter(true));
		f.setLocation(100, 100);
		f.setSize(600, 400);
		f.setVisible(true);
		
	 }
}
