package global.dto;

import java.io.Serializable;

import global.Groupe;

public class DeleteGroupDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = -5446784572291397197L;
	private Groupe groupe;

	public DeleteGroupDto(Groupe groupe) {
		super(TypeOperation.DELETE_GROUPE);
		this.groupe = groupe;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

}
