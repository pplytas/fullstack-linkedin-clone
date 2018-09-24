package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.AdApplicationEntity;
import server.entities.AdEntity;
import server.entities.UserEntity;

@Repository
public interface AdApplicationRepository extends JpaRepository<AdApplicationEntity, Long> {
	
	List<AdApplicationEntity> findByAd(AdEntity ad);

	AdApplicationEntity findByAdAndUser(AdEntity ad, UserEntity user);

	List<AdApplicationEntity> findByUser(UserEntity user);
	
}
