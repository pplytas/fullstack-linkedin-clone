package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import server.entities.ConnectionEntity;
import server.entities.UserEntity;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
	
	ConnectionEntity findByUserAndConnectedAndIsPending(UserEntity u, UserEntity c, Boolean isPending);
	
	@Query("SELECT c FROM ConnectionEntity c WHERE (user = ?1 OR connected = ?1) AND is_pending = ?2")
	List<ConnectionEntity> findByUserInversibleAndIsPending(UserEntity u1, Boolean isPending);
	
	List<ConnectionEntity> findByUserAndIsPending(UserEntity u, Boolean isPending);
	
	List<ConnectionEntity> findByConnectedAndIsPending(UserEntity u, Boolean isPending);

}
