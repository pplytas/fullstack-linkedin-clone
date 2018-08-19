package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class ChatOutputModel {

	private UserOutputModel chattingUser;
	private List<ChatMessageOutputModel> messages;
	
	public ChatOutputModel() {
		messages = new ArrayList<>();
	}

	public List<ChatMessageOutputModel> getMessages() {
		return messages;
	}

	public UserOutputModel getChattingUser() {
		return chattingUser;
	}
	
	public void setMessages(List<ChatMessageOutputModel> messages) {
		this.messages = messages;
	}
	
	public void addMessage(ChatMessageOutputModel message) {
		this.messages.add(message);
	}
	
	public void setChattingUser(UserOutputModel chattingUser) {
		this.chattingUser = chattingUser;
	}
	
}
