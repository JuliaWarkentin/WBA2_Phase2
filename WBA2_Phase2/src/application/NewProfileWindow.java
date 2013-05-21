package application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;

public class NewProfileWindow extends JFrame implements ActionListener{
	/// UI-Elements
	// Beschriftungen
	JLabel labelName = new JLabel("Name");
	JLabel labelBirthdate = new JLabel("Birthdate");
	JLabel labelGender = new JLabel("Gender");
	JLabel labelHeight = new JLabel("Height");
	JLabel labelWeight = new JLabel("Weight");
	
	// Name
	JTextField tfName = new JTextField();
	// Geburtstag
	JSpinner spinnerDate = new JSpinner(new SpinnerDateModel());
	// Geschlecht
	ButtonGroup groupGender = new ButtonGroup();
	JRadioButton radio1 = new JRadioButton("m");
	JRadioButton radio2 = new JRadioButton("f");
	// Größe
	JSpinner spinnerHeight = new JSpinner(new SpinnerNumberModel(160, 60, 300, 1));
	// Gewicht
	JSpinner spinnerWeight = new JSpinner(new SpinnerNumberModel(80, 10, 300, 1));
	
	// Bestätigungs und Abbrechen Button
	JButton buttonCreate = new JButton("Create Profile!");
	JButton buttonCancel = new JButton("Cancel");
	
	public NewProfileWindow(int fridgeID, int profileID) {
		super("Create new Profile:");
		addWindowListener(new WindowClosingAdapter(true));
		
		int x = 10;
		int y = 45; int yOffset = 20;
		int width = 80;
		int height = 20;
		labelName.setBounds(x, 0*y+yOffset, width, height);
		labelBirthdate.setBounds(x, 1*y+yOffset, width, height);
		labelGender.setBounds(x, 2*y+yOffset, width, height);
		labelHeight.setBounds(x, 3*y+yOffset, width, height);
		labelWeight.setBounds(x, 4*y+yOffset, width, height);
		
		x = 150;
		tfName.setBounds(x, 0*y+yOffset, width, height);
		spinnerDate.setBounds(x, 1*y+yOffset, width, height);	
		spinnerDate.setEditor(new JSpinner.DateEditor(spinnerDate, "dd.MM.yyyy"));
		radio1.setBounds(x, 2*y+yOffset, 50, height);
		radio2.setBounds(x+50, 2*y+yOffset, 50, height);
		groupGender.add(radio1);
		groupGender.add(radio2);
		spinnerHeight.setBounds(x, 3*y+yOffset, width, height);
		spinnerWeight.setBounds(x, 4*y+yOffset, width, height);
		
		x = 10;
		buttonCreate.setBounds(x,6*y,120,height);
		x = 150;
		buttonCancel.setBounds(x,6*y,width,height);
		
		this.getContentPane().setLayout(null);
		this.getContentPane().add(labelName);
		this.getContentPane().add(labelBirthdate);
		this.getContentPane().add(labelGender);
		this.getContentPane().add(labelHeight);
		this.getContentPane().add(labelWeight);
		
		this.getContentPane().add(tfName);
		this.getContentPane().add(spinnerDate);
		this.getContentPane().add(radio1);
		this.getContentPane().add(radio2);
		this.getContentPane().add(spinnerHeight);
		this.getContentPane().add(spinnerWeight);
		buttonCreate.addActionListener(this);
		buttonCancel.addActionListener(this);
		this.getContentPane().add(buttonCreate);
		this.getContentPane().add(buttonCancel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if(e.getActionCommand() == "Create Profile!") {
			
		}
	}
}
