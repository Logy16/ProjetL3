package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import server.Client;

public class Main {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		//Client c = new Client(new Socket("localhost", 7777));
		InterfaceConnexion conn = new InterfaceConnexion(null);
		conn.setVisible(true);

	}

}
