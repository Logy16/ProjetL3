package client;
import java.io.IOException;
import java.net.Socket;

import server.Client;
import server.Server;

public class MainServer {

	public static void main(String[] args) {
		Server.main(null);
		InterfaceServeur interfaceServeur;
		try {
			interfaceServeur = new InterfaceServeur(new Client(new Socket("localhost", 7777)));
			interfaceServeur.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
