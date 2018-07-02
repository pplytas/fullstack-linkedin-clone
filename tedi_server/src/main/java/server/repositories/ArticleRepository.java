package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import server.entities.ArticleEntity;
import server.entities.UserEntity;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
	
	@Query("SELECT a FROM ArticleEntity a "
			+ "WHERE "
			+ "(a.user IN (SELECT c1.user FROM ConnectionEntity c1 WHERE (c1.user = ?1 OR c1.connected = ?1) AND c1.isPending = false)) "
			+ "OR (a.user IN (SELECT c2.connected FROM ConnectionEntity c2 WHERE (c2.user = ?1 OR c2.connected = ?1) AND c2.isPending = false)) "
			+ "OR (a.id IN (SELECT u1.article FROM UpvoteEntity u1 WHERE u1.user IN (SELECT c3.user FROM ConnectionEntity c3 WHERE (c3.user = ?1 OR c3.connected = ?1) AND c3.isPending = false))) "
			+ "OR (a.id IN (SELECT u2.article FROM UpvoteEntity u2 WHERE u2.user IN (SELECT c4.connected FROM ConnectionEntity c4 WHERE (c4.user = ?1 OR c4.connected = ?1) AND c4.isPending = false))) ORDER BY a.dateTime DESC")
	List<ArticleEntity> findFeedOrderByDateTimeDesc(UserEntity user);
	
	List<ArticleEntity> findByUserOrderByDateTimeDesc(UserEntity user);
	
}
