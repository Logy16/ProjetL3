package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import global.dto.CreationFilDto;
import global.dto.DemandeDeConnexionDto;
import global.dto.GlobalDto;
import global.dto.GlobalDto.TypeOperation;
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
					InputStream inputStream = socket.getInputStream();
					System.out.println(inputStream);
					OutputStream outputStream = socket.getOutputStream();
					System.out.println(outputStream);
					System.out.println("Launching thread");
					ServerThread sThread = new ServerThread(socket, api, inputStream, outputStream);
					System.out.println("Client connected!");
				}
			} catch (NullPointerException | IOException e) {
				e.printStackTrace();
				System.out.println("Client failed connection ...");
			}
		}
	}

//****************************//
// METHODES INTERFACE SERVEUR //
//****************************//

	public void creerGroupe(String nom) {
		Groupe groupe = new Groupe(nom);
		api.createGroupe(groupe);
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

	public void addAgent(String nom, String prenom, String id, String notHashedPassword, Groupe... groupes) {
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
	}

	public void addUtilisateurCampus(String nom, String prenom, String id, String notHashedPassword,
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

//**********************************************//
// CLASSE INTERNE: 1 THREAD PAR CLIENT CONNECTE //
//**********************************************//

	private static class ServerThread extends Thread {
		private Socket socket;
		private APIServerSQL api;
		private InputStream inputStream;
		private OutputStream outputStream;

		public ServerThread(Socket s, APIServerSQL api, InputStream inputStream, OutputStream outputStream) {
			socket = s;
			this.api = api;
			this.inputStream = inputStream;
			this.outputStream = outputStream;

			System.out.println("Created client thread ...");
			this.start();
		}

		@Override
		public void run() {
			while (!socket.isClosed()) {

				try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
					GlobalDto globalDto = (GlobalDto) objectInputStream.readObject();
					switch (globalDto.getOperation()) {
					case CREATION_FIL:
						CreationFilDto dtoCF = (CreationFilDto) globalDto;
						demandeCreationFil(dtoCF);
						break;
					case DEMANDE_CONNEXION:
						DemandeDeConnexionDto dtoDDC = (DemandeDeConnexionDto) globalDto;
						try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
							if (demandeConnexion(dtoDDC)) {
								SimpleDto connexionReussie = new SimpleDto(TypeOperation.CONNEXION_REUSSIE);
								objectOutputStream.writeObject(connexionReussie);
							} else {
								SimpleDto connexionEchouee = new SimpleDto(TypeOperation.CONNEXION_ECHOUEE);
								objectOutputStream.writeObject(connexionEchouee);
							}
						} catch (NoSuchAlgorithmException | IOException e) {
							e.printStackTrace();
						}
						break;
					case SEND_MESSAGE:
						SendMessageDto dtoSM = (SendMessageDto) globalDto;
						sendMessage(dtoSM);
						break;
					default:
						break;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		}

		public void demandeCreationFil(CreationFilDto dto) {
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
		}

		public boolean demandeConnexion(DemandeDeConnexionDto dto) throws NoSuchAlgorithmException {
			String hashedPassword = new String(
					MessageDigest.getInstance("md5").digest(dto.getPassword().getBytes(StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8);
			Utilisateur utilisateurTest = api.getUtilisateur(dto.getLogin());
			System.out.println(utilisateurTest.getPassword() + "\n" + dto.getPassword());
			return utilisateurTest.getPassword().equals(hashedPassword)
					&& utilisateurTest.getIdentifiant().equals(dto.getLogin());
		}

		public void sendMessage(SendMessageDto dto) {
			Message newMessage = new Message(dto.getMessage(), new Date(), dto.getEnvoyeur(), dto.getFil());
			dto.getFil().addMessage(newMessage);
			api.sendMessage(newMessage);
			for (Utilisateur utilisateurFil : dto.getFil().getGroupe().getUtilisateurs()) {
				api.hasSentMessage(newMessage, utilisateurFil);
			}
			api.hasReadMessage(newMessage, dto.getEnvoyeur());
		}
	}
}
