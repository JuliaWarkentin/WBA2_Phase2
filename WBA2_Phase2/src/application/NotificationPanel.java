package application;

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

import xmpp.XMPPSession;

import jaxbClasses.Notification;
import jaxbClasses.Notifications;

//import com.sun.jersey.api.client.Client;

public class NotificationPanel extends JPanel implements MouseListener, ActionListener{
	String[] COLHEADS = { "Type", "Head", "Date" };
	JLabel label = new JLabel("Notifications");
	JButton buttonSub = new JButton("Subscribe to...");
	JTable table;
	JScrollPane scroll;
	JTextArea textArea;
	
	public NotificationPanel() {
		int width = MainFrame.width-5;
		
		setLayout(null);
//		setLocation(150, 100);
		setSize(MainFrame.width, MainFrame.height);
		setBackground(Color.green);
		
		table = new JTable(getTableData(), COLHEADS);
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
	
	int[] tableIDs;
	private String[][] getTableData() {
		// GET - .../notifications
		String url = "http://"+Client.host+":4434/notifications";
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
	    Notifications ns = Client.wrs.accept("application/xml").get(Notifications.class);
	    
	    // Daten auf Tabelle abbilden
	    String[][] data = new String[ns.getNotification().size()][3];
	    tableIDs = new int[ns.getNotification().size()];
	    for(int i=0; i<ns.getNotification().size(); i++) {
	    	Notifications.Notification n = new Notifications.Notification();
	    	data[i][0] = ns.getNotification().get(i).getType();
	    	data[i][1] = ns.getNotification().get(i).getHead();
	    	data[i][2] = ns.getNotification().get(i).getDate().toString();
	    	// ID aus hyperlink speichern
	    	String href = ns.getNotification().get(i).getHref();
	    	tableIDs[i] = Integer.parseInt(href.substring(href.length()-1)); 
	    }
	    System.out.println(Arrays.deepToString(data));
		return data;
	}
	
	private void updateTextArea() {
		int column = table.getSelectedRow();
		System.out.println("Column index: "+column+" | Noti-ID: "+ tableIDs[column]);
		
		// GET - .../notifications/{id}
		String url = "http://"+Client.host+":4434/notifications/"+ tableIDs[column];
	    System.out.println("URL: " + url);
	    Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
	    
	   
	    Notification ns = Client.wrs.accept("application/xml").get(Notification.class);
	    
	    this.textArea.setText(ns.getText());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == ("Subscribe to...")) {
			Client.xmpp.discoverNodes();
			SubscriptionList sl = new SubscriptionList(Client.xmpp.getNodes());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
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
