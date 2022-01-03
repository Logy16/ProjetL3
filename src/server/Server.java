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
import global.dto.AddUserDto;
import global.dto.AddUserDto.TypeUser;
import global.dto.AddUserToGroupeDto;
import global.dto.CreationFilDto;
import global.dto.CreerGroupeDto;
import global.dto.DemandeDeConnexionDto;
import global.dto.GetMessageStateDto;
import global.dto.GlobalDto;
import global.dto.GlobalDto.TypeOperation;
import global.dto.LireFilDto;
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

		// accepte les connexions de chaque client et crée un nouveau thread pour
		// chacun
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

	private static class ServerThread extends Thread {
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
						Utilisateur newUser;
						AddUserDto dtoAU = (AddUserDto) globalDto;
						if (dtoAU.getType() == TypeUser.AGENT) {
							newUser = addAgent(dtoAU.getNom(), dtoAU.getPrenom(), dtoAU.getId(), dtoAU.getPassword(),
									dtoAU.getGroupes());
						} else {
							newUser = addUtilisateurCampus(dtoAU.getNom(), dtoAU.getPrenom(), dtoAU.getId(),
									dtoAU.getPassword(), dtoAU.getGroupes());
						}
						objectOutputStream.writeObject(newUser);
						break;
					case ADD_USER_TO_GROUPE:
						AddUserToGroupeDto dtoAUTG = (AddUserToGroupeDto) globalDto;
						addUserToGroupe(dtoAUTG.getUser(), dtoAUTG.getGroupe());
						break;
					case LIRE_FIL:
						LireFilDto dtoLF = (LireFilDto) globalDto;
						lireMessageFil(dtoLF.getFil(), dtoLF.getUser());
						break;
					case GET_MESSAGE_STATE:
						GetMessageStateDto dtoGMS = (GetMessageStateDto) globalDto;
						Etat etatMessage = getMessageStatus(dtoGMS.getMessage());
						objectOutputStream.writeObject(etatMessage);
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

		public boolean demandeConnexion(DemandeDeConnexionDto dto) {
			Utilisateur utilisateurTest = api.getUtilisateur(dto.getLogin());
			return utilisateurTest.getPassword().equals(dto.getPassword())
					&& utilisateurTest.getIdentifiant().equals(dto.getLogin());
		}

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

		public Groupe creerGroupe(CreerGroupeDto dto) {
			Groupe groupe = new Groupe(dto.getNom());
			api.createGroupe(groupe);
			return groupe;
		}

		public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe) {
			boolean userInGroupe = false;
			for (Utilisateur utilisateurInGroupe : groupe.getUtilisateurs()) {
				if (utilisateurInGroupe.equals(user)) {
					userInGroupe = true;
				}
			}
			return userInGroupe;
		}

		public void addUserToGroupe(Utilisateur user, Groupe groupe) {
			if (!testIfUserInGroupe(user, groupe)) {
				groupe.addUtilisateurs(user);
				api.setUtilisateur(user);
			}
		}

		public Utilisateur addAgent(String nom, String prenom, String id, String notHashedPassword, Groupe... groupes) {
			boolean ajoute = false;
			Utilisateur newUser = new Agents(nom, prenom, id, notHashedPassword, groupes);
			for (Groupe groupeLink : groupes) {
				for (Utilisateur utilisateurLink : groupeLink.getUtilisateurs()) {
					if (utilisateurLink.equals(newUser)) {
						ajoute = true;
					}
				}
				if (!ajoute) {
					groupeLink.addUtilisateurs(newUser);
				}
				ajoute = false;
			}
			api.setUtilisateur(newUser);
			return newUser;
		}

		public Utilisateur addUtilisateurCampus(String nom, String prenom, String id, String notHashedPassword,
				Groupe... groupes) {
			boolean ajoute = false;
			Utilisateur newUser = new UtilisateurCampus(nom, prenom, id, notHashedPassword, groupes);
			for (Groupe groupeLink : groupes) {
				for (Utilisateur utilisateurLink : groupeLink.getUtilisateurs()) {
					if (utilisateurLink.equals(newUser)) {
						ajoute = true;
					}
				}
				if (!ajoute) {
					groupeLink.addUtilisateurs(newUser);
				}
				ajoute = false;
			}
			api.setUtilisateur(newUser);
			return newUser;
		}

		public void lireMessageFil(Fil fil, Utilisateur lecteur) {
			if (testIfUserInGroupe(lecteur, fil.getGroupe()) || fil.getCreateur().equals(lecteur)) {
				for (Message messageFil : fil.getMessages()) {
					if (getMessageStatus(messageFil) != Etat.LU) {
						api.hasReadMessage(messageFil, lecteur);
					}
				}
			}
		}

		public Etat getMessageStatus(Message message) {
			return api.getMessageState(message);
		}

		public void modifierNomUser(Utilisateur user, String newNom) {
			user.setNom(newNom);
			api.setUtilisateur(user);
		}

		public void modifierPrenomUser(Utilisateur user, String newPrenom) {
			user.setPrenom(newPrenom);
			api.setUtilisateur(user);
		}

		public void supprimerUtilisateur(Utilisateur user) {
			api.removeUtilisateur(user);
			Iterator<Groupe> listIterator = user.getGroupes().iterator();
			for (; listIterator.hasNext();) {
				Groupe groupeDel = listIterator.next();
				groupeDel.removeUtilisateurs(user);
			}
		}

		public void supprimerGroupe(Groupe groupe) {
			api.removeGroupe(groupe);
			groupe.removeUtilisateurs(groupe.getUtilisateurs());
		}

	}
}