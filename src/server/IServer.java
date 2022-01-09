package server;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.SortedSet;

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
import global.dto.GroupeDto;
import global.dto.IntegerDto;
import global.dto.LireFilDto;
import global.dto.ModifyUserDto;
import global.dto.SendMessageDto;
import global.dto.StringDto;
import global.dto.UtilisateurDto;

public interface IServer {

	/**
	 * Teste la demande de connexion d'un utilisateur au serveur
	 * 
	 * @param dto : Data transfer object contenant le login et le password de
	 *            l'utilisateur
	 * @return true si le mot de passe et le login correspond bien à celui de
	 *         l'utilisateur, false sinon
	 * @author Nemo
	 **/
	public boolean demandeConnexion(DemandeDeConnexionDto dto)
			throws NoSuchAlgorithmException, UnsupportedEncodingException;

	/**
	 * Permet de créer un nouveau fil et de l'ajouter à la base de donnée
	 * 
	 * @param dto : Data transfer object contenant toutes les informations
	 *            nécessaires à la creation du fil: groupe auquel s'adresse le fil,
	 *            nom du fil, créateur du fil, ainsi que le premier message du fil
	 * @return le nouveau fil créé
	 * @author Nemo
	 **/
	public Fil demandeCreationFil(CreationFilDto dto);

	/**
	 * Permet de créer un nouveau groupe et de l'ajouter à la base de donnée
	 * 
	 * @param dto : Data transfer object contenant le nom du nouveau groupe
	 * @return le nouveau groupe créé
	 * @author Nemo
	 **/
	public Groupe creerGroupe(CreerGroupeDto dto);

	/**
	 * Permet de tester si un utilisateur appartient a un groupe
	 * 
	 * @param user   : utilisateur à tester
	 * @param groupe : groupe à tester
	 * @return true si l'utilisateur est dans le groupe, false sinon
	 * @author Nemo
	 **/
	public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe);

	/**
	 * Permet d'ajouter un utilisateur dans un groupe s'il n'y est pas déjà et de
	 * mettre à jour la base de donnée
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur et le groupe dans
	 *            lequel l'utilisateur doit être ajouté
	 * @author Nemo
	 **/
	public void addUserToGroupe(AddUserToGroupeDto dto);

	/**
	 * Permet de créer un nouvel agent et de l'ajouter à la base de donnée
	 * 
	 * @param dto : Data transfer object contenant toutes les informations
	 *            nécessaires à la creation du nouvel agent: nom de l'agent, prénom
	 *            de l'agent, identifiant de l'agent, mot de passe de l'agent ainsi
	 *            que l'ensemble des groupes auquels il appartient
	 * @return le nouvel utilisateur créé
	 * @author Nemo
	 **/
	public Utilisateur addAgent(AddAgentDto dto);

	/**
	 * Permet de créer un nouvel utilisateur du campus et de l'ajouter à la base de
	 * donnée
	 * 
	 * @param dto : Data transfer object contenant toutes les informations
	 *            nécessaires à la creation du nouvel utilisateur du campus: nom de
	 *            l'utilisateur, prénom de l'utilisateur, identifiant de
	 *            l'utilisateur, mot de passe de l'utilisateur ainsi que l'ensemble
	 *            des groupes auquels il appartient
	 * @return le nouvel utilisateur créé
	 * @author Nemo
	 **/
	public Utilisateur addUtilisateurCampus(AddUserDto dto);

	/**
	 * Permet d'envoyer un message dans un fil de discussion
	 * 
	 * @param dto : Data transfer object contant toutes les informations utiles à la
	 *            création du nouveau message: le message, l'utilisateur à l'origine
	 *            du message ainsi que le fil sur lequel le message doit être envoyé
	 * @return le message envoyé sur le fil
	 * @author Nemo
	 **/
	public Message sendMessage(SendMessageDto dto);

	/**
	 * Permet de lire tout les messages non lus dans un fil pour un utilisateur, à
	 * utiliser lorsque un utilisateur ouvre un fil
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur lecteur, ainsi que
	 *            le fil contenant tout les messages à lire
	 * @author Nemo
	 **/
	public void lireMessageFil(LireFilDto lireFil);

	/**
	 * Permet d'obtenir l'état du message passé en paramètre
	 * 
	 * @param dto : Data transfer object contenant le message dont on souhaite
	 *            obtenir l'état
	 * @return l'état du message passé en paramètre
	 * @author Nemo
	 **/
	public Etat getMessageStatus(GetMessageStateDto dto);

	/**
	 * Permet de modifier le nom d'un utilisateur et de le mettre à jour dans la
	 * base de donnée
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur dont on souhaite
	 *            modifier le nom ainsi que le nouveau nom de celui ci
	 * @return l'utilisateur dont on a modifié le nom
	 * @author Nemo
	 **/
	public Utilisateur modifierNomUser(ModifyUserDto dto);

	/**
	 * Permet de modifier le prénom d'un utilisateur et de le mettre à jour dans la
	 * base de donnée
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur dont on souhaite
	 *            modifier le nom ainsi que le nouveau prenom de celui ci
	 * @return l'utilisateur dont on a modifié le prenom
	 * @author Nemo
	 **/
	public Utilisateur modifierPrenomUser(ModifyUserDto dto);

	/**
	 * Permet de supprimer un utilisateur de la base de donnée et met à jour les
	 * liens avec cet utilisateur
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur destiné à être
	 *            supprimé
	 * @author Nemo
	 **/
	public void supprimerUtilisateur(DeleteUserDto dto);

	/**
	 * Permet de supprimer un groupe de la base de donnée et met à jour les liens
	 * avec ce groupe
	 * 
	 * @param dto : Data transfer object contenant le groupe destiné à être supprimé
	 * @author Nemo
	 **/
	public void supprimerGroupe(DeleteGroupDto dto);

	/**
	 * Permet d'obtenir un utilisateur à partir de son identifiant
	 * 
	 * @param dto : Data transfer object contenant l'identifiant de l'utilisateur
	 *            que l'on souhaite obtenir
	 * @return l'utilisateur lié à l'identifiant
	 * @author Nemo
	 **/
	public Utilisateur getUtilisateur(StringDto dto);

	/**
	 * Permet d'obtenir l'ensemble des groupes liés à un utilisateur
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur dont on souhaite
	 *            obtenir les groupes
	 * @return l'ensemble des groupes liés à l'utilisateur
	 * @author Nemo
	 **/
	public Set<Groupe> getGroupe(UtilisateurDto dto);

	/**
	 * Permet d'obtenir l'ensemble des groupes de la base de donnée
	 * 
	 * @return l'ensemble des groupes contenus dans la base de donnée
	 * @author Nemo
	 **/
	public Set<Groupe> getGroupe();

	/**
	 * Permet d'obtenir un groupe à partir de son nom
	 * 
	 * @param dto : Data transfer object contenant le nom du groupe que l'on
	 *            souhaite obtenir
	 * @return le groupe lié au nom
	 * @author Nemo
	 **/
	public Groupe getGroupe(StringDto dto);

	/**
	 * Permet d'obtenir un fil à partir de son nom
	 * 
	 * @param dto : Data transfer object contenant le nom du fil que l'on souhaite
	 *            obtenir
	 * @return le fil lié au nom
	 * @author Nemo
	 **/
	public Fil getFil(StringDto dto);

	/**
	 * Permet d'obtenir l'ensemble des fils liés à un groupe
	 * 
	 * @param dto : Data transfer object contenant le groupe dont on souhaite
	 *            obtenir les fils
	 * @return l'ensemble des fils liés au groupe
	 * @author Nemo
	 **/
	public SortedSet<Fil> getFil(GroupeDto dto);

	/**
	 * Permet d'obtenir un fil à partir de son identifiant
	 * 
	 * @param dto : Data transfer object contenant l'identifiant du fil que l'on
	 *            souhaite obtenir
	 * @return le fil lié à l'identifiant
	 * @author Nemo
	 **/
	public Fil getFil(IntegerDto dto);
	
	/**
	 * Permet d'obtenir tous les utilisateurs
	 * @return set de tous les utilisateurs
	 * @author Liouss
	 **/
	public Set<Utilisateur> getUtilisateurs();
}
