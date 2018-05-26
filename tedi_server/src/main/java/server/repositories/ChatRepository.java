package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import server.entities.ChatEntity;
import server.entities.UserEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

	@Query("SELECT c FROM ChatEntity c WHERE (sender = ?1 AND receiver = ?2) OR (sender = ?2 AND receiver = ?1) ORDER BY id")
	List<ChatEntity> findBySenderAndReceiverInversibleOrderByIdDesc(UserEntity u1, UserEntity u2);
	
}
