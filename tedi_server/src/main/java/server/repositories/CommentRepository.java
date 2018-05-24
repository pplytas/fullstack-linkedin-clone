package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.ArticleEntity;
import server.entities.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

	List<CommentEntity> findByArticleOrderByDateTimeDesc(ArticleEntity article);
	
}
