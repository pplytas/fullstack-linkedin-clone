package server.endpoints;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.endpoints.outputmodels.UserListOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.ConnectionEntity;
import server.entities.UserEntity;
import server.repositories.ConnectionRepository;
import server.repositories.UserRepository;
import server.utilities.StorageManager;
import server.utilities.Validator;

@RestController
public class ConnectionController {
	
	@Autowired
	private StorageManager sm;
	
	@Autowired 
	private SecurityService secService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ConnectionRepository connRepo;

	//add a new connection between active user and the one specified by the parameter
	@PostMapping("/connect")
	public ResponseEntity<Object> connect(@RequestParam String email) {
		
		UserEntity currUser = secService.currentUser();
		UserEntity connUser = userRepo.findByEmail(email);
		if (connUser == null) {
			return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
		}
		//if the user we are trying to connect is admin, quietly reject with "not existing user" error
		//has to be done in separate if because connUser.getRoles() can throw NullPointerException
		if (!Validator.validateUserAuth(connUser)) {
			return new ResponseEntity<>("Not existing user with such email", HttpStatus.NOT_FOUND);
		}
		//cant make yourself a connected person to you
		if (currUser.getId() == connUser.getId()) {
			return new ResponseEntity<>("Can't connect to self", HttpStatus.BAD_REQUEST);
		}
		if (connRepo.findByUserAndConnectedAndIsPending(currUser, connUser, false) != null ||
				connRepo.findByUserAndConnectedAndIsPending(connUser, currUser, false) != null) {
			return new ResponseEntity<>("User is already connected", HttpStatus.CONFLICT);
		}
		else if (connRepo.findByUserAndConnectedAndIsPending(connUser, currUser, true) != null) {
			ConnectionEntity connection = connRepo.findByUserAndConnectedAndIsPending(connUser, currUser, true);
			connection.setIsPending(false);
			connRepo.save(connection);
			return new ResponseEntity<>("Connection request from " + email + " accepted", HttpStatus.OK);
		}
		else {
			ConnectionEntity connection = new ConnectionEntity(currUser, connUser, true);
			connRepo.save(connection);
			return new ResponseEntity<>("Connection request sent to " + email,HttpStatus.OK);
		}
		
	}
	
	//gets a user list with the connected or the pending to connect users of the active user
	//parameter specified which of the 2
	@GetMapping("/connections")
	public ResponseEntity<Object> getConnection(@RequestParam(defaultValue = "connected") String type) {
		
		UserEntity currUser = secService.currentUser();
		UserListOutputModel output = new UserListOutputModel();
		List<ConnectionEntity> requestedConnections;
		if (type.equals("connected")) {
			requestedConnections = connRepo.findByUserInversibleAndIsPending(currUser, false);
		}
		else if (type.equals("pending")) {
			requestedConnections = connRepo.findByConnectedAndIsPending(currUser, true);
		}
		else if (type.equals("sent")) {
			requestedConnections = connRepo.findByUserAndIsPending(currUser, true);
		}
		else {
			return new ResponseEntity<>("Parameter must be connected, pending or sent", HttpStatus.BAD_REQUEST);
		}
		try {
			for (ConnectionEntity c : requestedConnections) {
				UserEntity u;
				if (c.getUser().equals(currUser)) {
					u = c.getConnected();
				}
				else {
					u = c.getUser();
				}
				output.addUser(new UserOutputModel.UserOutputBuilder(u.getEmail())
													.name(u.getName())
													.surname(u.getSurname())
													.telNumber(u.getTelNumber())
													.picture(sm.getFile(u.getPicture())).build()
						);
			}
		} catch (IOException e) {
			return new ResponseEntity<>("Could not load profile pictures", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
		
	}
	
}
