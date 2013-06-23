package application.swing;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import application.Client;
import application.RESTHandler;
import application.swing.popups.AddProductFrame;

import jaxbClasses.Fridge;
import jaxbClasses.Fridges;
import jaxbClasses.Notifications;
import jaxbClasses.Profiles;

public class FridgePanel extends JPanel implements ActionListener, FocusListener {
	public static int selectedFridgeID;
	public static int selectedProducttypeID;
	
	// Fridgeauswahl
	private JLabel labelFridgelist = new JLabel("Related Fridges");
	private JList<String> listFridges;
	private JButton buttonSelect = new JButton("Select");
	private JScrollPane scrollList;
	private boolean isFridgeSelected;
	private int[] fridgeIDs; // zu wissen, welche Zeile welcher ID entspricht
	
	// Optionen
	private JLabel labelSelectedFridge = new JLabel("Please select a Fridge...");
	private ButtonGroup groupShow = new ButtonGroup();
	private JRadioButton rbShowMine = new JRadioButton("Show only my own Products");
	private JRadioButton rbShowAll = new JRadioButton("Show all Products in this Fridge");
	
	// Produkte
	private String[] columnNames = {"Producttype", "Stock"};
	private JTable table; 
	private JScrollPane scrollTable;
	private int[] producttypeIDs; 
	
	private JButton buttonAdd = new JButton("Add Product");
	private JButton buttonConsume = new JButton("Consume Product");
	
	// Errorlabel
	private JLabel labelError = new JLabel("");
	
	public FridgePanel() {
		setLayout(null);
		setSize(MainFrame.width, MainFrame.height);
//		setBackground(Color.white);
		
		// Fridgeliste
		labelFridgelist.setLocation(0, 0);
		labelFridgelist.setSize(100, 25);
		listFridges = new JList<String>(getFridges());
		listFridges.setSelectedIndex(1);
		listFridges.addFocusListener(this);
		scrollList = new JScrollPane(listFridges);
		scrollList.setLocation(0, 25);
		scrollList.setSize(MainFrame.width/3, 100);
		
		// -> mit Auswahlbutton
		buttonSelect.setLocation(0, 125);
		buttonSelect.setSize(MainFrame.width/3, 30);
		buttonSelect.addActionListener(this);
		
		add(labelFridgelist);
		add(scrollList);
		add(buttonSelect);
		
		// Options
		labelSelectedFridge.setLocation(0, 175);
		labelSelectedFridge.setSize(MainFrame.width, 25);
		int xOffset = MainFrame.width/3 + 25, yOffset = 25, width = 350, height = 25;
		rbShowMine.setLocation(xOffset, yOffset * 1);
		rbShowMine.setSize(width, height);
		rbShowAll.setLocation(xOffset, yOffset * 2);
		rbShowAll.setSize(width, height);
		rbShowMine.setActionCommand("mine");
		rbShowAll.setActionCommand("all");
		groupShow.add(rbShowMine);
		groupShow.add(rbShowAll);
		
		add(labelSelectedFridge);
		add(rbShowAll);
		add(rbShowMine);
		
		// Produkte
		int fullwidth = MainFrame.width-5;
		String[][] empty = {{"  ", "  "}, {"  ", "  "}};
		table = new JTable(empty, columnNames);
		table.setColumnSelectionAllowed(false);
		scrollTable = new JScrollPane(table);
		scrollTable.setLocation(0, 200);
		scrollTable.setSize(fullwidth, 100);
		width = 200; height = 25;
		buttonAdd.setLocation(fullwidth/2 - width, 300);
		buttonAdd.setSize(width, height);
		buttonAdd.addActionListener(this);
		buttonConsume.setLocation(fullwidth/2, 300);
		buttonConsume.setSize(width, height);
		buttonConsume.addActionListener(this);
		
		scrollTable.setVisible(false);
		buttonConsume.setVisible(false);
		buttonAdd.setVisible(false);
		
		add(scrollTable);
		add(buttonAdd);
		add(buttonConsume);
		
		labelError.setBounds(fullwidth/2 - 100, 425, 200, 25);
		labelError.setForeground(Color.red);
		add(labelError);
	}
	
	
	private String[] getFridges() {
		fridgeIDs = RESTHandler.getFridgesIDs(Client.profileID);
		return RESTHandler.getFridges(Client.profileID);
	}
	
	
	private String getFridgeName(int fridgeID) {
		producttypeIDs = RESTHandler.getProducttypeIDs(fridgeID);
		return RESTHandler.getFridgeName(fridgeID);
	}
	
	private String[][] getTableData(int fridgeID) {
		return RESTHandler.getFridgeTableData(fridgeID);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
//		selectedProducttypeID = 
		
		// Kühlschrank ausgewählt?
		if(e.getActionCommand() == "Select" && listFridges.getSelectedIndex() != -1) {
			selectedFridgeID = fridgeIDs[listFridges.getSelectedIndex()];
			// Label anpassen
			labelSelectedFridge.setText("Products in \"" + getFridgeName(selectedFridgeID) + "\"");
			// TableData anpassen
			String[][] tmp = getTableData(fridgeIDs[listFridges.getSelectedIndex()]);
			DefaultTableModel tm = new DefaultTableModel(tmp, columnNames);
			table.setModel(tm);
			
			// Produktliste sichtbar machen
			scrollTable.setVisible(true);
			buttonConsume.setVisible(true);
			buttonAdd.setVisible(true);
		}
		
		// Produkt hinzufügen?
		if(e.getActionCommand() == "Add Product") {
			int row = table.getSelectedRow();
			if(row == -1) { // Muss eine Zeile ausgewählt haben
				labelError.setText("Select a Producttype first...");
			} else {
				labelError.setText("");
				selectedProducttypeID = producttypeIDs[row];
				String name = (String) table.getModel().getValueAt(row, 0);
				AddProductFrame addProductFrame = new AddProductFrame(name);
				addProductFrame.setLocation(buttonAdd.getLocation().x + MainFrame.posX, 
						buttonAdd.getLocation().y + MainFrame.posY);
			}
		}
		
		// Produkt verbrauchen?
		if(e.getActionCommand() == "Consume Product") {
			
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		listFridges.setListData(getFridges());
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

}
