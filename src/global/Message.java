package global;

import java.io.Serializable;
import java.util.Date;

public class Message implements Comparable<Message>, Serializable {
	private static final long serialVersionUID = 8002843001897960745L;
	private String texte;
	private Date date;
	private Utilisateur expediteur;
	private Fil fil;

	public Message(String texte, Date date, Utilisateur expediteur, Fil fil) {
		this.texte = texte;
		this.date = date;
		this.expediteur = expediteur;
		this.fil = fil;
		fil.getMessages().add(this);
	}

	public String getTexte() {
		return texte;
	}

	public Date getDate() {
		return date;
	}

	public Utilisateur getExpediteur() {
		return expediteur;
	}

	public Fil getFil() {
		return fil;
	}

	@Override
	public int compareTo(Message o) {
		if (o == null) {
			return 1;
		}
		if (getDate().getTime() != o.getDate().getTime()) {
			return getDate().compareTo(o.getDate());
		}
		if (!o.texte.equals(texte)) {
			return texte.compareTo(o.texte);
		}
		return expediteur.identifiant.compareTo(o.expediteur.identifiant);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		}
		return ((Message) o).texte.equals(texte) && ((Message) o).date.getTime() == date.getTime()
				&& ((Message) o).expediteur.equals(expediteur);
	}

	@Override
	public int hashCode() {
		return 31 * texte.hashCode();
	}
}
