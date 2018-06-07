package server.auth;

import server.entities.UserEntity;

public interface UserService {

	void save(UserEntity user);
	
	void updateCredentials(String email, String password);
	
	UserEntity findByEmail(String email);
	
}