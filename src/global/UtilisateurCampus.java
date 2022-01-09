package global;

public class UtilisateurCampus extends Utilisateur {
	private static final long serialVersionUID = 2449447806844893846L;

	/**
	 * @param nom      : le nom
	 * @param prenom   : le prenom
	 * @param id       : l'id
	 * @param password : le mot de passe en clair
	 **/
	public UtilisateurCampus(String nom, String prenom, String id, String password) {
		super(nom, prenom, id, password);
	}

	@Override
	public String toString() {
		return "[(UTILISATEUR) Nom : " + nom + ", Prenom : " + prenom + ", Identifiant : " + identifiant + ", MDP "
				+ password + "]";
	}

}
