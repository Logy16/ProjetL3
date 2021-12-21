package global;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Groupe implements Comparable<Groupe> {
	private String nom;
	private Set<Utilisateur> utilisateurs = new HashSet<>();
	
	public Groupe(String nom) {
		this.nom = nom;
	}
	
	public String getNom() {
		return nom;
	}
	
	/**
	 * @return Les utilisateurs du groupe
	 */
	public Utilisateur[] getUtilisateurs(){
		Utilisateur[] returned = new Utilisateur[utilisateurs.size()];
		utilisateurs.toArray(returned);
		return returned;
	}
	
	public void addUtilisateurs(Utilisateur... utilisateurs) {
		List<Utilisateur> utilisateurs2 = Arrays.asList(utilisateurs);
		this.utilisateurs.addAll(utilisateurs2);
		utilisateurs2.stream().forEach(user->{
			user.getGroupes().add(this);
		});
	}
	
	public void removeUtilisateurs(Utilisateur... utilisateurs) {
		List<Utilisateur> utilisateurs2 = Arrays.asList(utilisateurs);
		this.utilisateurs.removeAll(utilisateurs2);
		utilisateurs2.stream().forEach(user->{
			user.getGroupes().remove(this);
		});
	}

	@Override
	public int compareTo(Groupe o) {
		if(o == null) {
			return 1;
		}
		return nom.compareTo(o.nom);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || o.getClass() != getClass()) {
			return false;
		}
		return ((Groupe)o).nom.equals(nom);
	}
	
	@Override
	public int hashCode() {
		return 31*nom.hashCode(); 
	}
	
	
}