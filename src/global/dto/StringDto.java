package global.dto;

import java.io.Serializable;

public class StringDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = 7304528410163062552L;
	private String string;

	public StringDto(TypeOperation type, String string) {
		super(type);
		this.string = string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

}
