package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.auth.UserService;
import server.endpoints.inputmodels.RegisterInputModel;
import server.entities.UserEntity;
import server.services.UserEntityService;
import server.utilities.StorageManager;
import server.utilities.Validator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
//class that handles register, login, logout and account display
public class AuthController {

	@Autowired
	private StorageManager sm;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserEntityService userEntityService;
	
	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestBody RegisterInputModel input, HttpServletRequest request) {
		if (!Validator.validateEmail(input.getEmail())) {
			return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
		}
		if (input.getPassword() == null || input.getPassword().equals("")) {
			String msg = "Can't register with empty password";
			return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
		}
		if (input.getName() == null || input.getName().equals("") 
			|| input.getSurname() == null || input.getSurname().equals("")) {
			String msg = "Can't register with empty name or surname";
			return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
		}
		if (userService.findByEmail(input.getEmail()) != null) {
			String msg = "A user with this email is already registered";
			return new ResponseEntity<>(msg, HttpStatus.CONFLICT);
		}
		try {
			UserEntity user = new UserEntity.UserBuilder(input.getEmail(), input.getPassword())
												.name(input.getName())
												.surname(input.getSurname())
												.telNumber(input.getTelNumber())
												.picture(sm.storeFile(input.getPicture())).build();
			userService.save(user);
			return new ResponseEntity<>(userEntityService.getUserOutputModelFromUser(user)
					, HttpStatus.CREATED);
		} catch (IOException e) {
			return new ResponseEntity<>("Couldn't save picture", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/login")
	public void login(@RequestParam("email") String e,
					  @RequestParam("password") String p) {

    	// (the actual login is handled by the Security framework)
    }
	
	@GetMapping(value="/logout")
	public void logout () {
		// (the actual logout is handled by the Security framework)
	}
	
}
