package global.dto;

import java.io.Serializable;

import global.Groupe;
import global.Utilisateur;

public class GetFilGroupeDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = -9209815115937148918L;
	private Utilisateur utilisateur;
	private Groupe groupe;

	public GetFilGroupeDto(TypeOperation type, Utilisateur utilisateur, Groupe groupe) {
		super(type);
		this.utilisateur = utilisateur;
		this.groupe = groupe;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

}
