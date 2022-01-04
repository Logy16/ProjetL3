package global.dto;

import java.io.Serializable;

import global.Utilisateur;

public class DeleteUserDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 6356220477582339797L;
	private Utilisateur user;

	public DeleteUserDto(Utilisateur user) {
		super(TypeOperation.DELETE_USER);
		this.user = user;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

}
