package global.dto;

import java.io.Serializable;

import global.Groupe;

public class GroupeDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = -1126534794925951608L;
	private Groupe groupe;

	public GroupeDto(TypeOperation type, Groupe groupe) {
		super(type);
		this.groupe = groupe;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

}
