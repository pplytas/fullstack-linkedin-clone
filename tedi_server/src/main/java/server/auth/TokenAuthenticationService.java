package server.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import server.entities.UserEntity;
import server.repositories.UserRepository;

@Service
public class TokenAuthenticationService {
	
	private final UserRepository userRepo;
	
	@Autowired
	public TokenAuthenticationService(UserRepository userRepo) {
		this.userRepo = userRepo;
	}
	
	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
	
	public void addAuthentication(HttpServletResponse res, String username) {
		Claims claims = Jwts.claims();
		UserEntity currUser = userRepo.findByEmail(username);
		if (currUser.getRole().getName().equals("ROLE_ADMIN")) {
			res.setStatus(202);
			claims.put("auth", new SimpleGrantedAuthority("ROLE_ADMIN").getAuthority());
		} else {
			res.setStatus(200);
			claims.put("auth", new SimpleGrantedAuthority("ROLE_USER").getAuthority());
		}
		claims.setSubject(currUser.getId().toString());
		String JWT = Jwts.builder()
	        .setClaims(claims)
	        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	        .signWith(SignatureAlgorithm.HS512, SECRET)
	        .compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}
	
	public Authentication getAuthentication(HttpServletRequest request) {
		try {  
		String token = request.getHeader(HEADER_STRING);
		  if (token != null) {
			  // parse the token.
			  Long userId = Long.parseLong(Jwts.parser()
			      .setSigningKey(SECRET)
			      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
			          .getBody()
			          .getSubject());
			  UserEntity currUser = userRepo.findById(userId).orElse(null);
			  String authority = (String) Jwts.parser()
				      .setSigningKey(SECRET)
				      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				          .getBody().get("auth");
			  Set<GrantedAuthority> authorities = new HashSet<>();
			  authorities.add(new SimpleGrantedAuthority(authority));
			  return currUser != null ?
			          new UsernamePasswordAuthenticationToken(currUser.getId(), null, authorities) :
			          null;
		  }
		}catch (Exception e) {
			return null;
		}
	  return null;
	}
	
}
