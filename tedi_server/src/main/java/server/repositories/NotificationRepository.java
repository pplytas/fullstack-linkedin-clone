package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import server.entities.NotificationEntity;
import server.entities.UserEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
	
	List<NotificationEntity> findByUserOrderByDateTimeDesc(UserEntity user);

}
