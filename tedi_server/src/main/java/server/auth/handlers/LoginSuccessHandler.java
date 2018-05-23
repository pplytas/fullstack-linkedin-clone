package server.auth.handlers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import server.auth.SecurityService;
import server.entities.RoleEntity;


@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final Logger mylogger = Logger.getLogger(LoginSuccessHandler.class.getName());
	
	@Autowired
	private SecurityService secService;
	
    @Override
    public void onAuthenticationSuccess(
    		
    	HttpServletRequest request,
    	HttpServletResponse response, 
    	Authentication authentication) 
    
    	throws ServletException, IOException {
    	
    		mylogger.info("LOGIN SUCCESS");
 	
    		response.setStatus(200);
    		boolean isAdmin = false;
    		for (RoleEntity r : secService.currentUser().getRoles()) {
    			if (r.getName().equals("ADMIN")) {
    				isAdmin = true;
    			}
    		}
    		if (isAdmin)
    			response.sendRedirect("/admin/userlist");
    		else
    			response.sendRedirect("/view/account");
    }
}
