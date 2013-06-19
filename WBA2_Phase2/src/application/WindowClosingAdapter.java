package application;

import java.awt.event.*;

public class WindowClosingAdapter extends WindowAdapter {
	boolean kill;

	public WindowClosingAdapter(boolean kill) {
		this.kill = kill;
	}

	public void windowClosing(WindowEvent event) {
		// Fenster schlieﬂen
		event.getWindow().setVisible(false);
		event.getWindow().dispose();
		if (kill) {
			// Verbindung abbrechen
			Client.xmpp.disconnect();
			// Client beenden
			System.exit(0);
		}
	}
}