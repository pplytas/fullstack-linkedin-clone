package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.entities.ArticleEntity;
import server.entities.NotificationEntity;
import server.entities.UserEntity;
import server.repositories.NotificationRepository;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepo;
	
	public void addNotification(UserEntity user, UserEntity referencedUser) {
		NotificationEntity notification = new NotificationEntity();
		
	}

	public void addNotification(UserEntity user, UserEntity referencedUser, ArticleEntity referencedArticle) {
		
	}
	
	public String generateNotificationText(NotificationEntity notification) {
		
		return "";
	}
	
}
