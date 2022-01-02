package global.dto;

import java.io.Serializable;

public class CreerGroupeDto extends GlobalDto implements Serializable {
	private static final long serialVersionUID = 6793135879728437932L;
	private String nom;

	public CreerGroupeDto(String nom) {
		super(TypeOperation.CREER_GROUPE);
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

}
