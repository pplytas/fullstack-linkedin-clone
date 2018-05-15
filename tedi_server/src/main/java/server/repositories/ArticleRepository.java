package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.ArticleEntity;
import server.entities.UserEntity;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

	List<ArticleEntity> findByUser(UserEntity user);
	
}
