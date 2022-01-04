package global.dto;

import java.io.Serializable;

import global.Utilisateur;

public class ModifyUserDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 6350865314237895366L;
	private String newName;
	private Utilisateur user;

	public ModifyUserDto(String newName, Utilisateur user, TypeOperation type) {
		super(type);
		this.newName = newName;
		this.user = user;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

}
