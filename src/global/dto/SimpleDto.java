package global.dto;

import java.io.Serializable;

public class SimpleDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = -4602469081828420721L;

	public SimpleDto(TypeOperation type) {
		super(type);
	}

}
