package application.swing.popups;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JRootPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import application.swing.WindowClosingAdapter;

public class SubscripeToFrame extends JFrame implements ActionListener {
	private JList<String> jList;
	private JButton buttonSub;
	private JButton buttonBack;
	private List<String> nodeList;
	private List<String> subscriptionList;

	public SubscripeToFrame(List<String> nodeList) {
		super("Choose Subscriptions");
		this.nodeList = nodeList;
		subscriptionList = new ArrayList<String>();
		addWindowListener(new WindowClosingAdapter(false));
		setLayout(null);
		// setUndecorated(true);
		// getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		setSize(220, 230);
		setVisible(true);

		jList = new JList<String>();
		updateNodeList();

		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		buttonSub = new JButton("Subscripe");
		buttonBack = new JButton("Back");
		buttonSub.addActionListener(this);
		buttonBack.addActionListener(this);

		jList.setBounds(0, 0, 200, 150);
		buttonSub.setBounds(0, 160, 100, 30);
		buttonBack.setBounds(100, 160, 100, 30);

		getContentPane().add(jList);
		getContentPane().add(buttonSub);
		getContentPane().add(buttonBack);
	}

	public void updateNodeList() {
		String[] s = new String[this.nodeList.size()];
		for (int i=0; i<s.length; i++) {
			for (int j = 0; j < subscriptionList.size(); j++) {
				if (subscriptionList.get(j) == this.nodeList.get(i))
					s[i] = nodeList.get(i) + " (subscribed)";
			}
			if (s[i] == null)
				s[i] = nodeList.get(i);
		}
		jList.setListData(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == "Subscripe") {
			System.out.println("Sub");
			ListModel<String> lm = jList.getModel();
			int[] sel = jList.getSelectedIndices();
			for (int i=0; i<sel.length; i++) {
				String value = lm.getElementAt(sel[i]);
				System.out.println("  " + value + " subscribed");
				subscriptionList.add(value);
			}
			updateNodeList();
		}
		if (e.getActionCommand() == "Back") {
			System.out.println("Back");
		}
	}
}
