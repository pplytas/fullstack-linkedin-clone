package server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import server.entities.NotificationEntity;
import server.entities.UserEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
	
	List<NotificationEntity> findByUserOrderByDateTimeDesc(UserEntity user);

}
