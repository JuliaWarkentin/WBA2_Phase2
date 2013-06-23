package application;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.SimplePayload;

import application.swing.MainFrame;
import application.swing.TabbedPanel;

/**
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public class ExpirationsNodeListener implements ItemEventListener<Item>{
	
	@Override
	public void handlePublishedItems(ItemPublishEvent<Item> items) {
		System.out.println("\nhandlePublishedItems");
		System.out.println("NodeID: " + items.getNodeId());
		System.out.println("Item count: " + items.getItems().size() + "\n\n");
		for(Item item : items.getItems()){
			System.out.println("\nItem ID: " + item.getId());
			System.out.println(((PayloadItem<SimplePayload>) item).getPayload().toXML());
			// Mache Nutzer auf die neue Nachricht aufmerksam
			TabbedPanel.tp.setTitleAt(1, "Notifications (+"+items.getItems().size()+")");
			JOptionPane.showMessageDialog(MainFrame.frame, "You just received a new Notification!\n" + ((PayloadItem<SimplePayload>) item).getPayload().toXML());
		}
	}
}
