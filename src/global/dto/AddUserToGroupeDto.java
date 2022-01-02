package global.dto;

import java.io.Serializable;

import global.Groupe;
import global.Utilisateur;

public class AddUserToGroupeDto extends GlobalDto implements Serializable {
	private static final long serialVersionUID = 8053894547185016101L;
	private Utilisateur user;
	private Groupe groupe;

	public AddUserToGroupeDto(Utilisateur user, Groupe groupe) {
		super(TypeOperation.ADD_USER_TO_GROUPE);
		this.user = user;
		this.groupe = groupe;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}

}
