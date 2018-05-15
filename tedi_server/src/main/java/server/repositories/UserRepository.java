package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import server.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
	
	List<UserEntity> findAll();
	
}
