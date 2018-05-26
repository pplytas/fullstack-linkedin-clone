package server.endpoints.outputmodels;

public class ChatMessageOutputModel {
	
	//here we can easily add a timestamp if needed
	private String message;
	private String sender; //defined by email
	private String receiver; //defined by email
	
	public ChatMessageOutputModel() {}

	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

}
