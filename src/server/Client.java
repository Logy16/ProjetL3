package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import global.Etat;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;
import global.dto.AddUserDto;
import global.dto.AddUserDto.TypeUser;
import global.dto.AddUserToGroupeDto;
import global.dto.CreationFilDto;
import global.dto.CreerGroupeDto;
import global.dto.DeleteGroupDto;
import global.dto.DeleteUserDto;
import global.dto.DemandeDeConnexionDto;
import global.dto.GetMessageStateDto;
import global.dto.GlobalDto;
import global.dto.GlobalDto.TypeOperation;
import global.dto.LireFilDto;
import global.dto.ModifyUserDto;
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
			Utilisateur newUser2 = client.addUtilisateurCampus("DI SCALA", "Jules", "Liouss", "testmdpjules",
					newGroupe);
			// UtilisateurCampus nemo = new Agents("BOUILLON", "Nemo", "Pastorale",
			// "testmdpnemo", tpa41);
			if (client.demandeConnexion("Pastorale", "testmdpnemo")) {
				Fil newFil = client.demandeCreationFil("Nouveau fil", newUser, newGroupe, "Ceci est un nouveau fil");
				Message newMessage = client.sendMessage("MessageTest", newUser, newFil);
				System.out.println(client.getMessageStatus(newMessage));
				client.lireMessagesFil(newFil, newUser2);
				System.out.println(client.getMessageStatus(newMessage));
			}
			Groupe newGroupe2 = client.createGroupe("Projet");
			client.addUserToGroupe(newUser, newGroupe2);
			client.modifierPrenomUser(newUser, "Nemo2");
			client.modifierNomUser(newUser2, "DI SCALA2");
			System.out.println("modification de Nemo en " + newUser.getPrenom());
			System.out.println("modification de DI SCALA en " + newUser2.getNom());
			client.supprimerGroupe(newGroupe2);
			client.supprimerUtilisateur(newUser2);
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

	public Fil demandeCreationFil(String nomFil, Utilisateur createur, Groupe groupe, String premierMessage)
			throws IOException, ClassNotFoundException {
		CreationFilDto fil = new CreationFilDto(groupe, nomFil, createur, premierMessage);
		objectOutputStream.writeObject(fil);
		return (Fil) objectInputStream.readObject();
	}

	public Message sendMessage(String message, Utilisateur envoyeur, Fil fil)
			throws IOException, ClassNotFoundException {
		SendMessageDto sendMessage = new SendMessageDto(message, envoyeur, fil);
		objectOutputStream.writeObject(sendMessage);
		return (Message) objectInputStream.readObject();
	}

	public void addUserToGroupe(Utilisateur user, Groupe groupe) throws IOException {
		AddUserToGroupeDto addUser = new AddUserToGroupeDto(user, groupe);
		objectOutputStream.writeObject(addUser);
	}

	public void lireMessagesFil(Fil fil, Utilisateur utilisateur) throws IOException {
		LireFilDto lireFil = new LireFilDto(utilisateur, fil);
		objectOutputStream.writeObject(lireFil);
	}

	public void modifierNomUser(Utilisateur user, String newName) throws IOException {
		user.setNom(newName);
		ModifyUserDto modifyName = new ModifyUserDto(newName, user, TypeOperation.MODIFY_LASTNAME);
		objectOutputStream.writeObject(modifyName);
	}

	public void modifierPrenomUser(Utilisateur user, String newName) throws IOException {
		user.setPrenom(newName);
		ModifyUserDto modifyName = new ModifyUserDto(newName, user, TypeOperation.MODIFY_FIRSTNAME);
		objectOutputStream.writeObject(modifyName);
	}

	public Etat getMessageStatus(Message m) throws IOException, ClassNotFoundException {
		GetMessageStateDto messageStatus = new GetMessageStateDto(m);
		objectOutputStream.writeObject(messageStatus);
		return (Etat) objectInputStream.readObject();
	}

	public void supprimerUtilisateur(Utilisateur u) throws IOException {
		DeleteUserDto deleteUser = new DeleteUserDto(u);
		objectOutputStream.writeObject(deleteUser);
	}

	public void supprimerGroupe(Groupe g) throws IOException {
		DeleteGroupDto deleteGroup = new DeleteGroupDto(g);
		objectOutputStream.writeObject(deleteGroup);
	}
}