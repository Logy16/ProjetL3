package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import global.Agents;
import global.Fil;
import global.Groupe;
import global.Utilisateur;
import global.dto.CreationFilDto;
import global.dto.DemandeDeConnexionDto;
import global.dto.GlobalDto;
import global.dto.SendMessageDto;

public class Client {
	private static Socket socket;
	private static InputStream inputStream;
	private static OutputStream outputStream;

	public static void main(String[] args) throws IOException {
		try {
			System.out.println("Creating connection ...");
			socket = new Socket("localhost", 7777);
			// socket.connect(socket.getLocalSocketAddress());
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}

		// TESTS
		System.out.println("Connection info");
		Groupe tpa41 = new Groupe("TPA41");
		Utilisateur nemo = new Agents("BOUILLON", "Nemo", "Pastorale", "testmdpnemo", tpa41);
		Client client = new Client();
		if (client.demandeConnexion("Pastorale", "testmdpnemo")) {
			client.demandeCreationFil("Nouveau fil", nemo, tpa41, "Ceci est un nouveau fil");
			System.out.println("test");
		}
		System.out.println("tttt");
		//

	}

	public boolean demandeConnexion(String login, String password) throws IOException {
		DemandeDeConnexionDto fil = new DemandeDeConnexionDto(login, password);

		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
			// OBJET NON RECU PAR LE SERVEUR
			objectOutputStream.writeObject(fil);

			try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
				// OBJET RECU PAR LE SERVEUR
				GlobalDto globalDto = (GlobalDto) objectInputStream.readObject();
				System.out.println(globalDto.getOperation());
				switch (globalDto.getOperation()) {
				case CONNEXION_REUSSIE:
					return true;
				case CONNEXION_ECHOUEE:
					return false;
				default:
					return false;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	public void demandeCreationFil(String nomFil, Utilisateur createur, Groupe groupe, String premierMessage)
			throws IOException {
		CreationFilDto fil = new CreationFilDto(groupe, nomFil, createur, premierMessage);

		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
			objectOutputStream.writeObject(fil);
		}
	}

	public void sendMessage(String message, Utilisateur envoyeur, Fil fil) throws IOException {
		SendMessageDto sendMessage = new SendMessageDto(message, envoyeur, fil);
		try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
			objectOutputStream.writeObject(sendMessage);
		}
	}
}
