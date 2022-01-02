package global.dto;

import java.io.Serializable;

public class GlobalDto implements Serializable {

	private static final long serialVersionUID = 6670353714413122416L;
	public TypeOperation operation;

	public enum TypeOperation {
		CREATION_FIL, DEMANDE_CONNEXION, SEND_MESSAGE, CONNEXION_REUSSIE, CONNEXION_ECHOUEE, CREER_GROUPE, ADD_USER,
		ETABLISSEMENT_CONNEXION;
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
