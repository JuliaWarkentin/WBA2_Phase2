package application.swing.popups;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import application.Client;
import application.RESTHandler;
import application.swing.WindowClosingAdapter;

import jaxbClasses.ProductType;
import jaxbClasses.Profile;
import jaxbClasses.Profiles;

/**
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class ProductInformationFrame extends JFrame {
	private JLabel labelName = new JLabel();
	private JLabel labelWeight = new JLabel();
	
	private JLabel labelDescription = new JLabel();
	
	private JLabel labelIngredients = new JLabel();
	private JList<String> listIngredients;
	private JScrollPane scrollIngredients;
	
	private JLabel labelNutrients = new JLabel();
	private JList<String> listNutrients;
	private JScrollPane scrollNutrients;
	
	private JLabel labelCurrentPrice = new JLabel();
	private JLabel labelBarcode = new JLabel();

	public ProductInformationFrame(ProductType pt) {
		super(pt.getName());
		setLayout(null);
		addWindowListener(new WindowClosingAdapter(false));
		
		int x = 0, y = 0, xOffset = 25, yOffset = 25, width = 300, height = 25;
		labelName.setText		("Name: 		"+pt.getName());
		labelName.setBounds(x, y + yOffset*0, width, height);
		labelWeight.setText		("Weight: 		"+pt.getWeight().getValue() +" "+pt.getWeight().getUnit().toString());
		labelWeight.setBounds(x, y + yOffset*1, width, height);
		add(labelName);
		add(labelWeight);
		
		labelDescription.setText("Description: 	"+pt.getDescription());
		labelDescription.setBounds(x, y + yOffset*2, width, height);
		add(labelDescription);
		
		String[] tmp = new String[pt.getIngredients().getIngredient().size()];
		for(int i=0; i<pt.getIngredients().getIngredient().size(); i++) {
			tmp[i] = pt.getIngredients().getIngredient().get(i);
		}
		listIngredients = new JList<String>(tmp);
		listIngredients.setSelectedIndex(0);
		scrollIngredients = new JScrollPane(listIngredients);
		scrollIngredients.setBounds(x, y + yOffset*3, width, height*4);
		add(scrollIngredients);
		
		labelNutrients.setText("Nutrients: (100g)");
		labelNutrients.setBounds(x, y + yOffset*7, width, height);
		add(labelNutrients);
		
		tmp = new String[9];
		tmp[0] = "Caloric Value: 	"+pt.getNutrients().getCaloricValue().getValue()+" "+pt.getNutrients().getCaloricValue().getUnit().toString();
		tmp[1] = "Protein: 			"+pt.getNutrients().getProtein()+"g";
		tmp[2] = "Carbohydrates: 	"+pt.getNutrients().getCarbohydrates()+"g";
		tmp[4] = "of wich Sugar: 	"+pt.getNutrients().getOfwichsugar()+"g";
		tmp[5] = "Fat: 				"+pt.getNutrients().getFat()+"g";
		tmp[6] = "of wich sugar: 	"+pt.getNutrients().getOfwichsugar()+"g";
		tmp[7] = "Roughage: 		"+pt.getNutrients().getRoughage()+"g";
		tmp[8] = "Sodium: 			"+pt.getNutrients().getSodium()+"g";
		
		listNutrients = new JList<String>(tmp);
		listNutrients.setSelectedIndex(0);
		scrollNutrients = new JScrollPane(listNutrients);
		scrollNutrients.setBounds(x, y + yOffset*8, width, height*4);
		add(scrollNutrients);
		
//		labelCurrentPrice.setText("Current Price: "+pt.getCurrentPrice().getValue()+" EUR"); // TODO: währung aus producttyp..
//		labelCurrentPrice.setBounds(x, y + yOffset*15, width, height);
//		add(labelCurrentPrice);
		
		labelBarcode.setText	("Barcode: 		"+pt.getDescription());
		labelBarcode.setBounds(x, y + yOffset*12, width, height);
		add(labelBarcode);
		
	}
}
