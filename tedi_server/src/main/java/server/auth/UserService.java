package server.auth;

import server.entities.UserEntity;

public interface UserService {

	void save(UserEntity user);
	
	String updateCredentials(String email, String password);
	
	UserEntity findByEmail(String email);
	
}