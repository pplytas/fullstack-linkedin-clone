package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.ConnectionEntity;
import server.entities.UserEntity;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
	
	ConnectionEntity findByUserAndConnectedAndIsPending(UserEntity u, UserEntity c, Boolean isPending);

}
