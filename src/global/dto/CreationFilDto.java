package global.dto;

import java.io.Serializable;

import global.Groupe;
import global.Utilisateur;

public class CreationFilDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 614747758751587958L;
	private Groupe groupe;
	private String chaine;
	private Utilisateur utilisateur;
	private String message;

	public CreationFilDto(Groupe groupe, String chaine, Utilisateur utilisateur, String message) {
		super(TypeOperation.CREATION_FIL);
		this.groupe = groupe;
		this.chaine = chaine;
		this.utilisateur = utilisateur;
		this.message = message;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

	public String getChaine() {
		return chaine;
	}

	public void setChaine(String chaine) {
		this.chaine = chaine;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
