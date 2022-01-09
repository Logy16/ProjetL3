package global.dto;

import java.io.Serializable;

import global.Utilisateur;

public class GetFilIntegerDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = -9209815115937148918L;
	private int integer;
	private Utilisateur utilisateur;

	public GetFilIntegerDto(TypeOperation type, int integer, Utilisateur utilisateur) {
		super(type);
		this.integer = integer;
		this.utilisateur = utilisateur;
	}

	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

}
