package server.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import server.auth.SecurityService;
import server.endpoints.outputmodels.NotificationListOutputModel;
import server.endpoints.outputmodels.NotificationOutputModel;
import server.entities.NotificationEntity;
import server.entities.UserEntity;
import server.repositories.NotificationRepository;
import server.services.NotificationService;

import java.util.List;

@RestController
public class NotificationController {

	@Autowired
	private SecurityService secService;

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private NotificationRepository notificationRepo;

	@GetMapping("/notifications")
	public ResponseEntity<Object> getNotifications() {
		UserEntity currUser = secService.currentUser();
		List<NotificationEntity> notifications = notificationRepo.findByUserOrderByDateTimeDesc(currUser);
		NotificationListOutputModel output = new NotificationListOutputModel();
		for (NotificationEntity notification : notifications) {
			NotificationOutputModel nOut = new NotificationOutputModel();
			nOut.setMessage(notificationService.refreshMessage(notification));
			nOut.setRefUserId(notification.getReferencedUser().getId());
			if (notification.getReferencedArticle() != null) {
				nOut.setRefArticleId(notification.getReferencedArticle().getId());
			}
			nOut.setDateTime(notification.getDateTime());
			nOut.setSeen(notification.getSeen());
			output.addNotificationOutputModel(nOut);
			notification.setSeen(true);
			notificationRepo.save(notification);
		}
		return new ResponseEntity<>(output, HttpStatus.OK);
	}

}
