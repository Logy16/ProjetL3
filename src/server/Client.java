package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import global.Fil;
import global.Groupe;
import global.Utilisateur;
import global.dto.AddUserDto;
import global.dto.AddUserDto.TypeUser;
import global.dto.CreationFilDto;
import global.dto.CreerGroupeDto;
import global.dto.DemandeDeConnexionDto;
import global.dto.GlobalDto;
import global.dto.GlobalDto.TypeOperation;
import global.dto.SendMessageDto;
import global.dto.SimpleDto;

public class Client {
	private Socket socket;
	private ObjectInputStream objectInputStream;
	private ObjectOutputStream objectOutputStream;

	public Client(Socket s) {
		try {
			this.socket = s;
			this.objectOutputStream = new ObjectOutputStream(s.getOutputStream());
			SimpleDto etablissementConnexion = new SimpleDto(TypeOperation.ETABLISSEMENT_CONNEXION);
			objectOutputStream.writeObject(etablissementConnexion);
			this.objectInputStream = new ObjectInputStream(s.getInputStream());
			SimpleDto returnEtablissementConnexion = (SimpleDto) objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			closeEverything(socket, objectInputStream, objectOutputStream);
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Creating connection ...");
		Socket socket = new Socket("localhost", 7777);
		Client client = new Client(socket);
		System.out.println("Connection info");

		// TESTS

		try {
			Groupe newGroupe = client.createGroupe("TPA41");
			// Groupe tpa41 = new Groupe("TPA41");
			Utilisateur newUser = client.addUtilisateurCampus("BOUILLON", "Nemo", "Pastorale", "testmdpnemo",
					newGroupe);
			// UtilisateurCampus nemo = new Agents("BOUILLON", "Nemo", "Pastorale",
			// "testmdpnemo", tpa41);
			if (client.demandeConnexion("Pastorale", "testmdpnemo")) {
				client.demandeCreationFil("Nouveau fil", newUser, newGroupe, "Ceci est un nouveau fil");
			}
			while (true)
				;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void closeEverything(Socket s, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
		try {
			if (objectInputStream != null) {
				objectInputStream.close();
			}
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Groupe createGroupe(String nom) throws IOException, ClassNotFoundException {
		CreerGroupeDto creerGroupe = new CreerGroupeDto(nom);
		objectOutputStream.writeObject(creerGroupe);
		return (Groupe) objectInputStream.readObject();
	}

	public Utilisateur addAgent(String nom, String prenom, String id, String password, Groupe... groupes)
			throws IOException, ClassNotFoundException {
		AddUserDto addUser = new AddUserDto(nom, prenom, id, password, TypeUser.AGENT, groupes);
		objectOutputStream.writeObject(addUser);
		return (Utilisateur) objectInputStream.readObject();
	}

	public Utilisateur addUtilisateurCampus(String nom, String prenom, String id, String password, Groupe... groupes)
			throws IOException, ClassNotFoundException {
		AddUserDto addUser = new AddUserDto(nom, prenom, id, password, TypeUser.UTILISATEUR_CAMPUS, groupes);
		objectOutputStream.writeObject(addUser);
		return (Utilisateur) objectInputStream.readObject();
	}

	public boolean demandeConnexion(String login, String password) throws IOException, ClassNotFoundException {
		DemandeDeConnexionDto fil = new DemandeDeConnexionDto(login, password);
		// OBJET NON RECU PAR LE SERVEUR
		objectOutputStream.writeObject(fil);
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
	}

	public void demandeCreationFil(String nomFil, Utilisateur createur, Groupe groupe, String premierMessage)
			throws IOException {
		CreationFilDto fil = new CreationFilDto(groupe, nomFil, createur, premierMessage);
		objectOutputStream.writeObject(fil);
	}

	public void sendMessage(String message, Utilisateur envoyeur, Fil fil) throws IOException {
		SendMessageDto sendMessage = new SendMessageDto(message, envoyeur, fil);
		objectOutputStream.writeObject(sendMessage);
	}
}