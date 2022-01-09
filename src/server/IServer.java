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
	 * @return true si le mot de passe et le login correspond bien � celui de
	 *         l'utilisateur, false sinon
	 * @author Nemo
	 **/
	public boolean demandeConnexion(DemandeDeConnexionDto dto)
			throws NoSuchAlgorithmException, UnsupportedEncodingException;

	/**
	 * Permet de cr�er un nouveau fil et de l'ajouter � la base de donn�e
	 * 
	 * @param dto : Data transfer object contenant toutes les informations
	 *            n�cessaires � la creation du fil: groupe auquel s'adresse le fil,
	 *            nom du fil, cr�ateur du fil, ainsi que le premier message du fil
	 * @return le nouveau fil cr��
	 * @author Nemo
	 **/
	public Fil demandeCreationFil(CreationFilDto dto);

	/**
	 * Permet de cr�er un nouveau groupe et de l'ajouter � la base de donn�e
	 * 
	 * @param dto : Data transfer object contenant le nom du nouveau groupe
	 * @return le nouveau groupe cr��
	 * @author Nemo
	 **/
	public Groupe creerGroupe(CreerGroupeDto dto);

	/**
	 * Permet de tester si un utilisateur appartient a un groupe
	 * 
	 * @param user   : utilisateur � tester
	 * @param groupe : groupe � tester
	 * @return true si l'utilisateur est dans le groupe, false sinon
	 * @author Nemo
	 **/
	public boolean testIfUserInGroupe(Utilisateur user, Groupe groupe);

	/**
	 * Permet d'ajouter un utilisateur dans un groupe s'il n'y est pas d�j� et de
	 * mettre � jour la base de donn�e
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur et le groupe dans
	 *            lequel l'utilisateur doit �tre ajout�
	 * @author Nemo
	 **/
	public void addUserToGroupe(AddUserToGroupeDto dto);

	/**
	 * Permet de cr�er un nouvel agent et de l'ajouter � la base de donn�e
	 * 
	 * @param dto : Data transfer object contenant toutes les informations
	 *            n�cessaires � la creation du nouvel agent: nom de l'agent, pr�nom
	 *            de l'agent, identifiant de l'agent, mot de passe de l'agent ainsi
	 *            que l'ensemble des groupes auquels il appartient
	 * @return le nouvel utilisateur cr��
	 * @author Nemo
	 **/
	public Utilisateur addAgent(AddAgentDto dto);

	/**
	 * Permet de cr�er un nouvel utilisateur du campus et de l'ajouter � la base de
	 * donn�e
	 * 
	 * @param dto : Data transfer object contenant toutes les informations
	 *            n�cessaires � la creation du nouvel utilisateur du campus: nom de
	 *            l'utilisateur, pr�nom de l'utilisateur, identifiant de
	 *            l'utilisateur, mot de passe de l'utilisateur ainsi que l'ensemble
	 *            des groupes auquels il appartient
	 * @return le nouvel utilisateur cr��
	 * @author Nemo
	 **/
	public Utilisateur addUtilisateurCampus(AddUserDto dto);

	/**
	 * Permet d'envoyer un message dans un fil de discussion
	 * 
	 * @param dto : Data transfer object contant toutes les informations utiles � la
	 *            cr�ation du nouveau message: le message, l'utilisateur � l'origine
	 *            du message ainsi que le fil sur lequel le message doit �tre envoy�
	 * @return le message envoy� sur le fil
	 * @author Nemo
	 **/
	public Message sendMessage(SendMessageDto dto);

	/**
	 * Permet de lire tout les messages non lus dans un fil pour un utilisateur, �
	 * utiliser lorsque un utilisateur ouvre un fil
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur lecteur, ainsi que
	 *            le fil contenant tout les messages � lire
	 * @author Nemo
	 **/
	public void lireMessageFil(LireFilDto lireFil);

	/**
	 * Permet d'obtenir l'�tat du message pass� en param�tre
	 * 
	 * @param dto : Data transfer object contenant le message dont on souhaite
	 *            obtenir l'�tat
	 * @return l'�tat du message pass� en param�tre
	 * @author Nemo
	 **/
	public Etat getMessageStatus(GetMessageStateDto dto);

	/**
	 * Permet de modifier le nom d'un utilisateur et de le mettre � jour dans la
	 * base de donn�e
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur dont on souhaite
	 *            modifier le nom ainsi que le nouveau nom de celui ci
	 * @return l'utilisateur dont on a modifi� le nom
	 * @author Nemo
	 **/
	public Utilisateur modifierNomUser(ModifyUserDto dto);

	/**
	 * Permet de modifier le pr�nom d'un utilisateur et de le mettre � jour dans la
	 * base de donn�e
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur dont on souhaite
	 *            modifier le nom ainsi que le nouveau prenom de celui ci
	 * @return l'utilisateur dont on a modifi� le prenom
	 * @author Nemo
	 **/
	public Utilisateur modifierPrenomUser(ModifyUserDto dto);

	/**
	 * Permet de supprimer un utilisateur de la base de donn�e et met � jour les
	 * liens avec cet utilisateur
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur destin� � �tre
	 *            supprim�
	 * @author Nemo
	 **/
	public void supprimerUtilisateur(DeleteUserDto dto);

	/**
	 * Permet de supprimer un groupe de la base de donn�e et met � jour les liens
	 * avec ce groupe
	 * 
	 * @param dto : Data transfer object contenant le groupe destin� � �tre supprim�
	 * @author Nemo
	 **/
	public void supprimerGroupe(DeleteGroupDto dto);

	/**
	 * Permet d'obtenir un utilisateur � partir de son identifiant
	 * 
	 * @param dto : Data transfer object contenant l'identifiant de l'utilisateur
	 *            que l'on souhaite obtenir
	 * @return l'utilisateur li� � l'identifiant
	 * @author Nemo
	 **/
	public Utilisateur getUtilisateur(StringDto dto);

	/**
	 * Permet d'obtenir l'ensemble des groupes li�s � un utilisateur
	 * 
	 * @param dto : Data transfer object contenant l'utilisateur dont on souhaite
	 *            obtenir les groupes
	 * @return l'ensemble des groupes li�s � l'utilisateur
	 * @author Nemo
	 **/
	public Set<Groupe> getGroupe(UtilisateurDto dto);

	/**
	 * Permet d'obtenir l'ensemble des groupes de la base de donn�e
	 * 
	 * @return l'ensemble des groupes contenus dans la base de donn�e
	 * @author Nemo
	 **/
	public Set<Groupe> getGroupe();

	/**
	 * Permet d'obtenir un groupe � partir de son nom
	 * 
	 * @param dto : Data transfer object contenant le nom du groupe que l'on
	 *            souhaite obtenir
	 * @return le groupe li� au nom
	 * @author Nemo
	 **/
	public Groupe getGroupe(StringDto dto);

	/**
	 * Permet d'obtenir un fil � partir de son nom
	 * 
	 * @param dto : Data transfer object contenant le nom du fil que l'on souhaite
	 *            obtenir
	 * @return le fil li� au nom
	 * @author Nemo
	 **/
	public Fil getFil(StringDto dto);

	/**
	 * Permet d'obtenir l'ensemble des fils li�s � un groupe
	 * 
	 * @param dto : Data transfer object contenant le groupe dont on souhaite
	 *            obtenir les fils
	 * @return l'ensemble des fils li�s au groupe
	 * @author Nemo
	 **/
	public SortedSet<Fil> getFil(GroupeDto dto);

	/**
	 * Permet d'obtenir un fil � partir de son identifiant
	 * 
	 * @param dto : Data transfer object contenant l'identifiant du fil que l'on
	 *            souhaite obtenir
	 * @return le fil li� � l'identifiant
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
