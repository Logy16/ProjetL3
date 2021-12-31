package global;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

public class Fil implements Comparable<Fil>, Serializable {

	private static final long serialVersionUID = -2595991243801274677L;
	private String sujet;
	private SortedSet<Message> messages = new TreeSet<>();
	private Groupe groupe;
	private Utilisateur createur;

	public Fil(String chaine, Groupe groupe, Utilisateur createur) {
		this.sujet = chaine;
		this.groupe = groupe;
		this.createur = createur;
	}

	public String getSujet() {
		return this.sujet;
	}

	public SortedSet<Message> getMessages() {
		return messages;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public Groupe getGroupe() {
		return groupe;
	}

	public Utilisateur getCreateur() {
		return createur;
	}

	@Override
	public int compareTo(Fil o) {
		if (o == null) {
			return 1;
		}
		if (o.messages.isEmpty() && messages.isEmpty()) {
			return 0;
		}
		if (o.messages.isEmpty()) {
			return 1;
		}
		if (messages.isEmpty()) {
			return -1;
		}
		if (!messages.first().equals(o.messages.first())) {
			return messages.first().compareTo(o.messages.first());
		}
		return createur.identifiant.compareTo(o.createur.identifiant);
	}

	@Override
	public int hashCode() {
		return 31 * sujet.hashCode() + 5 * createur.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		}
		return ((Fil) o).createur.equals(createur) && messages.first().equals(((Fil) o).messages.first());
	}

}
