package client;

import java.net.Socket;

import server.Client;

public class MainUtilisateur {

	public static void main(String[] args) {

		Client c = new Client(new Socket());
		InterfaceUtilisateur myFrame = new InterfaceUtilisateur(c);
		myFrame.setVisible(true);

	}

}
