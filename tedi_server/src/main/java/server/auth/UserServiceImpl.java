package server.auth;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import server.classification.Categories;
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
		if (roleRepo.findByName("ADMIN") == null) {
			RoleEntity adminRole = new RoleEntity("ADMIN");
			roleRepo.save(adminRole);
		}
		if (roleRepo.findByName("USER") == null) {
			RoleEntity userRole = new RoleEntity("USER");
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
		//creating 3 test users for each category, to help in data training
		for (int i=1; i<=3; i++) {
			if (findByEmail("software" + i) == null) {
				UserEntity test = new UserEntity.UserBuilder("software" + i, null).build();
				test.setCategories(Categories.SOFTWARE);
				saveUnregistered(test);
			}
		}
		for (int i=1; i<=3; i++) {
			if (findByEmail("telecom" + i) == null) {
				UserEntity test = new UserEntity.UserBuilder("telecom" + i, null).build();
				test.setCategories(Categories.TELECOMMUNICATIONS);
				saveUnregistered(test);
			}
		}
		for (int i=1; i<=3; i++) {
			if (findByEmail("hr" + i) == null) {
				UserEntity test = new UserEntity.UserBuilder("hr" + i, null).build();
				test.setCategories(Categories.HR);
				saveUnregistered(test);
			}
		}
	}
	
	private void saveAdmin(UserEntity user) {
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		user.setRole(roleRepo.findByName("ADMIN"));
		userRepo.save(user);
	}
	
	//to disable logins in this user, all we need to do is to set email to something non permitted (to avoid conflicts on new registered users)
	//and not save a role for the user, thus not saving him in the SpringSecurity table
	private void saveUnregistered(UserEntity user) {
		user.setPassword(null); //null password to differentiate when looking at db
		userRepo.save(user);
	}
	
	@Override
	public void save(UserEntity user) {
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		//only users are created after the first configuration
		user.setRole(roleRepo.findByName("USER"));
		userRepo.save(user);
	}
	
	@Override
	public void updateCredentials(String email, String password) {
		//basically we update both the Spring Security UserDetails entity and out UserEntity database table
		UserDetails origUser = (UserDetails) SecurityContextHolder.getContext()
												.getAuthentication().getPrincipal();
		UserEntity origEntity = userRepo.findByEmail(origUser.getUsername());
		if (email.equals(""))
			email = origUser.getUsername();
		if (password.equals("")) {
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
										origUser.isEnabled(),
										origUser.isAccountNonExpired(),
										origUser.isCredentialsNonExpired(),
										origUser.isAccountNonLocked(),
										origUser.getAuthorities()
										);
		SecurityContextHolder.getContext().setAuthentication(
											new UsernamePasswordAuthenticationToken(updatedUser, updatedUser.getPassword(), updatedUser.getAuthorities())
										);
		userRepo.save(origEntity);
		System.out.println(((UserDetails)(SecurityContextHolder.getContext()
												.getAuthentication().getPrincipal())).getUsername());
	}
	
	@Override
	public UserEntity findByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
}
