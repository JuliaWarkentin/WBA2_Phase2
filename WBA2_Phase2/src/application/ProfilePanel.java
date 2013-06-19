package application;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import jaxbClasses.Profile;
import jaxbClasses.Profiles;

public class ProfilePanel extends JPanel implements ActionListener {
	private JList<String> list;
	private JButton buttonSelect = new JButton("Select");

	private JLabel labelName = new JLabel();
	private JLabel labelBirth = new JLabel();
	private JLabel labelGender = new JLabel();
	private JLabel labelHeight = new JLabel();
	private JLabel labelWeight = new JLabel();

	public ProfilePanel() {
		setLayout(null);
		setSize(MainFrame.width, MainFrame.height);
		setBackground(Color.pink);
		
		// Profilliste
		list = new JList<String>(getProfiles());
		list.setLocation(0, 0);
		list.setSize(MainFrame.width/3, 200);
		list.setSelectedIndex(1);
		// Auswahlbutton
		buttonSelect.setLocation(0, 200);
		buttonSelect.setSize(MainFrame.width/3, 30);
		buttonSelect.addActionListener(this);
		
		// Labels
		int xOffset = 20, yOffset = 30, width = 200, height = 30;
		labelName.setLocation(MainFrame.width/3 + xOffset, yOffset*1);
		labelName.setSize(width, height);
		labelBirth.setLocation(MainFrame.width/3 + xOffset, yOffset*2);
		labelBirth.setSize(width, height);
		labelGender.setLocation(MainFrame.width/3 + xOffset, yOffset*3);
		labelGender.setSize(width, height);
		labelHeight.setLocation(MainFrame.width/3 + xOffset, yOffset*4);
		labelHeight.setSize(width, height);
		labelWeight.setLocation(MainFrame.width/3 + xOffset, yOffset*5);
		labelWeight.setSize(width, height);
		updateLabels();
		
		add(list);
		add(buttonSelect);
		add(labelName);
		add(labelBirth);
		add(labelGender);
		add(labelHeight);
		add(labelWeight);
	}
	
	int[] profileIDs;
	private String[] getProfiles() {
		// GET - .../profiles
		String url = "http://" + Client.host + ":4434/profiles";
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		Profiles ps = Client.wrs.accept("application/xml").get(Profiles.class);

		String[] names = new String[ps.getProfile().size()];
		profileIDs = new int[ps.getProfile().size()];
		for (int i = 0; i < ps.getProfile().size(); i++) {
			names[i] = ps.getProfile().get(i).getName();
			// ID aus Referenz entnehmen
			String href = ps.getProfile().get(i).getHref();
			profileIDs[i] = Integer.parseInt(href.substring(href.length()-1)); 
			System.out.println(profileIDs[i]);
		}
		System.out.println(Arrays.toString(names));
		return names;
	}
	
	private void updateLabels() {
		int column = list.getSelectedIndex();
		// GET - .../profiles/{id}
		String url = "http://" + Client.host + ":4434/profiles/" + profileIDs[column];
		Client.wrs = com.sun.jersey.api.client.Client.create().resource(url);
		Profile p = Client.wrs.accept("application/xml").get(Profile.class);
		
		labelName.setText  	("Name:         " + p.getName());
		labelBirth.setText 	("Birthdate:    " + p.getBirthdate().toString());
		labelGender.setText	("Gender:       " + p.getGender());
		labelHeight.setText	("Height:       " + p.getHeight());
		labelWeight.setText	("Weight:       " + p.getWeight());
		
		TabbedPanel.loginLabel.setText("\"Logged in\" as " + p.getName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == ("Select")) {
			updateLabels();
		}
	}
}
