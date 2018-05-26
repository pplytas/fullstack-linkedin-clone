package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class ChatOutputModel {

	private List<ChatMessageOutputModel> messages;
	
	public ChatOutputModel() {
		messages = new ArrayList<>();
	}

	public List<ChatMessageOutputModel> getMessages() {
		return messages;
	}

	public void setMessages(List<ChatMessageOutputModel> messages) {
		this.messages = messages;
	}
	
	public void addMessage(ChatMessageOutputModel message) {
		this.messages.add(message);
	}
	
}
