package global.dto;

import java.io.Serializable;

import global.Message;

public class GetMessageStateDto extends GlobalDto implements Serializable {
	private static final long serialVersionUID = 6793135879728437932L;
	private Message message;

	public GetMessageStateDto(Message message) {
		super(TypeOperation.GET_MESSAGE_STATE);
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
