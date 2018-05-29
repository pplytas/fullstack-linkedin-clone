package server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.AdEntity;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {
	
}
