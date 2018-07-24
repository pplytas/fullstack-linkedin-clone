package server.auth;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Collections;

public class TokenAuthenticationService {
	
	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
	
	static void addAuthentication(HttpServletResponse res, String username) {
	   String JWT = Jwts.builder()
	        .setSubject(username)
	        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	        .signWith(SignatureAlgorithm.HS512, SECRET)
	        .compact();
	   res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}
	
	static Authentication getAuthentication(HttpServletRequest request) {
		try {  
		String token = request.getHeader(HEADER_STRING);
		  if (token != null) {
			  // parse the token.
			  String user = Jwts.parser()
			      .setSigningKey(SECRET)
			      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
			          .getBody()
			          .getSubject();
			
			  return user != null ?
			          new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()) :
			          null;
		  }
		}catch (Exception e) {
			return null;
		}
	  return null;
	}
	
}
