package application.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import application.Client;
import application.RESTHandler;
import application.swing.popups.SubscripeToFrame;

import xmpp.XMPPSession;

import jaxbClasses.Notification;
import jaxbClasses.Notifications;

/**
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class NotificationPanel extends JPanel implements MouseListener, ActionListener {
	private JLabel label = new JLabel("Notifications");
	private JButton buttonSub = new JButton("Subscribe to...");
	private JTable table; 
	private JTextArea textArea;
	private int[] notifiIDs; 
	
	public NotificationPanel() {
		setLayout(null);
		setSize(MainFrame.width, MainFrame.height);
		
		int width = MainFrame.width-5;
		
		String[] tmp = {"Type", "Head", "Date"};
		table = new JTable(getTableData(), tmp);
		table.setColumnSelectionAllowed(false);
		table.addMouseListener(this);
		JScrollPane scroll = new JScrollPane(table);
		
		textArea = new JTextArea();
		textArea.setText("Select a message...");
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		label.setBounds(0, 0, 100, 50);
		buttonSub.setBounds(width/2 - 150/2, 10, 150, 30);
		scroll.setBounds(0, 50, width, 200); // Tabelle
		textArea.setBounds(0, 250, width, 100);	// Textfeld
		
		buttonSub.addActionListener(this);
		add(label);
		add(buttonSub);
		add(scroll);
		add(textArea);
	}
	
	private String[][] getTableData() {
		notifiIDs = RESTHandler.getNotificationIDs();
		return RESTHandler.getNotificationTableData();
	}
	
	private void updateTextArea() {
		int row = table.getSelectedRow();
//		System.out.println("Column index: "+row+" | Noti-ID: "+ notifiIDs[row]);
		if(row != -1)
			textArea.setText(RESTHandler.getNotificationText(notifiIDs[row]));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == ("Subscribe to...")) {
			Client.xmpp.discoverNodes();
			SubscripeToFrame sl = new SubscripeToFrame(Client.xmpp.getNodes());
			sl.setLocation(buttonSub.getLocation());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		TabbedPanel.tp.setTitleAt(1, "Notifications");
		updateTextArea();
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	

	
}
