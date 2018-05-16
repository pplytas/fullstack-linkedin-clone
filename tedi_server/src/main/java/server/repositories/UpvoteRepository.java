package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.ArticleEntity;
import server.entities.UpvoteEntity;
import server.entities.UserEntity;

public interface UpvoteRepository extends JpaRepository<UpvoteEntity, Long> {
	
	List<UpvoteEntity> findByUser(UserEntity user);
	
	List<UpvoteEntity> findByArticle(ArticleEntity article);

}
