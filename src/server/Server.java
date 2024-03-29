package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

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

public class Server {

	private static final int PORT_NUM = 7777;
	private static APIServerSQL api;
	private static ServerSocket serverSocket;

	public Server() {
		try {
			Server.serverSocket = new ServerSocket(PORT_NUM);
			Connection connection = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/projets5?serverTimezone=UTC", "root", "");
			Server.api = new SimpleAPIServerSQL(connection);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// accepte les connexions de chaque client et cree un nouveau thread pour chacun
		new Server();
		while (!serverSocket.isClosed()) {
			try {
				System.out.println("Waiting socket creation ...");
				System.out.println("Waiting client connection ...");
				Socket socket = serverSocket.accept();
				System.out.println("Client attempting connection ...");
				new ServerThread(socket, api);
				System.out.println("Client connected!");

			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
				System.out.println("Client failed connection ...");
			}
		}
	}

	public void closeEverything(ServerSocket serverSocket, Socket... socket) {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
			for (Socket s : socket) {
				if (s != null) {
					s.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
					case MODIFY_PASSWORD:
						ModifyUserDto dtoMP = (ModifyUserDto) globalDto;
						Utilisateur modifiedUserPass = modifierPasswordUser(dtoMP);
						objectOutputStream.writeObject(modifiedUserPass);
						break;
					case DELETE_GROUPE:
						DeleteGroupDto dtoDG = (DeleteGroupDto) globalDto;
						supprimerGroupe(dtoDG);
						break;
					case RESET_USER_GROUPS:
						ResetGroupFromUserDto dtor = (ResetGroupFromUserDto) globalDto;
						resetGroupes(dtor.getUser());
						break;
					case DELETE_USER:
						DeleteUserDto dtoDU = (DeleteUserDto) globalDto;
						supprimerUtilisateur(dtoDU);
						break;
					case GET_FIL_GROUPE:
						GetFilGroupeDto dtoGFG = (GetFilGroupeDto) globalDto;
						SortedSet<Fil> groupeFilReturn = getFil(dtoGFG);
						if (groupeFilReturn != null) {
							objectOutputStream.writeObject(groupeFilReturn);
						} else {
							throw new IllegalArgumentException(
									"No fil binded to this value: " + dtoGFG.getGroupe().getNom());
						}
						break;
					case GET_FIL_INT:
						GetFilIntegerDto dtoGFI = (GetFilIntegerDto) globalDto;
						Fil intFilReturn = getFil(dtoGFI);
						if (intFilReturn != null) {
							objectOutputStream.writeObject(intFilReturn);
						} else {
							throw new IllegalArgumentException("No fil binded to this value: " + dtoGFI.getInteger());
						}
						break;
					case GET_FIL_STRING:
						GetFilStringDto dtoGFS = (GetFilStringDto) globalDto;
						Fil stringFilReturn = getFil(dtoGFS);
						if (stringFilReturn != null) {
							objectOutputStream.writeObject(stringFilReturn);
						} else {
							throw new IllegalArgumentException("No fil binded to this value: " + dtoGFS.getString());
						}
						break;
					case GET_GROUPE_STRING:
						StringDto dtoGGS = (StringDto) globalDto;
						Groupe stringGroupeReturn = getGroupe(dtoGGS);
						if (stringGroupeReturn != null) {
							objectOutputStream.writeObject(stringGroupeReturn);
						} else {
							throw new IllegalArgumentException("No groupe binded to this value: " + dtoGGS.getString());
						}
						break;
					case GET_GROUPE_UTILISATEUR:
						UtilisateurDto dtoGGU = (UtilisateurDto) globalDto;
						Set<Groupe> utilisateurGroupeReturn = getGroupe(dtoGGU);
						if (utilisateurGroupeReturn != null) {
							objectOutputStream.writeObject(utilisateurGroupeReturn);
						} else {
							throw new IllegalArgumentException(
									"No groupe binded to this value: " + dtoGGU.getUtilisateur());
						}
						break;
					case GET_UTILISATEURS:
						Set<Utilisateur> utilisateursReturn = getUtilisateurs();
						if (utilisateursReturn != null) {
							objectOutputStream.writeObject(utilisateursReturn);
						} else {
							throw new IllegalArgumentException("No users found in the database");
						}
						break;
					case GET_UTILISATEUR_STRING:
						StringDto dtoGUS = (StringDto) globalDto;
						Utilisateur utilisateurReturn = getUtilisateur(dtoGUS);
						if (utilisateurReturn != null) {
							objectOutputStream.writeObject(utilisateurReturn);
						} else {
							throw new IllegalArgumentException("No user binded to this value: " + dtoGUS.getString());
						}
						break;
					case GET_GROUPE:
						Set<Groupe> groupeReturn = getGroupe();
						if (groupeReturn != null) {
							objectOutputStream.writeObject(groupeReturn);
						} else {
							throw new IllegalArgumentException("No groups found in the database");
						}
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

		@Override
		public Utilisateur getUtilisateur(StringDto dto) {
			return api.getUtilisateur(dto.getString());
		}

		@Override
		public Set<Utilisateur> getUtilisateurs() {
			return api.getUtilisateurs();
		}

		@Override
		public Set<Groupe> getGroupe() {
			return api.getGroupes();
		}

		@Override
		public Set<Groupe> getGroupe(UtilisateurDto dto) {
			return api.getGroupes(dto.getUtilisateur());
		}

		@Override
		public Groupe getGroupe(StringDto dto) {
			return api.getGroupe(dto.getString());
		}

		@Override
		public Fil getFil(GetFilStringDto dto) {
			Fil f = api.getFil(dto.getString());
			for(Message m : f.getMessages()) {
				api.hasSentMessage(m, dto.getUtilisateur());
			}
			return f;
		}

		@Override
		public SortedSet<Fil> getFil(GetFilGroupeDto dto) {
			SortedSet<Fil> fils = api.getFils(dto.getGroupe());
			for(Fil f : fils) {
				for(Message m : f.getMessages()) {
					api.hasSentMessage(m, dto.getUtilisateur());
				}
			}
			return fils;
		}

		@Override
		public Fil getFil(GetFilIntegerDto dto) {
			Fil f = api.getFil(dto.getInteger());
			for(Message m : f.getMessages()) {
				api.hasSentMessage(m, dto.getUtilisateur());
			}
			return f;
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
				if (s != null) {
					s.close();
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
			api.hasReadMessage(newMessage, dto.getEnvoyeur());
			return newMessage;
		}

		@Override
		public Groupe creerGroupe(CreerGroupeDto dto) {
			Groupe groupe = new Groupe(dto.getNom());
			api.createGroupe(groupe);
			return api.getGroupe(dto.getNom());
		}

		@Override
		public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe) {
			for (Utilisateur u : groupe.getUtilisateurs()) {
				if (u.getIdentifiant().equals(user.getIdentifiant())) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void addUserToGroupe(AddUserToGroupeDto dto) {
			Set<Groupe> groupes = api.getGroupes(dto.getUser());
			groupes.add(dto.getGroupe());
			api.setGroupes(dto.getUser(), new ArrayList<>(groupes));
		}

		@Override
		public Utilisateur addAgent(AddAgentDto dto) {
			boolean ajoute = false;
			Utilisateur newUserAgent = new Agents(dto.getNom(), dto.getPrenom(), dto.getId(), dto.getPassword());
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
					dto.getPassword());
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
			Utilisateur u = api.getUtilisateur(dto.getUser().getIdentifiant());
			return u;
		}

		@Override
		public Utilisateur modifierPrenomUser(ModifyUserDto dto) {
			dto.getUser().setPrenom(dto.getNewName());
			api.setUtilisateur(dto.getUser());
			Utilisateur u = api.getUtilisateur(dto.getUser().getIdentifiant());
			return u;
		}
		@Override
		public Utilisateur modifierPasswordUser(ModifyUserDto dto) {
			dto.getUser().setPassword(dto.getNewName());
			api.setUtilisateur(dto.getUser());
			Utilisateur u = api.getUtilisateur(dto.getUser().getIdentifiant());
			return u;
		}
		
		@Override
		public void supprimerUtilisateur(DeleteUserDto dto) {
			api.removeUtilisateur(dto.getUser());
		}

		@Override
		public void supprimerGroupe(DeleteGroupDto dto) {
			api.removeGroupe(dto.getGroupe());
			dto.getGroupe().removeUtilisateurs(dto.getGroupe().getUtilisateurs());
		}

		public void resetGroupes(Utilisateur u) {
			api.setGroupes(u, new ArrayList<Groupe>());

		}

	}
}