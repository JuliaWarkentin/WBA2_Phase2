package application.swing;

import java.awt.event.*;

import application.Client;

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