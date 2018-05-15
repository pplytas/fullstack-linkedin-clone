package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import server.auth.SecurityService;
import server.repositories.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired 
	private SecurityService secService;
	
	@Autowired
	private UserRepository userRepo;

}
