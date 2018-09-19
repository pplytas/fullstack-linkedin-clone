package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auth.SecurityService;
import server.endpoints.inputmodels.ChatMessageInputModel;
import server.endpoints.outputmodels.ChatListOutputModel;
import server.endpoints.outputmodels.ChatOutputModel;
import server.endpoints.outputmodels.LastChatOpenedOutputModel;
import server.entities.ChatEntity;
import server.entities.ConnectionEntity;
import server.entities.UserEntity;
import server.repositories.ChatRepository;
import server.repositories.ConnectionRepository;
import server.repositories.UserRepository;
import server.services.MessageService;
import server.utilities.Validator;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MessageController {
	
	@Autowired 
	private SecurityService secService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ConnectionRepository connRepo;
	
	@Autowired
	private ChatRepository chatRepo;
	
	@Autowired
	private MessageService messageService;

	@GetMapping("/messages/lastuser")
	public ResponseEntity<Object> getLastChatOpenedUserId() {

		UserEntity currUser = secService.currentUser();
		LastChatOpenedOutputModel lastChatOpenedOutputModel = new LastChatOpenedOutputModel();
		lastChatOpenedOutputModel.setId(currUser.getLastChatOpenedUserId());
		return new ResponseEntity<>(lastChatOpenedOutputModel, HttpStatus.OK);

	}

	@PutMapping("/messages/lastuser")
	public ResponseEntity<Object> updateLastChatOpenedUserId(@RequestParam Long id) {

		UserEntity currUser = secService.currentUser();
		UserEntity chatUser = userRepo.findById(id).orElse(null);
		if (chatUser == null) {
			return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
		} else if (chatUser.getId().equals(currUser.getId())) {
			return new ResponseEntity<>("Can't message self", HttpStatus.BAD_REQUEST);
		}
		currUser.setLastChatOpenedUserId(id);
		userRepo.save(currUser);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/message")
	public ResponseEntity<Object> message(@RequestBody ChatMessageInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		UserEntity otherUser = userRepo.findById(input.getId()).orElse(null);
		
		if (otherUser == null) {
			return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
		}
		if (otherUser.getId().equals(currUser.getId())) { //this is optional, i.e. facebook permits this!
			return new ResponseEntity<>("Can't message self", HttpStatus.BAD_REQUEST);
		}
		//check if the 2 users are connected (optional again)
		if (connRepo.findByUserAndConnectedAndIsPending(currUser, otherUser, false) == null &&
			connRepo.findByUserAndConnectedAndIsPending(otherUser, currUser, false) == null) {
			return new ResponseEntity<>("Can't message not connected user", HttpStatus.NOT_ACCEPTABLE);
		}
		
		ChatEntity newMessage = new ChatEntity();
		newMessage.setSender(currUser);
		newMessage.setReceiver(otherUser);
		newMessage.setMessage(input.getMessage());
		chatRepo.save(newMessage);
		return new ResponseEntity<>(HttpStatus.OK);
		
	}
	
	//gets the messages between current user and user specified by id
	@GetMapping("/messages")
	public ResponseEntity<Object> messages(@RequestParam Long id) {
		
		UserEntity currUser = secService.currentUser();
		UserEntity otherUser = userRepo.findById(id).orElse(null);
		if (otherUser == null) {
			return new ResponseEntity<>("Not existing user with such id", HttpStatus.NOT_FOUND);
		}
		if (!Validator.validateUserAuth(otherUser)) {
			return new ResponseEntity<>("Not existing user with such id", HttpStatus.NOT_FOUND);
		}
		//I dont check for connected or not users, but can be easily implemented if needed
		List<ChatEntity> messages = chatRepo.findBySenderAndReceiverInversibleOrderByIdDesc(currUser, otherUser);
		ChatOutputModel output = messageService.messagesToChatOutput(otherUser, messages);
		currUser.setLastChatOpenedUserId(otherUser.getId());
		userRepo.save(currUser);
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
	@GetMapping("/messages/all")
	public ResponseEntity<Object> allMessages() {
		
		UserEntity currUser = secService.currentUser();
		List<ConnectionEntity> connections = connRepo.findByUserInversibleAndIsPending(currUser, false);
		ChatListOutputModel output = new ChatListOutputModel();
		List<ChatOutputModel> chatOutputModels = new ArrayList<>();
		LastChatOpenedOutputModel lastChatOpenedOutputModel = new LastChatOpenedOutputModel();
		for (ConnectionEntity conn : connections) {
			List<ChatEntity> messages = chatRepo.findBySenderAndReceiverInversibleOrderByIdDesc(conn.getUser(), conn.getConnected());
			ChatOutputModel chatOutput;
			if (conn.getUser().getEmail().equals(currUser.getEmail())) {
				chatOutput = messageService.messagesToChatOutput(conn.getConnected(), messages);
			}
			else {
				chatOutput = messageService.messagesToChatOutput(conn.getUser(), messages);
			}
			chatOutputModels.add(chatOutput);
		}
		output.setChats(chatOutputModels);
		lastChatOpenedOutputModel.setId(currUser.getLastChatOpenedUserId());
		output.setLastOpened(lastChatOpenedOutputModel);
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
}
