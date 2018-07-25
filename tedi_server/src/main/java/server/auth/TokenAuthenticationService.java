package server.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {
	
	static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
	
	static String addAuthentication(String username) {
		Claims claims = Jwts.claims();
		if (username.equals("p@root.com") || username.equals("d@root.com")) {
			claims.put("auth", new SimpleGrantedAuthority("ROLE_ADMIN").getAuthority());
		} else {
			claims.put("auth", new SimpleGrantedAuthority("ROLE_USER").getAuthority());
		}
		claims.setSubject(username);
	   String JWT = Jwts.builder()
	        .setClaims(claims)
	        .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
	        .signWith(SignatureAlgorithm.HS512, SECRET)
	        .compact();
	   return JWT;
	}
	
	static void addAuthentication(HttpServletResponse res, String username) {
		Claims claims = Jwts.claims();
		if (username.equals("p@root.com") || username.equals("d@root.com")) {
			claims.put("auth", new SimpleGrantedAuthority("ROLE_ADMIN").getAuthority());
		} else {
			claims.put("auth", new SimpleGrantedAuthority("ROLE_USER").getAuthority());
		}
		claims.setSubject(username);
	   String JWT = Jwts.builder()
	        .setClaims(claims)
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
			  String user = (String) Jwts.parser()
			      .setSigningKey(SECRET)
			      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
			          .getBody()
			          .getSubject();
			  System.out.println(user);
			  String authority = (String) Jwts.parser()
				      .setSigningKey(SECRET)
				      .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
				          .getBody().get("auth");
			  System.out.println(authority);
			  Set<GrantedAuthority> authorities = new HashSet<>();
			  authorities.add(new SimpleGrantedAuthority(authority));
			  return user != null ?
			          new UsernamePasswordAuthenticationToken(user, null, authorities) :
			          null;
		  }
		}catch (Exception e) {
			return null;
		}
	  return null;
	}
	
}
