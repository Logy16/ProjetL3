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
	 * Retourne l'utilisateur recherché à partir du nom et du prénom ATTENTION : NE
	 * RETOURNE AUCUN GROUPE DANS L'UTILISATEUR
	 * 
	 * @param nom    : Nom de l'utilisateur
	 * @param prenom : Prénom de l'utilisateur
	 * @return L'utilisateur recherché, null si rien trouvé
	 * @author Liouss
	 **/
	public Utilisateur getUtilisateur(String nom, String prenom);

	/**
	 * Retourne l'utilisateur recherché à partir de son identifiant ATTENTION : NE
	 * RETOURNE AUCUN GROUPE DANS L'UTILISATEUR
	 * 
	 * @param identifiant : Identifiant de l'utilisateur
	 * @return L'utilisateur recherché, null si rien trouvé
	 * @author Liouss
	 **/
	public Utilisateur getUtilisateur(String identifiant);

	/**
	 * Permet d'ajouter ou d'update un utilisateur à la base de données. Si
	 * l'utilisateur est déjà dans la base de données, met à jour ses valeurs, et le
	 * crée sinon. ATTENTION : MET EGALEMENT A JOURS LA LISTE DES GROUPES DE
	 * L'UTILISATEUR ! (ne pas utiliser juste après getUtilisateur, car
	 * getUtilisateur ne donne pas de groupe)
	 * 
	 * @param utilisateur : L'utilisateur à écrire dans la base de données
	 * @return true si jamais un utilisateur de même identifiant était déjà présent,
	 *         false sinon
	 * @author Liouss
	 **/
	public boolean setUtilisateur(Utilisateur utilisateur);

	/**
	 * Permet d'obtenir un fil grâce à son titre
	 * 
	 * @param titre : titre du fil qu'on recherche
	 * @return le fil trouvé, null si rien trouvé
	 * @author Liouss
	 **/
	public Fil getFil(String titre);

	/**
	 * Permet d'obtenir un fil grâce à son titre
	 * 
	 * @param id : l'id du fil recherché
	 * @return le fil trouvé, null si rien trouvé
	 * @author Liouss
	 **/
	public Fil getFil(int id);

	/**
	 * Permet d'obtenir un set de Fil à partir d'un groupe
	 * 
	 * @param groupe : groupe qui peut lire les fils
	 * @return let set des fils trouvés
	 * @author Liouss
	 **/
	public SortedSet<Fil> getFils(Groupe groupe);

	/**
	 * Permet d'ajouter un fil à la base de données. ATTENTION : N'AJOUTE PAS ET NE
	 * MET PAS A JOURS LES MESSAGES LIÉS A CE FIL
	 * 
	 * @param fil : Le fil à écrire dans la base de données
	 * @return false si jamais un fil de même identifiant était déjà présent, true
	 *         sinon
	 * @author Liouss
	 **/
	public boolean createFil(Fil fil);

	/**
	 * Permet d'obtenir un groupe grâce à son titre
	 * 
	 * @param nom : titre du groupe qu'on recherche
	 * @return le groupe trouvé, null si rien trouvé
	 * @author Liouss
	 **/
	public Groupe getGroupe(String nom);

	/**
	 * Permet d'obtenir les groupes d'un utilisateur
	 * 
	 * @param utilisateur : l'utilisateur dont on recherche les groupes
	 * @return le groupe trouvé, null si rien trouvé
	 * @author Liouss
	 **/
	public Set<Groupe> getGroupesFromUser(Utilisateur utilisateur);

	/**
	 * Permet d'ajouter un groupe à la base de données. ATTENTION : NE MET PAS A
	 * JOURS LES UTILISATEURS APPARTENANT AU GROUPE, NI LA LISTE DES UTILISATEURS DU
	 * GROUPE ELLE-MÊME ATTENTION : NE SERT QU'A CREER UN GROUPE
	 * 
	 * @param groupe : Le groupe à écrire dans la base de données
	 * @return false si jamais un fil de même identifiant était déjà présent, true
	 *         sinon
	 * @author Liouss
	 **/
	public boolean createGroupe(Groupe groupe);

	/**
	 * Permet d'ajouter un message dans la base de données
	 * 
	 * @param message : Le message à écrire dans la base de données
	 * @author Liouss
	 **/
	public void sendMessage(Message message);

	/**
	 * Indique au serveur que le message a été envoyé à l'utilisateur
	 * 
	 * @param message     : Le message qui a été envoyé
	 * @param utilisateur : l'utilisateur à qui le message a été envoyé
	 * @author Liouss
	 **/
	public void hasSentMessage(Message message, Utilisateur utilisateur);

	/**
	 * Indique au serveur que le message a été lu par l'utilisateur
	 * 
	 * @param message     : Le message qui a été lu
	 * @param utilisateur : l'utilisateur qui a lu le message
	 * @author Liouss
	 **/
	public void hasReadMessage(Message message, Utilisateur utilisateur);

	/**
	 * Permet d'obtenir l'état d'un message
	 * 
	 * @param message : Le message dont on veut obtenir l'état
	 * @return L'état
	 **/
	public Etat getMessageState(Message message);
}
