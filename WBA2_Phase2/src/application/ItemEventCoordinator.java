package application;

import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.SimplePayload;

public class ItemEventCoordinator implements ItemEventListener<Item>{

	@Override
	public void handlePublishedItems(ItemPublishEvent<Item> items) {
		System.out.println("\nhandlePublishedItems");
		System.out.println("NodeID: " + items.getNodeId());
		System.out.println("Item count: " + items.getItems().size() + "\n\n");
		for(Item item : items.getItems()){
			System.out.println("\nItem ID: " + item.getId());
			System.out.println(((PayloadItem<SimplePayload>) item).getPayload().toXML());
		}
	}
}
