package global.dto;

import java.io.Serializable;

import global.Utilisateur;

public class UtilisateurDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 3812378132368429940L;
	private Utilisateur utilisateur;

	public UtilisateurDto(TypeOperation type, Utilisateur utilisateur) {
		super(type);
		this.utilisateur = utilisateur;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}
