package global;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Utilisateur implements Serializable {
	private static final long serialVersionUID = -5647550023167220838L;

	protected String nom;
	protected String prenom;
	protected SortedSet<Groupe> groupes = new TreeSet<>();
	protected String identifiant;
	protected String hashedPassword;

	/**
	 * 
	 * @param nom               : Nom de l'utilisateur
	 * @param prenom            : prenom de l'utilisateur
	 * @param identifiant       : identifiant de l'utilisateur
	 * @param notHashedPassword : mot de passe de l'utilisateur, en clair ! le
	 *                          constructeur hash le mot de passe
	 * @param gs                : la liste des groupes de l'utilisateur (vararg)
	 */
	public Utilisateur(String nom, String prenom, String identifiant, String notHashedPassword, Groupe... gs) {
		this.nom = nom;
		this.prenom = prenom;
		this.identifiant = identifiant;
		try {
			this.hashedPassword = new String(
					MessageDigest.getInstance("md5").digest(notHashedPassword.getBytes(StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		for (Groupe g : gs) {
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

	public String getHashedPassword() {
		return hashedPassword;
	}

	@Override
	public String toString() {
		return "[Nom : " + nom + ", Prenom : " + prenom + ", Identifiant : " + identifiant + ", MDP HASH "
				+ hashedPassword + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		}
		return ((Utilisateur) o).identifiant.equals(identifiant);
	}
}
