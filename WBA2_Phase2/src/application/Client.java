package application;

public class Client {
	public static void main(String args[]){
		NewProfileWindow npw = new NewProfileWindow(1,1);
		npw.setSize(300, 400);
		npw.setVisible(true);
		
		NewProfileWindow npw1 = new NewProfileWindow(1,1);
		npw1.setSize(300, 400);
		npw1.setLocation(100, 100);
		npw1.setVisible(true);
	}
}
