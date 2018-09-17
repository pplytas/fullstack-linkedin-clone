package server.endpoints.outputmodels;

public class ChatMessageOutputModel {
	
	//here we can easily add a timestamp if needed
	private String message;
	private Long sender; //defined by id
	private Long receiver; //defined by id
	
	public ChatMessageOutputModel() {}

	public String getMessage() {
		return message;
	}

	public Long getSender() {
		return sender;
	}

	public Long getReceiver() {
		return receiver;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSender(Long sender) {
		this.sender = sender;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
	}

}
