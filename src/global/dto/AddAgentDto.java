package global.dto;

import java.io.Serializable;

import global.Groupe;

public class AddAgentDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 2577967842183812018L;
	private String nom;
	private String prenom;
	private String id;
	private String password;
	private Groupe[] groupes;

	public AddAgentDto(String nom, String prenom, String identifiant, String password, Groupe... gs) {
		super(TypeOperation.ADD_AGENT);
		this.nom = nom;
		this.prenom = prenom;
		this.id = identifiant;
		this.password = password;
		this.groupes = gs;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Groupe[] getGroupes() {
		return groupes;
	}

	public void setGroupes(Groupe[] groupes) {
		this.groupes = groupes;
	}

}
