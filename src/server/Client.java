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
import global.dto.AddAgentDto;
import global.dto.AddUserDto;
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
			objectInputStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			closeEverything(socket, objectInputStream, objectOutputStream);
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("Creating connection ...");
		try {
			Socket socket = new Socket("localhost", 7777);
			Client client = new Client(socket);
			client.tests();
			System.out.println("Connection info");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tests() {
		try {
			Groupe newGroupe = createGroupe("TPA41");
			Utilisateur newUser2 = addUtilisateurCampus("DI SCALA", "Jules", "Liouss", "testmdpjules", newGroupe);
			Utilisateur newUser = addUtilisateurCampus("BOUILLON", "Nemo", "Pastorale", "testmdpnemo", newGroupe);
			if (demandeConnexion("Pastorale", "testmdpnemo")) {
				Fil newFil = demandeCreationFil("Nouveau fil", newUser, newGroupe, "Ceci est un nouveau fil");
				Message newMessage = sendMessage("MessageTest", newUser, newFil);
				System.out.println(getMessageStatus(newMessage));
				lireMessagesFil(newFil, newUser2);
				System.out.println(getMessageStatus(newMessage));
			}
			Groupe newGroupe2 = createGroupe("Projet");
			addUserToGroupe(newUser, newGroupe2);
			Utilisateur modified1 = modifierNomUser(newUser2, "DI SCALA2");
			System.out.println("modification de DI SCALA en " + modified1.getNom());
			Utilisateur modified2 = modifierPrenomUser(modified1, "cursedJules");
			System.out.println("modification de Jules en " + modified2.getPrenom());
//			client.supprimerGroupe(newGroupe2);
//			client.supprimerUtilisateur(newUser2);
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
			if (s != null) {
				s.close();
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
		AddAgentDto addUser = new AddAgentDto(nom, prenom, id, password, groupes);
		objectOutputStream.writeObject(addUser);
		return (Utilisateur) objectInputStream.readObject();
	}

	public Utilisateur addUtilisateurCampus(String nom, String prenom, String id, String password, Groupe... groupes)
			throws IOException, ClassNotFoundException {
		AddUserDto addUser = new AddUserDto(nom, prenom, id, password, groupes);
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

	public Utilisateur modifierNomUser(Utilisateur user, String newName) throws IOException, ClassNotFoundException {
		ModifyUserDto modifyName = new ModifyUserDto(newName, user, TypeOperation.MODIFY_LASTNAME);
		objectOutputStream.writeObject(modifyName);
		return (Utilisateur) objectInputStream.readObject();
	}

	public Utilisateur modifierPrenomUser(Utilisateur user, String newName) throws IOException, ClassNotFoundException {
		ModifyUserDto modifyName = new ModifyUserDto(newName, user, TypeOperation.MODIFY_FIRSTNAME);
		objectOutputStream.writeObject(modifyName);
		return (Utilisateur) objectInputStream.readObject();
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