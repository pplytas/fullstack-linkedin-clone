package server.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.endpoints.outputmodels.ChatMessageOutputModel;
import server.endpoints.outputmodels.ChatOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.ChatEntity;
import server.entities.UserEntity;
import server.utilities.StorageManager;

@Service
public class MessageService {

	@Autowired
	private UserEntityService userEntityService;
	
	public ChatOutputModel messagesToChatOutput(UserEntity chattingUser, List<ChatEntity> messages) {
		ChatOutputModel output = new ChatOutputModel();
		try {
		output.setChattingUser(userEntityService.getUserOutputModelFromUser(chattingUser));
		} catch (IOException e) {
			output.setChattingUser(userEntityService.getSafeUserOutputModelFromUser(chattingUser));
		}
		if (messages != null) {
			for (ChatEntity c : messages) {
				ChatMessageOutputModel mOut = new ChatMessageOutputModel();
				mOut.setMessage(c.getMessage());
				mOut.setSender(c.getSender().getId());
				mOut.setReceiver(c.getReceiver().getId());
				output.addMessage(mOut);
			}
		} else {
			output.setMessages(new ArrayList<>());
		}
		return output;
	}

}
