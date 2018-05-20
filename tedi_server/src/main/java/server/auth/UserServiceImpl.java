package server.auth;

import java.util.HashSet;
import java.util.Set;

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
import server.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptEncoder;
	
	//todo properly initialize db through hibernate instead
	@PostConstruct
	private void initializeAdmins() {
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
	
	@Override
	public void saveAdmin(UserEntity user) {
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		user.addRole(new RoleEntity("ADMIN"));
		userRepo.save(user);
	}
	
	@Override
	public void save(UserEntity user) {
		user.setPassword(bCryptEncoder.encode(user.getPassword()));
		Set<RoleEntity> roleSet = new HashSet<>();
		roleSet.add(new RoleEntity("USER")); //only users are created after the first configuration
		user.setRoles(roleSet);
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
