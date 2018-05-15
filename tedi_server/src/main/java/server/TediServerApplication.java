package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import server.auth.UserService;
import server.entities.UserEntity;

@SpringBootApplication
public class TediServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TediServerApplication.class, args);
	}
	
}
