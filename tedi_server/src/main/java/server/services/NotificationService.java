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
	
	public void addNotification(UserEntity user, UserEntity referencedUser, String message) {
		NotificationEntity notification = new NotificationEntity();
		notification.setUser(user);
		notification.setReferencedUser(referencedUser);
		notification.setDateTime();
		notification.setReferencedArticle(null);
		notification.setMessage(message);
		notificationRepo.save(notification);
	}

	public void addNotification(UserEntity user, UserEntity referencedUser, ArticleEntity referencedArticle, String message) {
		NotificationEntity notification = new NotificationEntity();
		notification.setUser(user);
		notification.setReferencedUser(referencedUser);
		notification.setDateTime();
		notification.setReferencedArticle(referencedArticle);
		notification.setMessage(message);
		notificationRepo.save(notification);
	}
	
}
