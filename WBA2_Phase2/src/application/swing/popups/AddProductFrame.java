package application.swing.popups;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import jaxbClasses.CurrencyAttr;
import jaxbClasses.Product;

import application.Client;
import application.RESTHandler;
import application.swing.FridgePanel;
import application.swing.MainFrame;
import application.swing.WindowClosingAdapter;

import com.sun.jersey.api.client.ClientResponse;

public class AddProductFrame extends JFrame implements ActionListener {
	// Label und Anzahl des Produkts
	String productName;
	JLabel labelProducttyp = new JLabel("Added Amound: ");
	JSpinner spinnerAmount = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
	
	// Verfallsdatum
	JLabel labelExpDate = new JLabel("Expirationdate: ");
	JSpinner spinnerDate = new JSpinner(new SpinnerDateModel());
	
	// Preis
	JLabel labelPrice = new JLabel("Price was (€): ");
	JSpinner spinnerPrice = new JSpinner(new SpinnerNumberModel(1d, 1d, 20d, 0.1d));
	
	// Buttons
	JButton buttonAdd = new JButton("Add");
	JButton buttonBack = new JButton("Close");
	
	// Bestätigung
	JLabel labelConfirm = new JLabel("");
	
	public AddProductFrame(String productName) {
		super("Adding \""+ productName +"\"");
		this.productName = productName;
		setLayout(null);
		setSize(250, 175);
		setVisible(true);
		addWindowListener(new WindowClosingAdapter(false));
		
		int x = 10, y = 10, width = 100, height = 25, yOffset = 25, xOffset = width+x;
		
		// Label und Anzahl des Produkts
		labelProducttyp.setBounds(x+xOffset*0, y+yOffset*0, width, height);
		spinnerAmount.setBounds(x+xOffset*1, y+yOffset*0, width, height);
		add(labelProducttyp);
		add(spinnerAmount);
		
		// Verfallsdatum
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "yyyy.MM.dd"));
		labelExpDate.setBounds(x+xOffset*0, y+yOffset*1, width, height);
		spinnerDate.setBounds(x+xOffset*1, y+yOffset*1, width, height);
		add(labelExpDate);
		add(spinnerDate);
		
		// Preis
		labelPrice.setBounds(x+xOffset*0, y+yOffset*2, width, height);
		spinnerPrice.setBounds(x+xOffset*1, y+yOffset*2, width, height);
		add(labelPrice);
		add(spinnerPrice);
		
		// Buttons
		buttonAdd.setBounds(x+xOffset*0, y+yOffset*3, width, height);
		buttonBack.setBounds(x+xOffset*1, y+yOffset*3, width, height);
		buttonAdd.addActionListener(this);
		add(buttonAdd);
		buttonBack.addActionListener(this);
		add(buttonBack);
		
		labelConfirm.setBounds(x+xOffset*0, y+yOffset*4, width*2, height);
		labelConfirm.setForeground(MainFrame.colorSuccess);
		add(labelConfirm);
	}

	private void addProducts() {
		// Verfallsdatum aus spinnerDate entnehmen und zu XMLGregorianCalendar formatieren
		Date d = (Date)(spinnerDate.getModel().getValue());
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(d);
		
		XMLGregorianCalendar expDate = null;
		try {
			expDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			expDate.setTime(0, 0, 0);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		int amount = Integer.parseInt(""+spinnerAmount.getModel().getValue());
		int createdCnt = 0;
		for(int i=0; i<amount; i++) {
			if(RESTHandler.addProduct(FridgePanel.selectedProducttypeID, 
					FridgePanel.selectedFridgeID, 
					Client.profileID, 
					Client.currentDate, 
					expDate, 
					Float.parseFloat(""+spinnerPrice.getValue())) == 201) // Erfolgreich angelegt?
				createdCnt++; 
		}
		
		// Erfolgsmeldung anzeigen
		labelConfirm.setText("+"+createdCnt+" '"+productName+"' successfully added!");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand() == "Add") {
			addProducts();
		}
	}
	
	
}
