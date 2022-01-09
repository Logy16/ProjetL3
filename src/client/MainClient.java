package client;

import java.io.IOException;

import server.Client;

public class MainClient {

	public static void main(String[] args) {
		
		try {
			Client.main(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
