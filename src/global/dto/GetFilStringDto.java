package global.dto;

import java.io.Serializable;

import global.Utilisateur;

public class GetFilStringDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 3638889727922904421L;
	private Utilisateur utilisateur;
	private String string;

	public GetFilStringDto(TypeOperation type, Utilisateur utilisateur, String string) {
		super(type);
		this.utilisateur = utilisateur;
		this.string = string;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

}
