package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import server.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String email);
	
	@Query("SELECT u FROM UserEntity u WHERE u.email != ?1 AND (u.role is null OR u.role != 1)")
	List<UserEntity> findByEmailNotAndRoleNotAdminOrIsNull(String email);
	
	List<UserEntity> findByNameContainingOrSurnameContaining(String partialName, String partialSurname);
	
	List<UserEntity> findByNameContainingAndSurnameContaining(String partialName, String partialSurname);
		
	List<UserEntity> findAll();
	
}
