package global;

public class UtilisateurCampus extends Utilisateur {
	
	/**
	 * @param nom : le nom
	 * @param prenom : le prenom
	 * @param id : l'id
	 * @param password : le mot de passe en clair
	 * **/
	public UtilisateurCampus(String nom, String prenom, String id, String password, Groupe... gs) {
		super(nom, prenom, id, password, gs);
	}
	
	@Override
	public String toString() {
		return "[(UTILISATEUR) Nom : "+nom+", Prenom : "+prenom+", Identifiant : "+identifiant+", MDP "+password+"]";
	}

}
