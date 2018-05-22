package server.endpoints;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.auth.UserService;
import server.endpoints.inputmodels.RegisterInputModel;
import server.endpoints.outputmodels.UserOutputModel;
import server.entities.UserEntity;
import server.utilities.StorageManager;
import server.utilities.Validator;

@RestController
//class that handles register, login, logout and account display
public class AuthController {

	@Autowired
	private StorageManager sm;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private SecurityService secService;
	
	@PostMapping("/register")
	public ResponseEntity<Object> register(@RequestBody RegisterInputModel input, HttpServletRequest request) {
		if (!Validator.validateEmail(input.getEmail())) {
			return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity<>(new UserOutputModel.UserOutputBuilder(user.getEmail())
																			.name(user.getName())
																			.surname(user.getSurname())
																			.telNumber(user.getTelNumber())
																			.picture(sm.getFile(user.getPicture())).build()
					, HttpStatus.CREATED);
		} catch (IOException e) {
			return new ResponseEntity<Object>("Couldn't save picture", HttpStatus.INTERNAL_SERVER_ERROR);
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
	
	@GetMapping(value="/account")
	public ResponseEntity<Object> getAccount() {
		
		try {
			
			UserEntity user = secService.currentUser();
			
			if (user == null)
				return new ResponseEntity<>("No active user found", HttpStatus.NOT_FOUND);

			return new ResponseEntity<>(
					new UserOutputModel.UserOutputBuilder(user.getEmail())
														.name(user.getName())
														.surname(user.getSurname())
														.telNumber(user.getTelNumber())
														.picture(sm.getFile(user.getPicture())).build()
					, HttpStatus.OK);
		}
		catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
