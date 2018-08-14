package server.endpoints.outputmodels;

import java.util.ArrayList;
import java.util.List;

public class NotificationListOutputModel {

	List<NotificationOutputModel> notificationOutputModelList;

	public NotificationListOutputModel() {
		notificationOutputModelList = new ArrayList<>();
	}

	public List<NotificationOutputModel> getNotificationOutputModelList() {
		return notificationOutputModelList;
	}

	public void addNotificationOutputModel(NotificationOutputModel notificationOutputModel) {
		this.notificationOutputModelList.add(notificationOutputModel);
	}

	public void setNotificationOutputModelList(List<NotificationOutputModel> notificationOutputModelList) {
		this.notificationOutputModelList = notificationOutputModelList;
	}
}
