package server.endpoints.inputmodels;

public class ChatMessageInputModel {
	
	private String message;
	private String email; //this is the email of the user we chat with
	
	public ChatMessageInputModel() {}

	public String getMessage() {
		return message;
	}

	public String getEmail() {
		return email;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
