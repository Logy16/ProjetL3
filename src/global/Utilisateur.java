package global;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Utilisateur {
	protected String nom;
	protected String prenom;
	protected SortedSet<Groupe> groupes = new TreeSet<>();
	protected String identifiant;
	protected String password;
	
	/**
	 * 
	 * @param nom : Nom de l'utilisateur
	 * @param prenom : prenom de l'utilisateur
	 * @param identifiant : identifiant de l'utilisateur
	 * @param password : mot de passe de l'utilisateur
	 * @param gs : la liste des groupes de l'utilisateur (vararg)
	**/
	public Utilisateur(String nom, String prenom, String identifiant, String password, Groupe... gs) {
		this.nom = nom;
		this.prenom = prenom;
		this.identifiant = identifiant;
		this.password = password;
		for(Groupe g : gs) {
			g.addUtilisateurs(this);
		}
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/**
	 * Toujours préférer utiliser groupe.ajouterUtilisateurs ou
	 * groupe.removeUtilisateurs pour modifier
	 **/
	public Set<Groupe> getGroupes() {
		return groupes;
	}

	public String getIdentifiant() {
		return identifiant;
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "[Nom : "+nom+", Prenom : "+prenom+", Identifiant : "+identifiant+", MDP "+password+"]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		}
		return ((Utilisateur) o).identifiant.equals(identifiant);
	}
}
