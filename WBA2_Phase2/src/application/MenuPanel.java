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

import org.jivesoftware.smack.XMPPException;

public class MenuPanel extends JPanel implements ActionListener{
	
	JButton buttonProducts = new JButton("Products");
	JButton buttonNotifi = new JButton("Notifications");
	JButton buttonProfiles = new JButton("Profiles");
	
	public MenuPanel() {
		setLayout(new FlowLayout());
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
}
