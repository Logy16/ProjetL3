package global.dto;

import java.io.Serializable;

public class GlobalDto implements Serializable {

	private static final long serialVersionUID = 6670353714413122416L;
	private TypeOperation operation;

	public enum TypeOperation {
		CREATION_FIL, DEMANDE_CONNEXION, SEND_MESSAGE, CONNEXION_REUSSIE, CONNEXION_ECHOUEE, CREER_GROUPE, ADD_AGENT,
		ADD_USER, ETABLISSEMENT_CONNEXION, ADD_USER_TO_GROUPE, LIRE_FIL, GET_MESSAGE_STATE, MODIFY_FIRSTNAME,
		MODIFY_LASTNAME, DELETE_GROUPE, DELETE_USER, GET_FIL_INT, GET_FIL_STRING, GET_FIL_GROUPE, GET_GROUPE_STRING,
		GET_GROUPE_UTILISATEUR, GET_UTILISATEUR_STRING, GET_GROUPE;
	}

	public GlobalDto(TypeOperation type) {
		this.operation = type;
	}

	public TypeOperation getOperation() {
		return operation;
	}

	public void setOperation(TypeOperation operation) {
		this.operation = operation;
	}
}
