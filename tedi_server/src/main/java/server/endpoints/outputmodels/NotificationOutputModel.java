package server.endpoints.outputmodels;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationOutputModel {

	private String message;
	private Long refUserId;
	private Long refArticleId;
	private String dateTime;
	private Boolean seen;

	public NotificationOutputModel() {}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getRefUserId() {
		return refUserId;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	
	public Boolean getSeen() {
		return seen;
	}

	public void setRefUserId(Long refUserId) {
		this.refUserId = refUserId;
	}

	public Long getRefArticleId() {
		return refArticleId;
	}

	public void setRefArticleId(Long refArticleId) {
		this.refArticleId = refArticleId;
	}
	
	public void setDateTime(Date dateTime) {
		if (dateTime == null) {
			this.dateTime = null;
			return;
		}
			
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.dateTime = sdf.format(dateTime);
	}
	
	public void setSeen(Boolean seen) {
		this.seen = seen;
	}
}
