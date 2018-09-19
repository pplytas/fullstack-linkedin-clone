package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auth.SecurityService;
import server.endpoints.inputmodels.ChatMessageInputModel;
import server.endpoints.outputmodels.ChatOutputModel;
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

	@PostMapping("/message")
	public ResponseEntity<Object> message(@RequestBody ChatMessageInputModel input) {
		
		UserEntity currUser = secService.currentUser();
		UserEntity otherUser = userRepo.findById(input.getId()).orElse(null);
		
		if (otherUser == null) {
			return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
		}
		if (otherUser.getId() == currUser.getId()) { //this is optional, i.e. facebook permits this!
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
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
	@GetMapping("/messages/all")
	public ResponseEntity<Object> allMessages() {
		
		UserEntity currUser = secService.currentUser();
		List<ConnectionEntity> connections = connRepo.findByUserInversibleAndIsPending(currUser, false);
		List<ChatOutputModel> output = new ArrayList<>();
		for (ConnectionEntity conn : connections) {
			List<ChatEntity> messages = chatRepo.findBySenderAndReceiverInversibleOrderByIdDesc(conn.getUser(), conn.getConnected());
			ChatOutputModel chatOutput;
			if (conn.getUser().getEmail().equals(currUser.getEmail())) {
				chatOutput = messageService.messagesToChatOutput(conn.getConnected(), messages);
			}
			else {
				chatOutput = messageService.messagesToChatOutput(conn.getUser(), messages);
			}
			output.add(chatOutput);
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
}
