package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.ArticleEntity;
import server.entities.UserEntity;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
	
	List<ArticleEntity> findByUserOrderByDateTimeDesc(UserEntity user);
	
}
