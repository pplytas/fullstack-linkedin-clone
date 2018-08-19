package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.AdApplicationEntity;
import server.entities.AdEntity;

@Repository
public interface AdApplicationRepository extends JpaRepository<AdApplicationEntity, Long> {
	
	List<AdApplicationEntity> getByAd(AdEntity ad);

}
