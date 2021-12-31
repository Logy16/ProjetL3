package global.dto;

import java.io.Serializable;

public class DemandeDeConnexionDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 3179947332587538195L;
	private String login;
	private String password;

	public DemandeDeConnexionDto(String login, String password) {
		super(TypeOperation.DEMANDE_CONNEXION);
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
