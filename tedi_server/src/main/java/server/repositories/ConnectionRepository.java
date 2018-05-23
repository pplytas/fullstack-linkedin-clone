package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.ConnectionEntity;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {

}
