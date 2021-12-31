package global;

public class Agents extends Utilisateur {
	
	
	/**
	 * @param nom : le nom
	 * @param prenom : le prenom
	 * @param id : l'id
	 * @param password : le mot de passe en clair
	 * **/
	public Agents(String nom, String prenom, String id, String password, Groupe... gs) {
		super(nom, prenom, id, password, gs);
	}
	
	@Override
	public String toString() {
		return "[(AGENT) Nom : "+nom+", Prenom : "+prenom+", Identifiant : "+identifiant+", MDP "+password+"]";
	}
}
