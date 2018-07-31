package server.auth;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import server.entities.UserEntity;
import server.repositories.UserRepository;

@Service
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	private TokenAuthenticationService tokenAuthService;
	private UserRepository userRepo;
	
	private static final String url = "/login";
	
	@Autowired
	public JWTLoginFilter(UserRepository userRepo, TokenAuthenticationService tokenAuthService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		this.userRepo = userRepo;
		this.tokenAuthService = tokenAuthService;
	    setAuthenticationManager(authManager);
	}
	

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
	     throws AuthenticationException, IOException, ServletException {
		 AccountCredentials creds = new ObjectMapper()
		        .readValue(req.getInputStream(), AccountCredentials.class);
		 UserEntity currUser = userRepo.findByEmail(creds.getEmail());
	  Set<GrantedAuthority> authorities = new HashSet<>();
	  if (currUser != null) {
		  authorities.add(new SimpleGrantedAuthority(currUser.getRole().getName()));
	  }
	  return getAuthenticationManager().authenticate(
    			new UsernamePasswordAuthenticationToken(
    				creds.getEmail(),
    				creds.getPassword(),
    				authorities
        		)
    		);
	}
	
	  @Override
	  protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		  tokenAuthService
	        .addAuthentication(res, auth.getName());
	  }

}
