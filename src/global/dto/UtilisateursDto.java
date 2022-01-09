package global.dto;

import java.io.Serializable;
import java.util.Set;

import global.Utilisateur;

public class UtilisateursDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 3812378132368429940L;
	private Set<Utilisateur> utilisateurs;

	public UtilisateursDto(Set<Utilisateur> utilisateur) {
		super(TypeOperation.GET_UTILISATEURS);
		this.utilisateurs = utilisateur;
	}

	public Set<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(Set<Utilisateur> utilisateur) {
		this.utilisateurs = utilisateur;
	}

}
