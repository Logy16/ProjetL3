package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;

import global.Agents;
import global.Etat;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;
import global.UtilisateurCampus;
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

public class Server {

	private static final int PORT_NUM = 7777;
	private static APIServerSQL api;

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Socket socket = null;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT_NUM);
			Connection connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/projets5?serverTimezone=UTC", "root", "");
			api = new SimpleAPIServerSQL(connection);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}

		// accepte les connexions de chaque client et cree un nouveau thread pour chacun
		while (true) {
			try {
				System.out.println("Waiting socket creation ...");
				if (serverSocket != null) {
					System.out.println("Waiting client connection ...");
					socket = serverSocket.accept();
					System.out.println("Client attempting connection ...");
					new ServerThread(socket, api);
					System.out.println("Client connected!");
				}
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
				System.out.println("Client failed connection ...");
			}
		}
	}

//**********************************************//
// CLASSE INTERNE: 1 THREAD PAR CLIENT CONNECTE //
//**********************************************//

	private static class ServerThread extends Thread implements IServer {
		private Socket socket;
		private APIServerSQL api;
		private ObjectInputStream objectInputStream;
		private ObjectOutputStream objectOutputStream;

		public ServerThread(Socket s, APIServerSQL api) {
			try {
				socket = s;
				this.api = api;
				this.objectInputStream = new ObjectInputStream(s.getInputStream());
				SimpleDto etablissementConnexion = (SimpleDto) objectInputStream.readObject();
				this.objectOutputStream = new ObjectOutputStream(s.getOutputStream());
				objectOutputStream.writeObject(etablissementConnexion);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				closeEverything(socket, objectInputStream, objectOutputStream);
			}
			System.out.println("Created client thread ...");
			this.start();
		}

		@Override
		public void run() {
			while (socket.isConnected()) {
				try {
					GlobalDto globalDto = (GlobalDto) objectInputStream.readObject();
					switch (globalDto.getOperation()) {
					case CREATION_FIL:
						CreationFilDto dtoCF = (CreationFilDto) globalDto;
						Fil newFil = demandeCreationFil(dtoCF);
						objectOutputStream.writeObject(newFil);
						break;
					case DEMANDE_CONNEXION:
						DemandeDeConnexionDto dtoDDC = (DemandeDeConnexionDto) globalDto;
						if (demandeConnexion(dtoDDC)) {
							SimpleDto connexionReussie = new SimpleDto(TypeOperation.CONNEXION_REUSSIE);
							objectOutputStream.writeObject(connexionReussie);
						} else {
							SimpleDto connexionEchouee = new SimpleDto(TypeOperation.CONNEXION_ECHOUEE);
							objectOutputStream.writeObject(connexionEchouee);
						}
						break;
					case SEND_MESSAGE:
						SendMessageDto dtoSM = (SendMessageDto) globalDto;
						Message newMessage = sendMessage(dtoSM);
						objectOutputStream.writeObject(newMessage);
						break;
					case CREER_GROUPE:
						CreerGroupeDto dtoCG = (CreerGroupeDto) globalDto;
						Groupe newGroupe = creerGroupe(dtoCG);
						objectOutputStream.writeObject(newGroupe);
						break;
					case ADD_USER:
						AddUserDto dtoAU = (AddUserDto) globalDto;
						Utilisateur newUserCampus = addUtilisateurCampus(dtoAU);
						objectOutputStream.writeObject(newUserCampus);
						break;
					case ADD_AGENT:
						AddAgentDto dtoAA = (AddAgentDto) globalDto;
						Utilisateur newAgent = addAgent(dtoAA);
						objectOutputStream.writeObject(newAgent);
						break;
					case ADD_USER_TO_GROUPE:
						AddUserToGroupeDto dtoAUTG = (AddUserToGroupeDto) globalDto;
						addUserToGroupe(dtoAUTG);
						break;
					case LIRE_FIL:
						LireFilDto dtoLF = (LireFilDto) globalDto;
						lireMessageFil(dtoLF);
						break;
					case GET_MESSAGE_STATE:
						GetMessageStateDto dtoGMS = (GetMessageStateDto) globalDto;
						Etat etatMessage = getMessageStatus(dtoGMS);
						objectOutputStream.writeObject(etatMessage);
						break;
					case MODIFY_FIRSTNAME:
						ModifyUserDto dtoMUF = (ModifyUserDto) globalDto;
						Utilisateur modifiedUserName = modifierPrenomUser(dtoMUF);
						objectOutputStream.writeObject(modifiedUserName);
						break;
					case MODIFY_LASTNAME:
						ModifyUserDto dtoMUL = (ModifyUserDto) globalDto;
						Utilisateur modifiedUserLastName = modifierNomUser(dtoMUL);
						objectOutputStream.writeObject(modifiedUserLastName);
						break;
					case DELETE_GROUPE:
						DeleteGroupDto dtoDG = (DeleteGroupDto) globalDto;
						supprimerGroupe(dtoDG);
						break;
					case DELETE_USER:
						DeleteUserDto dtoDU = (DeleteUserDto) globalDto;
						supprimerUtilisateur(dtoDU);
						break;
					default:
						break;
					}
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					closeEverything(socket, objectInputStream, objectOutputStream);
					break;
				}
			}
		}

		public void closeEverything(Socket s, ObjectInputStream objectInputStream,
				ObjectOutputStream objectOutputStream) {
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

		@Override
		public Fil demandeCreationFil(CreationFilDto dto) {
			if (dto.getChaine() == null) {
				throw new IllegalArgumentException("Chaine cannot be null");
			}
			Fil fil = new Fil(dto.getChaine(), dto.getGroupe(), dto.getUtilisateur());
			api.createFil(fil);

			// Always add a first message to the fil.
			if ((dto.getMessage() == null) || dto.getMessage().equals("")) {
				throw new IllegalArgumentException("First message cannot be null or empty");
			}
			Message premierMessage = new Message(dto.getMessage(), new Date(), dto.getUtilisateur(), fil);
			fil.addMessage(premierMessage);
			api.sendMessage(premierMessage);
			for (Utilisateur utilisateurFil : fil.getGroupe().getUtilisateurs()) {
				api.hasSentMessage(premierMessage, utilisateurFil);
			}
			api.hasReadMessage(premierMessage, dto.getUtilisateur());
			return fil;
		}

		@Override
		public boolean demandeConnexion(DemandeDeConnexionDto dto) {
			Utilisateur utilisateurTest = api.getUtilisateur(dto.getLogin());
			return utilisateurTest.getPassword().equals(dto.getPassword())
					&& utilisateurTest.getIdentifiant().equals(dto.getLogin());
		}

		@Override
		public Message sendMessage(SendMessageDto dto) {
			Message newMessage = new Message(dto.getMessage(), new Date(), dto.getEnvoyeur(), dto.getFil());
			dto.getFil().addMessage(newMessage);
			api.sendMessage(newMessage);
			for (Utilisateur utilisateurFil : dto.getFil().getGroupe().getUtilisateurs()) {
				api.hasSentMessage(newMessage, utilisateurFil);
			}
			api.hasReadMessage(newMessage, dto.getEnvoyeur());
			return newMessage;
		}

		@Override
		public Groupe creerGroupe(CreerGroupeDto dto) {
			Groupe groupe = new Groupe(dto.getNom());
			api.createGroupe(groupe);
			return groupe;
		}

		@Override
		public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe) {
			return user.getGroupes().contains(groupe);
		}

		@Override
		public void addUserToGroupe(AddUserToGroupeDto dto) {
			if (!testIfUserInGroupe(dto.getUser(), dto.getGroupe())) {
				dto.getGroupe().addUtilisateurs(dto.getUser());
				api.setUtilisateur(dto.getUser());
			}
		}

		@Override
		public Utilisateur addAgent(AddAgentDto dto) {
			boolean ajoute = false;
			Utilisateur newUserAgent = new Agents(dto.getNom(), dto.getPrenom(), dto.getId(), dto.getPassword(),
					dto.getGroupes());
			for (Groupe groupeLink : dto.getGroupes()) {
				if (groupeLink.getUtilisateursSet().contains(newUserAgent)) {
					ajoute = true;
				}
				if (!ajoute) {
					groupeLink.addUtilisateurs(newUserAgent);
				}
				ajoute = false;
			}
			api.setUtilisateur(newUserAgent);
			return newUserAgent;
		}

		@Override
		public Utilisateur addUtilisateurCampus(AddUserDto dto) {
			boolean ajoute = false;
			Utilisateur newUserCampus = new UtilisateurCampus(dto.getNom(), dto.getPrenom(), dto.getId(),
					dto.getPassword(), dto.getGroupes());
			for (Groupe groupeLink : dto.getGroupes()) {
				if (groupeLink.getUtilisateursSet().contains(newUserCampus)) {
					ajoute = true;
				}
				if (!ajoute) {
					groupeLink.addUtilisateurs(newUserCampus);
				}
				ajoute = false;
			}
			api.setUtilisateur(newUserCampus);
			return newUserCampus;
		}

		@Override
		public void lireMessageFil(LireFilDto lireFil) {
			if (testIfUserInGroupe(lireFil.getUser(), lireFil.getFil().getGroupe())
					|| lireFil.getFil().getCreateur().equals(lireFil.getUser())) {
				for (Message messageFil : lireFil.getFil().getMessages()) {
					api.hasReadMessage(messageFil, lireFil.getUser());
				}
			}
		}

		@Override
		public Etat getMessageStatus(GetMessageStateDto dto) {
			return api.getMessageState(dto.getMessage());
		}

		@Override
		public Utilisateur modifierNomUser(ModifyUserDto dto) {
			dto.getUser().setNom(dto.getNewName());
			api.setUtilisateur(dto.getUser());
			return api.getUtilisateur(dto.getUser().getIdentifiant());
		}

		@Override
		public Utilisateur modifierPrenomUser(ModifyUserDto dto) {
			dto.getUser().setPrenom(dto.getNewName());
			api.setUtilisateur(dto.getUser());
			return api.getUtilisateur(dto.getUser().getIdentifiant());
		}

		@Override
		public void supprimerUtilisateur(DeleteUserDto dto) {
			api.removeUtilisateur(dto.getUser());
			Iterator<Groupe> listIterator = dto.getUser().getGroupes().iterator();
			for (; listIterator.hasNext();) {
				Groupe groupeDel = listIterator.next();
				groupeDel.removeUtilisateurs(dto.getUser());
			}
		}

		@Override
		public void supprimerGroupe(DeleteGroupDto dto) {
			api.removeGroupe(dto.getGroupe());
			dto.getGroupe().removeUtilisateurs(dto.getGroupe().getUtilisateurs());
		}

	}
}