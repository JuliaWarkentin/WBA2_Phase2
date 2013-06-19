package application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class TabbedPanel extends JPanel {
	JTabbedPane tp = new JTabbedPane();
	NotificationPanel n = new NotificationPanel();
	ProfilePanel p = new ProfilePanel();
	
	public static JLabel loginLabel = new JLabel(); 

	public TabbedPanel() {
		
//		for (int i = 0; i < 5; ++i) {
//			JPanel panel = new JPanel();
//			panel.add(new JLabel("Karte " + i));
//			JButton next = new JButton("Weiter");
//			next.addActionListener(new NextTabActionListener());
//			panel.add(next);
//			tp.addTab("Tab" + i, panel);
//		}
		setLayout(null);
		tp.setLocation(0, 0);
		tp.setSize(MainFrame.width, MainFrame.height);
		
		tp.addTab("Fridges", new JPanel());
		tp.addTab("Notifications", n);
		tp.addTab("Profiles", p);
		add(tp, null);
		
		loginLabel.setLocation(250, 0);
		loginLabel.setSize(300, 20);
		add(loginLabel);
	}

	class NextTabActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
//			int tab = tp.getSelectedIndex();
//			tab = (tab >= tp.getTabCount() - 1 ? 0 : tab + 1);
//			tp.setSelectedIndex(tab);
//			((JPanel) tp.getSelectedComponent()).requestDefaultFocus();
		}
	}
}
