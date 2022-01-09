package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.SortedSet;

import client.InterfaceConnexion;
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
import global.dto.GetFilGroupeDto;
import global.dto.GetFilIntegerDto;
import global.dto.GetFilStringDto;
import global.dto.GetMessageStateDto;
import global.dto.GlobalDto;
import global.dto.GlobalDto.TypeOperation;
import global.dto.LireFilDto;
import global.dto.ModifyUserDto;
import global.dto.ResetGroupFromUserDto;
import global.dto.SendMessageDto;
import global.dto.SimpleDto;
import global.dto.StringDto;
import global.dto.UtilisateurDto;

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
			InterfaceConnexion interfaceConn = new InterfaceConnexion(client);
			interfaceConn.setVisible(true);
			// client.tests();
			System.out.println("Connection info");
		} catch (IOException e) {
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

	public Utilisateur getUtilisateur(String identifiant) throws IOException, ClassNotFoundException {
		StringDto getUtilisateur = new StringDto(TypeOperation.GET_UTILISATEUR_STRING, identifiant);
		objectOutputStream.writeObject(getUtilisateur);
		return (Utilisateur) objectInputStream.readObject();
	}

	@SuppressWarnings("unchecked")
	public Set<Utilisateur> getUtilisateurs() throws IOException, ClassNotFoundException {
		SimpleDto getUtilisateurs = new SimpleDto(TypeOperation.GET_UTILISATEURS);
		objectOutputStream.writeObject(getUtilisateurs);
		return (Set<Utilisateur>) objectInputStream.readObject();
	}

	@SuppressWarnings("unchecked")
	public Set<Groupe> getGroupe() throws IOException, ClassNotFoundException {
		SimpleDto getGroupe = new SimpleDto(TypeOperation.GET_GROUPE);
		objectOutputStream.writeObject(getGroupe);
		return (Set<Groupe>) objectInputStream.readObject();
	}

	@SuppressWarnings("unchecked")
	public Set<Groupe> getGroupes(Utilisateur utilisateur) throws IOException, ClassNotFoundException {
		UtilisateurDto getGroupes = new UtilisateurDto(TypeOperation.GET_GROUPE_UTILISATEUR, utilisateur);
		objectOutputStream.writeObject(getGroupes);
		return (Set<Groupe>) objectInputStream.readObject();
	}

	public Groupe getGroupe(String nom) throws IOException, ClassNotFoundException {
		StringDto getGroupe = new StringDto(TypeOperation.GET_GROUPE_STRING, nom);
		objectOutputStream.writeObject(getGroupe);
		return (Groupe) objectInputStream.readObject();
	}

	public Fil getFil(Utilisateur utilisateur, int id) throws IOException, ClassNotFoundException {
		GetFilIntegerDto getFil = new GetFilIntegerDto(TypeOperation.GET_FIL_INT, id, utilisateur);
		objectOutputStream.writeObject(getFil);
		return (Fil) objectInputStream.readObject();
	}

	@SuppressWarnings("unchecked")
	public SortedSet<Fil> getFils(Utilisateur utilisateur, Groupe groupe) throws IOException, ClassNotFoundException {
		GetFilGroupeDto getFils = new GetFilGroupeDto(TypeOperation.GET_FIL_GROUPE, utilisateur, groupe);
		objectOutputStream.writeObject(getFils);
		return (SortedSet<Fil>) objectInputStream.readObject();
	}

	public Fil getFil(Utilisateur utilisateur, String titre) throws IOException, ClassNotFoundException {
		GetFilStringDto getFil = new GetFilStringDto(TypeOperation.GET_FIL_STRING, utilisateur, titre);
		objectOutputStream.writeObject(getFil);
		return (Fil) objectInputStream.readObject();
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

	public Utilisateur modifierPasswordUser(Utilisateur user, String newPass) throws IOException, ClassNotFoundException {
		ModifyUserDto modifyPassword = new ModifyUserDto(newPass, user, TypeOperation.MODIFY_PASSWORD);
		objectOutputStream.writeObject(modifyPassword);
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

	public void resetGroupeUser(Utilisateur u) throws IOException {
		ResetGroupFromUserDto resetDto = new ResetGroupFromUserDto(u);
		objectOutputStream.writeObject(resetDto);
	}
}