package webservice;

/**
 * @author Simon Klinge
 * @author Julia Warkentin
 *
 */
public final class Helper {
	private Helper() {}
	
	/**
	 * @param href im Format: .../xxx/{id}
	 * @return id aus dem übergebenem Hyperlink
	 */
	public static int getID(String href) {
		return Integer.parseInt(href.substring(href.lastIndexOf("/")+1));
	}
}
