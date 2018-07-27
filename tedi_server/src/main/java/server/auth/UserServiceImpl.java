package server.auth;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import server.entities.RoleEntity;
import server.entities.UserEntity;
import server.repositories.RoleRepository;
import server.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptEncoder;
	
	//todo properly initialize db through hibernate instead
	@PostConstruct
	private void initializeDB() {
		if (roleRepo.findByName("ROLE_ADMIN") == null) {
			RoleEntity adminRole = new RoleEntity("ROLE_ADMIN");
			roleRepo.save(adminRole);
		}
		if (roleRepo.findByName("ROLE_USER") == null) {
			RoleEntity userRole = new RoleEntity("ROLE_USER");
			roleRepo.save(userRole);
		}
		if (findByEmail("d@root.com") == null) {
			UserEntity admin1 = new UserEntity.UserBuilder("d@root.com", "toor")
												.name("Dimitris").surname("Gounaris").build();
			saveAdmin(admin1);
		}
		if (findByEmail("p@root.com") == null) {
			UserEntity admin2 = new UserEntity.UserBuilder("p@root.com", "toor")
												.name("Panos").surname("Plytas").build();
			saveAdmin(admin2);
		}
	}
	
	private void saveAdmin(UserEntity user) {
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		user.setRole(roleRepo.findByName("ROLE_ADMIN"));
		userRepo.save(user);
	}
	
	@Override
	public void save(UserEntity user) {
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		//only users are created after the first configuration
		user.setRole(roleRepo.findByName("ROLE_USER"));
		userRepo.save(user);
	}
	
	@Override
	public String updateCredentials(String email, String password) {

		UserEntity origEntity = userRepo.findByEmail((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if (email == null || email.equals(""))
			email = origEntity.getEmail();
		if (password == null || password.equals("")) {
			password = origEntity.getPassword();
		}
		else
			password = bCryptEncoder.encode(password);
		origEntity.setEmail(email);
		origEntity.setPassword(password);
		System.out.println("US " + email);
		System.out.println("PW " + password);
		UserDetails updatedUser = new User(
										email,
										password,
										true,
										true,
										true,
										true,
										SecurityContextHolder.getContext().getAuthentication().getAuthorities()
										);
		SecurityContextHolder.getContext().setAuthentication(
											new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), updatedUser.getAuthorities())
										);
		userRepo.save(origEntity);
		return TokenAuthenticationService.addAuthentication(email);
	}
	
	@Override
	public UserEntity findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
}
