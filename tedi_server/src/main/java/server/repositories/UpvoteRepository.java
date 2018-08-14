package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.ArticleEntity;
import server.entities.UpvoteEntity;
import server.entities.UserEntity;

@Repository
public interface UpvoteRepository extends JpaRepository<UpvoteEntity, Long> {
	
	List<UpvoteEntity> findByUser(UserEntity user);
	
	List<UpvoteEntity> findByArticle(ArticleEntity article);
	
	UpvoteEntity findByArticleAndUser(ArticleEntity article, UserEntity user);

}
