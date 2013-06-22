package application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import jaxbClasses.CurrencyAttr;
import jaxbClasses.Product;

import com.sun.jersey.api.client.ClientResponse;

public class AddProductFrame extends JFrame implements ActionListener {
	// Label und Anzahl des Produkts
	JLabel labelProducttyp = new JLabel("Added Amound: ");
	JSpinner spinnerCount = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
	
	// Verfallsdatum
	JLabel labelExpDate = new JLabel("Expirationdate: ");
	JSpinner spinnerDate = new JSpinner(new SpinnerDateModel());
	
	// Preis
	JLabel labelPrice = new JLabel("Price was: ");
	JSpinner spinnerPrice = new JSpinner(new SpinnerNumberModel(1d, 1d, 20d, 0.10d));
	
	// Buttons
	JButton buttonAdd = new JButton("Add");
	JButton buttonBack = new JButton("Close");
	
	// Bestätigung
	JLabel labelCorfirm = new JLabel("jhgfg");
	
	public AddProductFrame(String producttyp) {
		super("Adding \""+ producttyp+"\"");
		setLayout(null);
		setSize(250, 175);
		setLocation(100, 100);
		setVisible(true);
		addWindowListener(new WindowClosingAdapter(false));
		
		int x = 10, y = 10, width = 100, height = 25, yOffset = 25, xOffset = width+x;
		
		// Label und Anzahl des Produkts
		labelProducttyp.setBounds(x+xOffset*0, y+yOffset*0, width, height);
		spinnerCount.setBounds(x+xOffset*1, y+yOffset*0, width, height);
		add(labelProducttyp);
		add(spinnerCount);
		
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
		
		// Schalter
		buttonAdd.setBounds(x+xOffset*0, y+yOffset*3, width, height);
		buttonBack.setBounds(x+xOffset*1, y+yOffset*3, width, height);
		buttonAdd.addActionListener(this);
		add(buttonAdd);
		buttonBack.addActionListener(this);
		add(buttonBack);
		
		labelCorfirm.setBounds(x+xOffset*0, y+yOffset*4, width, height);
		labelCorfirm.setForeground(new Color(50, 255, 50));
		add(labelCorfirm);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.getActionCommand());
	}
	
	private String addProduct(int producttypeID, int fridgeID, int profileID,
			XMLGregorianCalendar inputDate, XMLGregorianCalendar expirationDate,
			float priceWas) {
		// POST - .../products
		String url = "http://localhost:4434/products";
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		
		Product p = new Product();
		
		Product.ProductType pt = new Product.ProductType();
		pt.setHref("/producttypes/2"); 
		pt.setName("ignored");
		p.setProductType(pt);
		
		Product.InFridge f = new Product.InFridge();
		f.setHref("/fridges/1");
		f.setName("ignored");
		p.setInFridge(f);
		
//		p.setInputdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2000-01-01"));
//		p.setExpirationdate(DatatypeFactory.newInstance().newXMLGregorianCalendar("2000-01-02"));
		Product.Owner po = new Product.Owner();
		Product.Owner.Profile pro = new Product.Owner.Profile();
		pro.setHref("/profile/4");
		pro.setName("willbeignored");
		po.setProfile(pro);
		p.setOwner(po);
		Product.PriceWas priceWas = new Product.PriceWas();
		priceWas.setCurrency(CurrencyAttr.EUR);
		priceWas.setValue(0.99f);
		p.setPriceWas(priceWas);
		
		ClientResponse r = Client.wrs.type(MediaType.APPLICATION_XML).post(ClientResponse.class, p);
	    System.out.println(r.toString());
	    
	    return null;
	}
}
