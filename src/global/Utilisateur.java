package global;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Utilisateur implements Serializable, Comparable<Utilisateur>{
	private static final long serialVersionUID = -5647550023167220838L;

	protected String nom;
	protected String prenom;
	protected String identifiant;
	protected String password;

	/**
	 * 
	 * @param nom         : Nom de l'utilisateur
	 * @param prenom      : prenom de l'utilisateur
	 * @param identifiant : identifiant de l'utilisateur
	 * @param password    : mot de passe de l'utilisateur
	 * @param gs          : la liste des groupes de l'utilisateur (vararg)
	 **/
	public Utilisateur(String nom, String prenom, String identifiant, String password) {
		this.nom = nom;
		this.prenom = prenom;
		this.identifiant = identifiant;
		this.password = password;
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

	public String getIdentifiant() {
		return identifiant;
	}

	public String getPassword() {
		return password;
	}
	
	public String classToString() {
		if(this instanceof Agents) {
			return "Agents";
		}
		else
			return "Utilisateur campus";
	}

	@Override
	public String toString() {
		return "[Nom : " + nom + ", Prenom : " + prenom + ", Identifiant : " + identifiant + ", MDP " + password + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		}
		return ((Utilisateur) o).identifiant.equals(identifiant);
	}
	
	@Override
	public int compareTo(Utilisateur u) {
		return getIdentifiant().compareTo(u.getIdentifiant());
		
	}
}
