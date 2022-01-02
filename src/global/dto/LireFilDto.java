package global.dto;

import java.io.Serializable;

import global.Fil;
import global.Utilisateur;

public class LireFilDto extends GlobalDto implements Serializable {
	private static final long serialVersionUID = -7635187631898259430L;
	private Utilisateur user;
	private Fil fil;

	public LireFilDto(Utilisateur user, Fil fil) {
		super(TypeOperation.LIRE_FIL);
		this.user = user;
		this.fil = fil;
	}

	public Utilisateur getUser() {
		return user;
	}

	public void setUser(Utilisateur user) {
		this.user = user;
	}

	public Fil getFil() {
		return fil;
	}

	public void setFil(Fil fil) {
		this.fil = fil;
	}
}
