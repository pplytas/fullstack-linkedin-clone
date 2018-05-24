package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.ConnectionEntity;

@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {

}
