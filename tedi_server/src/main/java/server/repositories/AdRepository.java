package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.classification.Categories;
import server.entities.AdEntity;
import server.entities.UserEntity;

@Repository
public interface AdRepository extends JpaRepository<AdEntity, Long> {
	
	List<AdEntity> findByPublisher(UserEntity user);
	
	AdEntity findByTitleAndPublisherIsNull(String title);
	
	List<AdEntity> findByPublisherIsNotNull();
	
	List<AdEntity> findByCategoryAndPublisherIsNotNullOrderByPublishDateDesc(Categories category);
	
}
