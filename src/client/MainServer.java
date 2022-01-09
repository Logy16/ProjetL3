package client;

import java.io.IOException;
import java.net.Socket;

import server.Client;
import server.Server;

public class MainServer {

	public static void main(String[] args) {
		new MainServerThread();
		InterfaceServeur interfaceServeur;
		try {
			interfaceServeur = new InterfaceServeur(new Client(new Socket("localhost", 7777)));
			interfaceServeur.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static class MainServerThread extends Thread {

		public MainServerThread() {
			this.start();
		}

		@Override
		public void run() {
			Server.main(null);
		}
	}

}
