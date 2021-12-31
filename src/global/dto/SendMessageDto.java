package global.dto;

import java.io.Serializable;

import global.Fil;
import global.Utilisateur;

public class SendMessageDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = -4059505066507490093L;
	private String message;
	private Utilisateur envoyeur;
	private Fil fil;

	public SendMessageDto(String message, Utilisateur envoyeur, Fil fil) {
		super(TypeOperation.SEND_MESSAGE);
		this.message = message;
		this.envoyeur = envoyeur;
		this.fil = fil;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Utilisateur getEnvoyeur() {
		return envoyeur;
	}

	public void setEnvoyeur(Utilisateur envoyeur) {
		this.envoyeur = envoyeur;
	}

	public Fil getFil() {
		return fil;
	}

	public void setFil(Fil fil) {
		this.fil = fil;
	}

}
