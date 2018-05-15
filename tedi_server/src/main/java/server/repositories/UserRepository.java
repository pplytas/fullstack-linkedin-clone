package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import server.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
	
}
