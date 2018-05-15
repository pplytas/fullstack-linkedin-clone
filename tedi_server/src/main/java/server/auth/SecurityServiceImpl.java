package server.auth;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import server.entities.UserEntity;
import server.repositories.UserRepository;

@Service
public class SecurityServiceImpl implements SecurityService {

	private final Logger mylogger = Logger.getLogger(SecurityServiceImpl.class.getName());
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private AuthenticationManager authMan;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public UserEntity currentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try {
			User authUser =  (User) auth.getPrincipal();
			return userRepo.findByEmail(authUser.getUsername());
		} catch (Exception e) {
			mylogger.log(Level.INFO, "Exception", e);
			return null;
		}
	}
	
	
	//the 2 following functions don't seem to work
	@Override
	public void autologin(String email, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
		authMan.authenticate(usernamePasswordAuthenticationToken);
		if (usernamePasswordAuthenticationToken.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
	}
	
	public void authenticateUserAndSetSession(UserEntity user, HttpServletRequest request) {

        String email = user.getEmail();
        String password = user.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authMan.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }  
	
}
