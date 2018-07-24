package server.auth;

import server.entities.UserEntity;

public interface SecurityService {

	UserEntity currentUser();
		
}
