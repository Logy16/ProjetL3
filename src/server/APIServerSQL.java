package server;

import java.util.Set;
import java.util.SortedSet;

import global.Etat;
import global.Fil;
import global.Groupe;
import global.Message;
import global.Utilisateur;

public interface APIServerSQL {

	/**
	 * Retourne l'utilisateur recherch� � partir du nom et du pr�nom ATTENTION : NE
	 * RETOURNE AUCUN GROUPE DANS L'UTILISATEUR
	 * 
	 * @param nom    : Nom de l'utilisateur
	 * @param prenom : Pr�nom de l'utilisateur
	 * @return L'utilisateur recherch�, null si rien trouv�
	 * @author Liouss
	 **/
	public Utilisateur getUtilisateur(String nom, String prenom);

	/**
	 * Retourne l'utilisateur recherch� � partir de son identifiant ATTENTION : NE
	 * RETOURNE AUCUN GROUPE DANS L'UTILISATEUR
	 * 
	 * @param identifiant : Identifiant de l'utilisateur
	 * @return L'utilisateur recherch�, null si rien trouv�
	 * @author Liouss
	 **/
	public Utilisateur getUtilisateur(String identifiant);

	/**
	 * Permet d'ajouter ou d'update un utilisateur � la base de donn�es. Si
	 * l'utilisateur est d�j� dans la base de donn�es, met � jour ses valeurs, et le
	 * cr�e sinon. ATTENTION : MET EGALEMENT A JOURS LA LISTE DES GROUPES DE
	 * L'UTILISATEUR ! (ne pas utiliser juste apr�s getUtilisateur, car
	 * getUtilisateur ne donne pas de groupe)
	 * 
	 * @param utilisateur : L'utilisateur � �crire dans la base de donn�es
	 * @return true si jamais un utilisateur de m�me identifiant �tait d�j� pr�sent,
	 *         false sinon
	 * @author Liouss
	 **/
	public boolean setUtilisateur(Utilisateur utilisateur);

	/**
	 * Permet d'obtenir un fil gr�ce � son titre
	 * 
	 * @param titre : titre du fil qu'on recherche
	 * @return le fil trouv�, null si rien trouv�
	 * @author Liouss
	 **/
	public Fil getFil(String titre);

	/**
	 * Permet d'obtenir un fil gr�ce � son titre
	 * 
	 * @param id : l'id du fil recherch�
	 * @return le fil trouv�, null si rien trouv�
	 * @author Liouss
	 **/
	public Fil getFil(int id);

	/**
	 * Permet d'obtenir un set de Fil � partir d'un groupe
	 * 
	 * @param groupe : groupe qui peut lire les fils
	 * @return let set des fils trouv�s
	 * @author Liouss
	 **/
	public SortedSet<Fil> getFils(Groupe groupe);

	/**
	 * Permet d'ajouter un fil � la base de donn�es. ATTENTION : N'AJOUTE PAS ET NE
	 * MET PAS A JOURS LES MESSAGES LI�S A CE FIL
	 * 
	 * @param fil : Le fil � �crire dans la base de donn�es
	 * @return false si jamais un fil de m�me identifiant �tait d�j� pr�sent, true
	 *         sinon
	 * @author Liouss
	 **/
	public boolean createFil(Fil fil);

	/**
	 * Permet d'obtenir un groupe gr�ce � son titre
	 * 
	 * @param nom : titre du groupe qu'on recherche
	 * @return le groupe trouv�, null si rien trouv�
	 * @author Liouss
	 **/
	public Groupe getGroupe(String nom);

	/**
	 * Permet d'obtenir les groupes d'un utilisateur
	 * 
	 * @param utilisateur : l'utilisateur dont on recherche les groupes
	 * @return le groupe trouv�, null si rien trouv�
	 * @author Liouss
	 **/
	public Set<Groupe> getGroupesFromUser(Utilisateur utilisateur);

	/**
	 * Permet d'ajouter un groupe � la base de donn�es. ATTENTION : NE MET PAS A
	 * JOURS LES UTILISATEURS APPARTENANT AU GROUPE, NI LA LISTE DES UTILISATEURS DU
	 * GROUPE ELLE-M�ME ATTENTION : NE SERT QU'A CREER UN GROUPE
	 * 
	 * @param groupe : Le groupe � �crire dans la base de donn�es
	 * @return false si jamais un fil de m�me identifiant �tait d�j� pr�sent, true
	 *         sinon
	 * @author Liouss
	 **/
	public boolean createGroupe(Groupe groupe);

	/**
	 * Permet d'ajouter un message dans la base de donn�es
	 * 
	 * @param message : Le message � �crire dans la base de donn�es
	 * @author Liouss
	 **/
	public void sendMessage(Message message);

	/**
	 * Indique au serveur que le message a �t� envoy� � l'utilisateur
	 * 
	 * @param message     : Le message qui a �t� envoy�
	 * @param utilisateur : l'utilisateur � qui le message a �t� envoy�
	 * @author Liouss
	 **/
	public void hasSentMessage(Message message, Utilisateur utilisateur);

	/**
	 * Indique au serveur que le message a �t� lu par l'utilisateur
	 * 
	 * @param message     : Le message qui a �t� lu
	 * @param utilisateur : l'utilisateur qui a lu le message
	 * @author Liouss
	 **/
	public void hasReadMessage(Message message, Utilisateur utilisateur);

	/**
	 * Permet d'obtenir l'�tat d'un message
	 * 
	 * @param message : Le message dont on veut obtenir l'�tat
	 * @return L'�tat
	 **/
	public Etat getMessageState(Message message);
}
