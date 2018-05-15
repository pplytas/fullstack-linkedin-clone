package server.auth.handlers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	private final Logger mylogger = Logger.getLogger(LoginSuccessHandler.class.getName());
	
    @Override
    public void onAuthenticationSuccess(
    		
    	HttpServletRequest request,
    	HttpServletResponse response, 
    	Authentication authentication) 
    
    	throws ServletException, IOException {
    	
    		mylogger.info("LOGIN SUCCESS");
 	
    		response.setStatus(200);
    		response.sendRedirect("/account");
    }
}
