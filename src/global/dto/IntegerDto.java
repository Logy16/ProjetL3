package global.dto;

import java.io.Serializable;

public class IntegerDto extends GlobalDto implements Serializable {

	private static final long serialVersionUID = -6620694073268212273L;
	private int integer;

	public IntegerDto(TypeOperation type, int integer) {
		super(type);
		this.integer = integer;
	}

	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}

}
