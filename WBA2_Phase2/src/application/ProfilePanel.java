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

/**
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class ProfilePanel extends JPanel implements ActionListener {
	// Auswahlliste
	private JLabel labelProfiles = new JLabel("Existing Profiles:");
	private JList<String> list;
	private JScrollPane scroll;
	private JButton buttonSelect = new JButton("Select");
	private int[] profileIDs; // benötigt, um zu Wissen welche Listenzeile welcher profileID entspricht

	// Informationen zum ausgewählten Profil
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
		labelProfiles.setLocation(0, 0);
		labelProfiles.setSize(100, 25);
		list = new JList<String>(getProfiles());
		list.setSelectedIndex(0);
		JScrollPane scroll = new JScrollPane(list);
		scroll.setLocation(0, 25);
		scroll.setSize(MainFrame.width/3, 100);
		
		// Auswahlbutton
		buttonSelect.setLocation(0, 125);
		buttonSelect.setSize(MainFrame.width/3, 30);
		buttonSelect.addActionListener(this);
		
		// Labels
		int xOffset = 25, yOffset = 25, width = 200, height = 30;
		labelName.setLocation(MainFrame.width/3 + xOffset, yOffset*0);
		labelName.setSize(width, height);
		labelBirth.setLocation(MainFrame.width/3 + xOffset, yOffset*1);
		labelBirth.setSize(width, height);
		labelGender.setLocation(MainFrame.width/3 + xOffset, yOffset*2);
		labelGender.setSize(width, height);
		labelHeight.setLocation(MainFrame.width/3 + xOffset, yOffset*3);
		labelHeight.setSize(width, height);
		labelWeight.setLocation(MainFrame.width/3 + xOffset, yOffset*4);
		labelWeight.setSize(width, height);
		updateLabels();
		
		add(labelProfiles);
		add(scroll);
		add(buttonSelect);
		add(labelName);
		add(labelBirth);
		add(labelGender);
		add(labelHeight);
		add(labelWeight);
	}
	
	private String[] getProfiles() {
		profileIDs = RESTHandler.getProfileIDs();
		return RESTHandler.getProfiles();
	}
	
	private void updateLabels() {
		Profile p = RESTHandler.getProfile(profileIDs[list.getSelectedIndex()]);
		
		labelName.setText  	("Name:         " + p.getName());
		labelBirth.setText 	("Birthdate:    " + p.getBirthdate().toString());
		labelGender.setText	("Gender:       " + p.getGender());
		labelHeight.setText	("Height:       " + p.getHeight());
		labelWeight.setText	("Weight:       " + p.getWeight());
		
		TabbedPanel.loginLabel.setText("\"Logged in\" as " + p.getName());
		Client.profileID = profileIDs[list.getSelectedIndex()];
		System.out.println("New ProfileID: "+Client.profileID);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == ("Select")) {
			updateLabels();
		}
	}
	
}
