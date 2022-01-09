package global.dto;

import java.io.Serializable;

import global.Groupe;
import global.Utilisateur;

public class ResetGroupFromUserDto extends GlobalDto implements Serializable {
	private static final long serialVersionUID = 8053894547185016101L;
	private Utilisateur user;

	public ResetGroupFromUserDto(Utilisateur user) {
		super(TypeOperation.RESET_USER_GROUPS);
		this.user = user;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

}
