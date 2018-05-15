package server.auth;

import server.entities.UserEntity;

public interface UserService {

	void save(UserEntity user);
	
	void saveAdmin(UserEntity user);
	
	UserEntity findByEmail(String email);
	
}
