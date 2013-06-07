package application;

import java.awt.BorderLayout;
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

import jaxbClasses.Notification;
import jaxbClasses.Notifications;

import com.sun.jersey.api.client.Client;

public class NotificationPanel extends JPanel implements MouseListener, ActionListener{
	public static final String[] COLHEADS = { "Type", "Head", "Date" };
	JLabel label = new JLabel("Notifications");
	JButton buttonSub = new JButton("Subscribe to...");
	JTable table;
	JScrollPane scroll;
	JTextArea textArea;
	
	XmppSession xmpp;
	
	
	public NotificationPanel(XmppSession xmpp) {
		this.xmpp = xmpp;
		
		int width = 430;
		
		setLayout(null);
		setLocation(150, 0);
		setSize(width, 400);
//		setBackground(Color.green);
		
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
	public String[][] getTableData() {
		// .../notification Ressource anspechen
		String url = "http://"+SessionData.host+":4434/fridges/"+SessionData.fridgeID+"/notifications";
	    System.out.println("URL: " + url);
	    SessionData.wrs = Client.create().resource(url);
	    
	    // GET - daten besorgen
	    Notifications ns = SessionData.wrs.accept("application/xml").get(Notifications.class);
	    
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
	    	tableIDs[i] = Integer.parseInt(href.substring(href.length()-1)); System.out.println(tableIDs[i]);
	    }
	    System.out.println(Arrays.deepToString(data));
		return data;
	}
	
	public void updateTextArea() {
		int column = table.getSelectedRow();
		System.out.println("Column index: "+column+" | Noti-ID: "+ tableIDs[column]);
		
		// .../notification/{id} Ressource anspechen
		String url = "http://"+SessionData.host+":4434/fridges/"+SessionData.fridgeID+"/notifications/"+ tableIDs[column];
	    System.out.println("URL: " + url);
	    SessionData.wrs = Client.create().resource(url);
	    
	    // GET - daten besorgen
	    Notification ns = SessionData.wrs.accept("application/xml").get(Notification.class);
	    
	    this.textArea.setText(ns.getText());
	    
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == ("Subscribe to...")) {
			SubscriptionList sl = new SubscriptionList(xmpp.getNodes());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
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
