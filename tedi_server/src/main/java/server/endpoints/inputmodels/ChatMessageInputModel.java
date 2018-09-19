package server.endpoints.inputmodels;

public class ChatMessageInputModel {
	
	private String message;
	private Long id; //this is the id of the user we chat with
	
	public ChatMessageInputModel() {}

	public String getMessage() {
		return message;
	}

	public Long getId() {
		return id;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
