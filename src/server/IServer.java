package server;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import global.Etat;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;

public interface IServer {

	/**
	 * Teste la demande de connexion d'un utilisateur au serveur
	 * 
	 * @param login    : login de l'utilisateur a tester
	 * @param password : mot de passe de l'utilisateur a tester
	 * @return true si le mot de passe correspond bien � celui de l'utilisateur,
	 *         false sinon
	 * @author Nemo
	 **/
	public boolean demandeConnexion(String login, String password)
			throws NoSuchAlgorithmException, UnsupportedEncodingException;

	/**
	 * Permet de cr�er un nouveau fil et de l'ajouter � la base de donn�e
	 * 
	 * @param titre    : titre � attribuer au fil
	 * @param groupe   : groupe auquel s'adresse le fil
	 * @param createur : utilisateur a l'origine du fil
	 * @param message  : premier message envoy� par le cr�ateur sur le fil
	 * @author Nemo
	 **/
	public void creerFil(String titre, Groupe groupe, Utilisateur createur, String message);

	/**
	 * Permet de cr�er un nouveau groupe et de l'ajouter � la base de donn�e
	 * 
	 * @param nom : nom � attribuer au groupe
	 * @author Nemo
	 **/
	public void creerGroupe(String nom);

	/**
	 * Permet de tester si un utilisateur appartient a un groupe
	 * 
	 * @param user   : utilisateur a tester
	 * @param groupe : groupe a tester
	 * @return true si l'utilisateur est dans le groupe, false sinon
	 * @author Nemo
	 **/
	public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe);

	/**
	 * Permet d'ajouter un utilisateur dans un groupe s'il n'y est pas d�j� et de
	 * mettre � jour la base de donn�e
	 * 
	 * @param user   : utilisateur a ajouter dans le groupe
	 * @param groupe : groupe dans lequel l'utilisateur sera rajout�
	 * @author Nemo
	 **/
	public void addUserToGroupe(Utilisateur user, Groupe groupe);

	/**
	 * Permet de cr�er un nouvel agent et de l'ajouter � la base de donn�e
	 * 
	 * @param nom               : nom � attribuer � l'agent
	 * @param prenom            : prenom � attribuer � l'agent
	 * @param id                : id de l'agent
	 * @param notHashedPassword : password brut de l'agent
	 * @param groupes           : groupe(s) auquel(s) appartient l'agent
	 * @author Nemo
	 **/
	public void addAgent(String nom, String prenom, String id, String notHashedPassword, Groupe... groupes);

	/**
	 * Permet de cr�er un nouvel utilisateur du campus et de l'ajouter � la base de
	 * donn�e
	 * 
	 * @param nom               : nom � attribuer � l'utilisateur
	 * @param prenom            : prenom � attribuer � l'utilisateur
	 * @param id                : id de l'utilisateur
	 * @param notHashedPassword : password brut de l'utilisateur
	 * @param groupes           : groupe(s) auquel(s) appartient l'utilisateur
	 * @author Nemo
	 **/
	public void addUtilisateurCampus(String nom, String prenom, String id, String notHashedPassword, Groupe... groupes);

	/**
	 * Permet d'envoyer un message dans un fil de discussion
	 * 
	 * @param message  : message � envoyer sur le fil
	 * @param envoyeur : utilisateur � la source du message
	 * @param fil      : fil de discussion sur lequel doit �tre envoy� le message
	 * @author Nemo
	 **/
	public void envoyerMessage(String message, Utilisateur envoyeur, Fil fil);

	/**
	 * Permet de lire tout les messages non lus dans un fil pour un utilisateur, a
	 * utiliser lorsque un utilisateur ouvre un fil
	 * 
	 * @param fil     : fil dans lequel les messages sont lus
	 * @param lecteur : utilisateur ouvrant le fil
	 * @author Nemo
	 **/
	public void lireMessageFil(Fil fil, Utilisateur lecteur);

	/**
	 * Permet d'obtenir l'Etat du message pass� en param�tre
	 * 
	 * @param message : message dont on souhaite obtenir l'etat
	 * @return l'Etat du message pass� en param�tre
	 * @author Nemo
	 **/
	public Etat getMessageStatus(Message message);

	/**
	 * Permet de modifier le nom d'un utilisateur et de le mettre � jour dans la
	 * base de donn�e
	 * 
	 * @param user   : utilisateur dont il faut modifier le nom
	 * @param newNom : nouveau nom � attribuer � l'utilisateur
	 * @author Nemo
	 **/
	public void modifierNomUser(Utilisateur user, String newNom);

	/**
	 * Permet de modifier le pr�nom d'un utilisateur et de le mettre � jour dans la
	 * base de donn�e
	 * 
	 * @param user      : utilisateur dont il faut modifier le pr�nom
	 * @param newPrenom : nouveau pr�nom � attribuer � l'utilisateur
	 * @author Nemo
	 **/
	public void modifierPrenomUser(Utilisateur user, String newPrenom);

	/**
	 * Permet de supprimer un utilisateur de la base de donn�e et met � jour les
	 * liens avec cet utilisateur
	 * 
	 * @param user : utilisateur � supprimer
	 * @author Nemo
	 **/
	public void supprimerUtilisateur(Utilisateur user);

	/**
	 * Permet de supprimer un groupe de la base de donn�e et met � jour les liens
	 * avec ce groupe
	 * 
	 * @param groupe : groupe � supprimer
	 * @author Nemo
	 **/
	public void supprimerGroupe(Groupe groupe);
}
