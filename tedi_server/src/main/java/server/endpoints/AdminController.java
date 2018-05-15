package server.endpoints;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.endpoints.outputmodels.UserListOutputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.UserEntity;
import server.repositories.UserRepository;
import server.utilities.StorageManager;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StorageManager sm;
	
	//todo add search parameters(?)
	@RequestMapping("/userlist")
	public ResponseEntity<Object> userList() {
		
		List<UserEntity> allUsers = userRepo.findAll();
		UserListOutputModel output = new UserListOutputModel();
		for (UserEntity u : allUsers) {
			try {
				output.addUser(new UserOutputModel.
									UserOutputBuilder(u.getEmail())
													.name(u.getName())
													.surname(u.getSurname())
													.telNumber(u.getTelNumber())
													.picture(sm.getFile(u.getPicture())).build());
			} catch (IOException e) {
				return new ResponseEntity<>("Could not load images", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
	}
	
}
