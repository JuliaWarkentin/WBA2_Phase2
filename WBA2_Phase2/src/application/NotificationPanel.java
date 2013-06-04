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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import jaxbClasses.Notifications;

import com.sun.jersey.api.client.Client;

public class NotificationPanel extends JPanel implements MouseListener{
	public static final String[] COLHEADS = { "Type", "Head", "Date" };
	
	public NotificationPanel() {
		setLayout(null);
		setLocation(150, 0);
		setSize(430, 400);
		setBackground(Color.green);
		
		JTable table = new JTable(getTableData(), COLHEADS);
		table.setColumnSelectionAllowed(false);
		table.addMouseListener(this);
		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(0, 0, 400, 200);
		
//		add(new JLabel("Notifications"));
		add(scroll);
		
		JTextArea text = new JTextArea();
		text.setText("Click a message obove.");
		text.setBounds(0, 200, 400, 200);
		add(text);
	}
	
	
	public static String[][] getTableData() {
		// .../notification Ressource anspechen
		String url = "http://"+SessionData.host+":4434/fridges/"+SessionData.fridgeID+"/notifications";
	    System.out.println("URL: " + url);
	    SessionData.wrs = Client.create().resource(url);
	    
	    // GET - daten besorgen
	    Notifications ns = SessionData.wrs.accept("application/xml").get(Notifications.class);
	    
	    // Daten auf Tabelle abbilden
	    String[][] data = new String[ns.getNotification().size()][3];
	    for(int i=0; i<ns.getNotification().size(); i++) {
	    	Notifications.Notification n = new Notifications.Notification();
	    	data[i][0] = ns.getNotification().get(i).getType();
	    	data[i][1] = ns.getNotification().get(i).getHead();
	    	data[i][2] = ns.getNotification().get(i).getDate().toString();
	    }
	    System.out.println(Arrays.deepToString(data));
		return data;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mousePressed");
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
